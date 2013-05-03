package easter.challenge;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.internal.collections.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class MaxUsersLoggedInTest {

    @DataProvider
    public Object[][] simpleDataProvider() {
        return new Object[][] {
            {Arrays.asList(new Pair<Integer,Integer>(7,10), new Pair<Integer, Integer>(3,20),
                new Pair<Integer, Integer>(9,9), new Pair<Integer, Integer>(1,4)),
                Arrays.asList(9,3)},
            {Arrays.asList(new Pair<Integer,Integer>(7,10), new Pair<Integer, Integer>(3,20),
                new Pair<Integer, Integer>(9,10), new Pair<Integer, Integer>(1,4)),
                Arrays.asList(9,3)},
            {Arrays.asList(new Pair<Integer,Integer>(6,10), new Pair<Integer, Integer>(1,1),
                new Pair<Integer, Integer>(6,77), new Pair<Integer, Integer>(6,7)),
                Arrays.asList(6,3)},
            {Arrays.asList(new Pair<Integer,Integer>(6,10), new Pair<Integer, Integer>(1,1),
                new Pair<Integer, Integer>(7,77), new Pair<Integer, Integer>(6,7)),
                Arrays.asList(7,3)},
            {Arrays.asList(new Pair<Integer,Integer>(1,2), new Pair<Integer, Integer>(3,4),
                new Pair<Integer, Integer>(5,6), new Pair<Integer, Integer>(7,8)),
                Arrays.asList(1,1)},
            {Arrays.asList(new Pair<Integer, Integer>(1, 7), new Pair<Integer, Integer>(2, 6),
                    new Pair<Integer, Integer>(3, 5), new Pair<Integer, Integer>(4, 4)),
                Arrays.asList(4,4)},
            {Arrays.asList(new Pair<Integer, Integer>(1, 7)),
                Arrays.asList(1,1)},
            //interval borders with negative numbers should be allowed
            {Arrays.asList(new Pair<Integer, Integer>(-100, -10), new Pair<Integer, Integer>(-11, -1),
                new Pair<Integer, Integer>(-12, 0), new Pair<Integer, Integer>(0, 10),
                new Pair<Integer, Integer>(-1, 12)),
                Arrays.asList(-11,3)},
            {Arrays.asList(new Pair<Integer, Integer>(1, 1), new Pair<Integer, Integer>(1, 1),
                new Pair<Integer, Integer>(1, 1)),
                Arrays.asList(1,3)},
        };
    }

    @Test(dataProvider = "simpleDataProvider")
    public void testExample(List<Pair<Integer,Integer>> intervals, List<Integer> expected){
        MaxUsersLoggedIn maxUsersLoggedIn = new MaxUsersLoggedIn();
        List<Integer> result = maxUsersLoggedIn.maxSystemUsage(maxUsersLoggedIn.sortEvents(
            maxUsersLoggedIn.sortEvents(intervals)).listIterator());
        Assert.assertEquals(result.size(), expected.size(), "Result must contain exactly 3 elements.");
        Assert.assertEquals(result, expected, "Result differs from expectation.");
    }


    @DataProvider
    public Object[][] negativeDataProvider() {
        return new Object[][] {
            {null},
            {new ArrayList<Pair<Integer,Integer>>().listIterator()},
            //reverse intervals are illegal
            {Arrays.asList(new Pair<Integer,Integer>(10,7), new Pair<Integer, Integer>(20,3)).listIterator()},
        };
    }

    @Test(dataProvider = "negativeDataProvider", expectedExceptions = AssertionError.class)
    public void negativeTest(ListIterator<Pair<Integer,Integer>> intervals) {
        MaxUsersLoggedIn maxUsersLoggedIn = new MaxUsersLoggedIn();
        maxUsersLoggedIn.maxSystemUsage(intervals);
    }

}
