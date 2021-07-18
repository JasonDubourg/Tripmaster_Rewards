package com.tripmaster.rewards.service;

import com.tripmaster.rewards.repository.UserDatabase;
import com.tripmaster.rewards.user.User;
import com.tripmaster.rewards.user.UserReward;
import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.util.ArrayList;
import java.util.List;

@Service
public class RewardsService {
    // proximity in miles
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
    private int proximityBuffer = 4500;
    private int attractionProximityRange = 200;
    private final TripPricer tripPricer = new TripPricer();
    private final GpsUtil gpsUtil = new GpsUtil();
    private final RewardCentral rewardsCentral = new RewardCentral();
    private Logger logger = LoggerFactory.getLogger(RewardsService.class);
    private static final String tripPricerApiKey = "test-server-api-key";

    @Autowired
    UserDatabase userDatabase;

    public List<UserReward> calculateUserRewards(String userName){
        userDatabase.initializeUserDatabase();
        User user = userDatabase.getUser(userName);
        return this.calculateRewards(user);
    }

    public List<UserReward> calculateRewards(User user) {
        List<VisitedLocation> userLocations = user.getVisitedLocations();
        List<Attraction> attractions = gpsUtil.getAttractions();
        for (VisitedLocation visitedLocation : userLocations) {
            for (Attraction attraction : attractions) {
                if (user.getUserRewards().stream()
                        .filter(r -> r.getAttraction().attractionName.equals(attraction.attractionName)).count() == 0 && nearAttraction(visitedLocation, attraction)) {
                        user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
                    }
                }
            }
        return user.getUserRewards();
        }

    public List<Provider> getTripDeals(String userName){
        userDatabase.initializeUserDatabase();
        User user = userDatabase.getUser(userName);
        int cumulatativeRewardPoints = this.calculateRewards(user).stream().mapToInt(i -> i.getRewardPoints()).sum();
        List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, user.getUserId(),
                user.getUserPreferences().getNumberOfAdults(), user.getUserPreferences().getNumberOfChildren(),
                user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
        user.setTripDeals(providers);
        return providers;
    }

    private int getRewardPoints(Attraction attraction, User user) {
        return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
    }

    private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
        boolean res = false;
        double distance = getDistance(attraction, visitedLocation.location);
         if(distance < proximityBuffer){
             res = true;
         }
         return res;
    }

    public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math
                .acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        return STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
    }
}
