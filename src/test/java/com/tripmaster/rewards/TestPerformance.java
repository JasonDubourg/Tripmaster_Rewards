package com.tripmaster.rewards;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.tripmaster.rewards.repository.UserDatabase;
import com.tripmaster.rewards.thread.ThreadUserService;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class TestPerformance {

    @Test
    public void highVolumeGetRewards() {
        ThreadUserService threadUserService = new ThreadUserService();
        StopWatch stopWatch = new StopWatch();

        UserDatabase.setInternalUserNumber(10);
        // Users should be incremented up to 100,000, and test finishes within 20 minutes
        // Note : the amount of rewards calculated depends of the number of users set and the amount of threads used.
        threadUserService.setThreadAmount(100);

        System.out.format("Number of user's rewards to calculate : %d \n",
                threadUserService.getThreadAmount() * UserDatabase.getInternalUserNumber());

        stopWatch.start();
        threadUserService.calculateUsersRewardsThreadPool();
        stopWatch.stop();

        System.out.format("highVolumeGetRewards: Time Elapsed: %d seconds.", TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
        assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }
}
