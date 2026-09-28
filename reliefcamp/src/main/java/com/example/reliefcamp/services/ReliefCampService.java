package com.example.reliefcamp.services;

import java.util.List;

import com.example.reliefcamp.models.ReliefCampModel;

public interface ReliefCampService {
    public ReliefCampModel createReliefCampModel(ReliefCampModel camp);
    public List<ReliefCampModel> getAllCamps();
    public ReliefCampModel updateReliefCampModel(ReliefCampModel camp);

}
