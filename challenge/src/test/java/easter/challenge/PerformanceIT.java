package easter.challenge;

import com.google.common.base.Stopwatch;
import com.sun.tools.javac.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class PerformanceIT {

    static final Logger LOGGER = LoggerFactory.getLogger(PerformanceIT.class);

    @DataProvider
    public Object[][] testSize() {
        return new Object[][] {
            {1000000},
            {5000000},  //40s
            {10000000}, //80s
            {20000000}, //175s -> degradation start
//            {40000000}, //520s
//            {60000000}, //1200s = 20min
//            {70000000}, //16000s = 4.5h -> steep degradation
//            {80000000},
//            {90000000},
//            {100000000},
        };
    }

    @Test(dataProvider = "testSize", enabled = true)
    public void increasingDataTest(int numberIntervals) {
        Stopwatch stopwatch = new Stopwatch();
        MaxUsersLoggedIn maxUsersLoggedIn = new MaxUsersLoggedIn();
        List<Pair<Integer,Integer>> intervals = generateIntervals(numberIntervals);
        stopwatch.start();
        List<Integer> result = maxUsersLoggedIn.maxSystemUsage(maxUsersLoggedIn.sortEvents(intervals).listIterator());
        stopwatch.stop();
        LOGGER.info("Calculating " + numberIntervals + " intervals took: " +
                stopwatch.elapsedTime(TimeUnit.MILLISECONDS) + "ms.");
        Assert.assertEquals(result.size(), 2, "Result must contain exactly 2 elements.");
        LOGGER.info("A total of " + result.get(1) + " concurrent users where observed starting at " + result.get(0) + "\n");
    }

    /**
     * Generate intervals using java.util.Random. The distribution curve should simulate equivalent load patterns
     * independent of number of intervals created which suffices for these basic tests. It represents an increasing
     * customer base with the same access pattern.
     * @param amount Number of intervals to create.
     */
    public static List<Pair<Integer,Integer>> generateIntervals(int amount) {
        Pair<Integer,Integer>[] intervals = new Pair[amount];
        int in, out;
        Random random = new Random(System.currentTimeMillis());
        for(int i = 0; i < amount; i++) {
            in = random.nextInt();
            out = random.nextInt();
            if(in < out) {
                intervals[i] = new Pair<Integer, Integer>(in, out);
            } else {
                intervals[i] = new Pair<Integer, Integer>(out, in);
            }
        }
        return Arrays.asList(intervals);
    }
}
