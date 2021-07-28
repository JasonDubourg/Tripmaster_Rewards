package com.tripmaster.rewards.controller;

import com.jsoniter.output.JsonStream;
import com.tripmaster.rewards.repository.UserDatabase;
import com.tripmaster.rewards.service.RewardsService;
import com.tripmaster.rewards.user.User;
import com.tripmaster.rewards.user.UserReward;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tripPricer.Provider;

import java.util.List;

@RestController
public class RewardsController {

    @Autowired
    RewardsService rewardsService;

    @GetMapping(value = "/rewards")
    public String locationService(){
        String test = "Hello Rewards Location";
        return test;
    }

    @GetMapping(value = "/getRewards")
    public List<UserReward> getUserRewards(@RequestParam("userName") String userName){
        return rewardsService.calculateUserRewards(userName);
    }

    @GetMapping(value = "/getTripDeals")
    public List<Provider> getTripDeals(@RequestParam("userName") String userName){
        UserDatabase userDatabase = new UserDatabase();
        userDatabase.initializeUserDatabase();
        User user = userDatabase.getUser(userName);
         return rewardsService.getTripDeals(user);
    };
}
