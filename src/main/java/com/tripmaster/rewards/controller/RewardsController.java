package com.tripmaster.rewards.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RewardsController {

    @GetMapping(value = "/rewards")
    public String locationService(){
        String test = "Hello Rewards Location";
        return test;
    }
}
