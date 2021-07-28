package com.tripmaster.rewards;

import com.tripmaster.rewards.repository.UserDatabase;
import com.tripmaster.rewards.service.RewardsService;
import com.tripmaster.rewards.user.User;
import com.tripmaster.rewards.user.UserReward;
import com.tripmaster.rewards.util.LocalizationUtil;
import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.Test;
import tripPricer.Provider;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class RewardsServiceTest {

    @Test
    public void getTripDeals(){
        LocalizationUtil localizationUtil = new LocalizationUtil();
        Locale.setDefault(localizationUtil.getUS_LOCALE());

        GpsUtil gpsUtil = new GpsUtil();
        RewardsService rewardsService = new RewardsService();

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        VisitedLocation visitedLocation1 = gpsUtil.getUserLocation(user.getUserId());
        VisitedLocation visitedLocation2 = gpsUtil.getUserLocation(user.getUserId());
        VisitedLocation visitedLocation3 = gpsUtil.getUserLocation(user.getUserId());
        user.addToVisitedLocations(visitedLocation1);
        user.addToVisitedLocations(visitedLocation2);
        user.addToVisitedLocations(visitedLocation3);

        List<Provider> providers = rewardsService.getTripDeals(user);

        assertTrue(providers.size() > 1);
    }

    @Test
    public void userGetRewards() throws ExecutionException, InterruptedException {
        LocalizationUtil localizationUtil = new LocalizationUtil();
        Locale.setDefault(localizationUtil.getUS_LOCALE());

        GpsUtil gpsUtil = new GpsUtil();
        RewardsService rewardsService = new RewardsService();
        UserDatabase.setInternalUserNumber(0);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        Attraction attraction = gpsUtil.getAttractions().get(0);
        user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
        gpsUtil.getUserLocation(user.getUserId());
        rewardsService.calculateRewards(user);
        List<UserReward> userRewards = user.getUserRewards();
        assertEquals(1, userRewards.size());
    }

    @Test
    public void nearAttraction() {
        GpsUtil gpsUtil= new GpsUtil();
        RewardsService rewardsService = new RewardsService();
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        Attraction attractionVisited = gpsUtil.getAttractions().get(0);
        user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attractionVisited, new Date()));
        VisitedLocation visitedLocation = user.getLastVisitedLocation();
        Attraction attraction = gpsUtil.getAttractions().get(0);
        assertTrue(rewardsService.nearAttraction(visitedLocation, attraction));
    }

    @Test
    public void getRewardPoints() {
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        RewardsService rewardsService = new RewardsService();
        GpsUtil gpsUtil= new GpsUtil();
        Attraction attraction = gpsUtil.getAttractions().get(0);
        Integer pointsRewarded = rewardsService.getRewardPoints(attraction, user);
        assertSame(pointsRewarded.getClass(), Integer.class); // Because the points rewarded are random
        assertTrue(pointsRewarded > 0);
    }

    @Test
    public void getDistance() {
        RewardsService rewardsService = new RewardsService();
        Location loc1 = new Location(0.0, 0.0);
        Location loc2 = new Location(1.0, 1.0);
        double distance = rewardsService.getDistance(loc1, loc2);
        assertEquals(distance, 97.64439545235415, 0.5);
    }

}
