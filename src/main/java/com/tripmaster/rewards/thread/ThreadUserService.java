package com.tripmaster.rewards.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadUserService {
    private Logger logger = LoggerFactory.getLogger(ThreadUserService.class);
    private UserRewardsRunnable userRewardsRunnable = new UserRewardsRunnable();
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private int threadAmount = 500;

    public void calculateUsersRewardsThreadPool() {
        logger.debug("Submitting the rewards calculation to the different ThreadPools ...");
        for (int t = 1; t < threadAmount; t++) {
            executorService.submit(userRewardsRunnable);
        }

        try {
            executorService.submit(userRewardsRunnable).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.debug("The get() method of the Future class have been interrupted or the result is not available.");
            Thread.currentThread().interrupt();
        }

        stopExecutorService(executorService);
        logger.debug("The users have their rewards been successfully calculated.");
    }

    public void stopExecutorService(ExecutorService executorService) {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                logger.debug("The await termination time is overdue.");
                executorService.shutdownNow();
            }
        } catch (InterruptedException interruptedException) {
            logger.debug("The thread have been interrupted during the await termination time.");
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public int getThreadAmount() {
        return threadAmount;
    }

    public void setThreadAmount(int threadAmount) {
        this.threadAmount = threadAmount;
    }
}
