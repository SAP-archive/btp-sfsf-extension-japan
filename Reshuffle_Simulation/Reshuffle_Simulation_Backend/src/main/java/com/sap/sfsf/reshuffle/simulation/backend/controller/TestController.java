package com.sap.sfsf.reshuffle.simulation.backend.controller;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.sap.sfsf.reshuffle.simulation.backend.model.Candidate;
import com.sap.sfsf.reshuffle.simulation.backend.services.CandidateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
    @Autowired
    CandidateService candidateService;
    
	@RequestMapping(value = "/create", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    @Transactional
    public String createCandidates(){
        List<Candidate> list = new ArrayList<Candidate>();
        Candidate c1 = new Candidate("mnadal","50014328");
        Candidate c2 = new Candidate("eakers","50014328");
        list.add(c1);
        list.add(c2);
        candidateService.deleteAll();
        candidateService.saveAll(list);
        return "";
    }
    
}