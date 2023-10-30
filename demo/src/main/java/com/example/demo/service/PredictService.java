package com.example.demo.service;

import com.example.demo.repository.RaceResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PredictService {

    @Autowired
    private RaceResultRepository raceResultRepository;
}
