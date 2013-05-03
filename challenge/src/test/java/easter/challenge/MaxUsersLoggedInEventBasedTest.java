package easter.challenge;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.internal.collections.Pair;

import java.util.Arrays;
import java.util.List;

public class MaxUsersLoggedInEventBasedTest {

    @DataProvider
    public Object[][] simpleDataProvider() {
        return new Object[][] {
            {Arrays.asList(new Pair<Integer, Integer>(7, 10), new Pair<Integer, Integer>(3, 20),
                    new Pair<Integer, Integer>(9, 9), new Pair<Integer, Integer>(1, 4)),
                Arrays.asList(9,9,3)},
            {Arrays.asList(new Pair<Integer,Integer>(7,10), new Pair<Integer, Integer>(3,20),
                new Pair<Integer, Integer>(9,10), new Pair<Integer, Integer>(1,4)),
                Arrays.asList(9,10,3)},
            {Arrays.asList(new Pair<Integer,Integer>(6,10), new Pair<Integer, Integer>(1,1),
                new Pair<Integer, Integer>(6,77), new Pair<Integer, Integer>(6,7)),
                Arrays.asList(6,7,3)},
            {Arrays.asList(new Pair<Integer,Integer>(1,2), new Pair<Integer, Integer>(3,4),
                new Pair<Integer, Integer>(5,6), new Pair<Integer, Integer>(7,8)),
                Arrays.asList(1,2,1)},
            {Arrays.asList(new Pair<Integer, Integer>(1, 7), new Pair<Integer, Integer>(2, 6),
                    new Pair<Integer, Integer>(3, 5), new Pair<Integer, Integer>(4, 4)),
                Arrays.asList(4,4,4)},
            {Arrays.asList(new Pair<Integer, Integer>(1, 7)),
                Arrays.asList(1,7,1)},
            //interval borders with negative numbers should be allowed
            {Arrays.asList(new Pair<Integer, Integer>(-100, -10), new Pair<Integer, Integer>(-11, -1),
                new Pair<Integer, Integer>(-12, 0), new Pair<Integer, Integer>(0, 10),
                new Pair<Integer, Integer>(-1, 12)),
                Arrays.asList(-11,-10,3)},
            {Arrays.asList(new Pair<Integer, Integer>(1, 1), new Pair<Integer, Integer>(1, 1),
                new Pair<Integer, Integer>(1, 1)),
                Arrays.asList(1,1,3)},
        };
    }

    @Test(dataProvider = "simpleDataProvider")
    public void testExample(List<Pair<Integer,Integer>> intervals, List<Integer> expected){
        MaxUsersLoggedInEventBased maxUsersLoggedIn = new MaxUsersLoggedInEventBased();
        List<Pair<Integer,MaxUsersLoggedInEventBased.EventType>> eventList =
                maxUsersLoggedIn.convertAndSortIntervals(intervals);
        List<Integer> result = maxUsersLoggedIn.maxSystemUsage(maxUsersLoggedIn.sortEvents(
                        maxUsersLoggedIn.sortEvents(eventList)));
        Assert.assertEquals(result.size(), expected.size(), "Result must contain exactly 3 elements.");
        Assert.assertEquals(result, expected, "Result differs from expectation.");
    }


    @DataProvider
    public Object[][] negativeDataProvider() {
        return new Object[][] {
            {Arrays.asList()},
            {null},
            //reverse intervals are illegal
            {Arrays.asList(new Pair<Integer,Integer>(10,7), new Pair<Integer, Integer>(20,3))},
        };
    }

    @Test(dataProvider = "negativeDataProvider", expectedExceptions = AssertionError.class)
    public void negativeTest(List<Pair<Integer,Integer>> intervals) {
        MaxUsersLoggedInEventBased maxUsersLoggedIn = new MaxUsersLoggedInEventBased();
        List<Pair<Integer,MaxUsersLoggedInEventBased.EventType>> eventList =
                maxUsersLoggedIn.convertAndSortIntervals(intervals);
        maxUsersLoggedIn.maxSystemUsage(eventList);
    }


    @Test
    public void typeCompareTest() {
        Assert.assertEquals(MaxUsersLoggedInEventBased.EventType.IN.compareTo(MaxUsersLoggedInEventBased.EventType.OUT), -1);
        Assert.assertEquals(MaxUsersLoggedInEventBased.EventType.OUT.compareTo(MaxUsersLoggedInEventBased.EventType.IN), 1);
        Assert.assertEquals(MaxUsersLoggedInEventBased.EventType.IN.compareTo(MaxUsersLoggedInEventBased.EventType.IN), 0);
    }

}
