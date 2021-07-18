package com.tripmaster.rewards.repository;

import com.tripmaster.rewards.service.RewardsService;
import com.tripmaster.rewards.user.User;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class UserDatabase {
    private Logger logger = LoggerFactory.getLogger(UserDatabase.class);
    private static final Map<String, User> internalUserMap = new HashMap<>();
    private static int internalUserNumber = 10;
    private final RewardsService rewardsService = new RewardsService();

    public UserDatabase() {
    }

    public Map<String, User> getInternalUserMap() {
        return internalUserMap;
    }

    public static void setInternalUserNumber(int internalUserNumber) {
        UserDatabase.internalUserNumber = internalUserNumber;
    }

    public static int getInternalUserNumber() {
        return internalUserNumber;
    }

    public void initializeUserDatabase() {
        IntStream.range(0, internalUserNumber).forEach(i -> {
            String userName = "internalUser" + i;
            String phone = "000";
            String email = userName + "@tourGuide.com";
            User user = new User(UUID.randomUUID(), userName, phone, email);
            generateUserLocationHistory(user);
            internalUserMap.put(userName, user);
        });
        logger.debug("Created " + internalUserNumber + " internal database users.");
    }

    private void generateUserLocationHistory(User user) {
        IntStream.range(0, 3).parallel().forEach(i -> {
            user.addToVisitedLocations(new VisitedLocation(user.getUserId(),
                    new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
        });
    }

    private double generateRandomLongitude() {
        double leftLimit = -180;
        double rightLimit = 180;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    private double generateRandomLatitude() {
        double leftLimit = -85.05112878;
        double rightLimit = 85.05112878;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    private Date getRandomTime() {
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
        return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }

    public User getUser(String userName) {
        return internalUserMap.get(userName);
    }

    public List<User> getAllUsers() {
        return internalUserMap.values().parallelStream().collect(Collectors.toList());
    }

    public void addUser(User user) {
        if (!internalUserMap.containsKey(user.getUserName())) {
            internalUserMap.put(user.getUserName(), user);
        }
    }

}
