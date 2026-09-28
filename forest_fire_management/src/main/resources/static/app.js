const incidentRows = document.querySelector('#incident-rows');
const listFeedback = document.querySelector('#list-feedback');
const formFeedback = document.querySelector('#form-feedback');
const reportForm = document.querySelector('#report-form');

document.querySelector('#current-date').textContent = new Intl.DateTimeFormat('en', {
    weekday: 'short', month: 'short', day: 'numeric'
}).format(new Date());

function escapeHtml(value) {
    return String(value ?? '').replace(/[&<>"']/g, character => ({
        '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
    })[character]);
}

async function request(path, options = {}) {
    const response = await fetch(path, {
        ...options,
        headers: { 'Content-Type': 'application/json', ...options.headers }
    });

    if (!response.ok) {
        const problem = await response.json().catch(() => ({}));
        throw new Error(problem.detail || `Request failed (${response.status})`);
    }

    return response.status === 204 ? null : response.json();
}

function formatDate(dateValue) {
    return new Intl.DateTimeFormat('en', { month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' })
        .format(new Date(dateValue));
}

function renderIncidents(incidents) {
    document.querySelector('#incident-count').textContent = `${incidents.length} REPORT${incidents.length === 1 ? '' : 'S'}`;

    const openIncidents = incidents.filter(incident => incident.status !== 'RESOLVED');
    document.querySelector('#open-count').textContent = openIncidents.length;
    document.querySelector('#priority-count').textContent = openIncidents.filter(incident =>
        incident.severity === 'HIGH' || incident.severity === 'CRITICAL'
    ).length;
    document.querySelector('#ranger-count').textContent = new Set(openIncidents
        .filter(incident => incident.assignedRanger)
        .map(incident => incident.assignedRanger)).size;

    if (incidents.length === 0) {
        incidentRows.innerHTML = '<tr><td class="empty-state" colspan="6">No reports yet. Add the first incident below.</td></tr>';
        return;
    }

    incidentRows.innerHTML = incidents.map(incident => {
        const severityClass = incident.severity.toLowerCase();
        const statusClass = incident.status.toLowerCase();
        const ranger = incident.assignedRanger
            ? `<span class="ranger-name">${escapeHtml(incident.assignedRanger)}</span>`
            : '<span class="ranger-empty">Unassigned</span>';

        return `<tr>
            <td><span class="location-name">${escapeHtml(incident.location)}</span><span class="incident-id">INC-${incident.id}</span></td>
            <td><span class="severity severity-${severityClass}">${escapeHtml(incident.severity)}</span></td>
            <td>${formatDate(incident.reportedAt)}</td>
            <td>${ranger}</td>
            <td><span class="status status-${statusClass}">${escapeHtml(incident.status)}</span></td>
            <td><div class="action-controls">
                <input aria-label="Ranger name for incident ${incident.id}" placeholder="Ranger name" value="${escapeHtml(incident.assignedRanger || '')}" data-ranger-for="${incident.id}">
                <button type="button" data-action="dispatch" data-id="${incident.id}">Assign</button>
                <select aria-label="Status for incident ${incident.id}" data-status-for="${incident.id}">
                    ${['REPORTED', 'DISPATCHED', 'CONTAINED', 'RESOLVED'].map(status => `<option value="${status}" ${incident.status === status ? 'selected' : ''}>${status}</option>`).join('')}
                </select>
                <button type="button" data-action="status" data-id="${incident.id}">Update</button>
            </div></td>
        </tr>`;
    }).join('');
}

async function loadIncidents() {
    try {
        renderIncidents(await request('/api/incidents'));
        listFeedback.textContent = '';
    } catch (error) {
        incidentRows.innerHTML = '<tr><td class="empty-state" colspan="6">Incident data is unavailable. Check the database connection and refresh.</td></tr>';
        listFeedback.textContent = error.message;
    }
}

reportForm.addEventListener('submit', async event => {
    event.preventDefault();
    const formData = new FormData(reportForm);
    const button = reportForm.querySelector('button[type="submit"]');
    button.disabled = true;
    formFeedback.textContent = '';

    try {
        await request('/api/incidents', {
            method: 'POST',
            body: JSON.stringify({ location: formData.get('location'), severity: formData.get('severity') })
        });
        reportForm.reset();
        document.querySelector('#severity').value = 'MEDIUM';
        formFeedback.textContent = 'Incident report submitted.';
        await loadIncidents();
    } catch (error) {
        formFeedback.textContent = error.message;
    } finally {
        button.disabled = false;
    }
});

incidentRows.addEventListener('click', async event => {
    const button = event.target.closest('button[data-action]');
    if (!button) return;

    const incidentId = button.dataset.id;
    const action = button.dataset.action;
    let path = `/api/incidents/${incidentId}/status`;
    let body;

    if (action === 'dispatch') {
        const rangerName = document.querySelector(`[data-ranger-for="${incidentId}"]`).value.trim();
        if (!rangerName) {
            listFeedback.textContent = 'Enter a ranger name before assigning.';
            return;
        }
        path = `/api/incidents/${incidentId}/dispatch`;
        body = { rangerName };
    } else {
        body = { status: document.querySelector(`[data-status-for="${incidentId}"]`).value };
    }

    button.disabled = true;
    listFeedback.textContent = '';
    try {
        await request(path, { method: 'PATCH', body: JSON.stringify(body) });
        await loadIncidents();
    } catch (error) {
        listFeedback.textContent = error.message;
        button.disabled = false;
    }
});

loadIncidents();