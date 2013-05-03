package easter.challenge;

import com.google.common.base.Stopwatch;
import com.sun.tools.javac.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PerformanceEventBasedIT {

    Logger LOGGER = LoggerFactory.getLogger(PerformanceEventBasedIT.class);

    @DataProvider
    public Object[][] testSize() {
        return new Object[][] {
            {500000},   // 1s
            {1000000},  // 2s
            {10000000}, // 35s -> degradation starts
            {20000000}, // 72s
            {40000000}, // 167s
        };
    }

    @Test(dataProvider = "testSize")
    public void increasingDataTest(int numberIntervals) {
        Stopwatch stopwatch = new Stopwatch();
        MaxUsersLoggedInEventBased maxUsersLoggedIn = new MaxUsersLoggedInEventBased();
        List<Pair<Integer,Integer>> intervals = PerformanceIT.generateIntervals(numberIntervals);
        stopwatch.start();
        List<Pair<Integer,MaxUsersLoggedInEventBased.EventType>> eventList = maxUsersLoggedIn.convertAndSortIntervals(intervals);
        List<Integer> result = maxUsersLoggedIn.maxSystemUsage(eventList);
        stopwatch.stop();
        LOGGER.info("Calculating " + numberIntervals + " intervals took: " +
                stopwatch.elapsedTime(TimeUnit.MILLISECONDS) + "ms.");
        Assert.assertEquals(result.size(), 3, "Result must contain exactly 3 elements.");
        LOGGER.info("A total of " + result.get(2) + " concurrent users where observed in this interval [" +
            result.get(0) + "," + result.get(1) +"].\n");
    }
}
