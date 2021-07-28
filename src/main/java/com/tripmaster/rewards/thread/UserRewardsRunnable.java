package com.tripmaster.rewards.thread;

import com.tripmaster.rewards.repository.UserDatabase;
import com.tripmaster.rewards.service.RewardsService;
import com.tripmaster.rewards.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserRewardsRunnable implements  Runnable {
    private Logger logger = LoggerFactory.getLogger(ThreadUserService.class);
    private RewardsService rewardsService = new RewardsService();
    UserDatabase userDatabase = new UserDatabase();

    @Override
    public void run() {
        userDatabase.initializeUserDatabase();
        List<User> users = userDatabase.getAllUsers();
        logger.debug("Begin calculating the rewards; Calculating {} rewards...", users.size());
        for (User user : users) {
            rewardsService.calculateRewards(user);
        }
    }
}
