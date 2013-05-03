package easter.challenge;

import com.google.common.collect.Lists;
import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;
import easter.profiler.ObjectSizeProfiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.internal.collections.Pair;

import java.lang.instrument.Instrumentation;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

/**
 * Approach: Iterate through login events and build a set of logout events. On every login check if logouts have
 * happened between the last login and this login.
 * This approach avoids building a sorted list of events (login/logout) with length 2n.
 * It also stops processing once all login events have been processed as the maximum number of logins has happened at
 * that point. However, the approach only returns the start of the interval with max users and not the end which
 * satisfies the task but might be inferior in reality.
 */
public class MaxUsersLoggedIn {

    static final Logger LOGGER = LoggerFactory.getLogger(MaxUsersLoggedIn.class);
    private static Instrumentation instrumentation;

    /**
     * More memory efficient version. Dose not require list of size 2n.
     * @param intervals List of intervals represented as Pairs of login and logout time as Integer
     * @return List of integers containing the start time of the max system load and the number of users logged in
     * at that time.
     */
    public List<Integer> maxSystemUsage(ListIterator<Pair<Integer,Integer>> intervals) {
        Assert.assertTrue(intervals != null && intervals.hasNext(), "Eventlist is null or empty.");
        int maxUsers = 0;
        int currentSessions = 0;
        List<Integer> maxLoadStart = Lists.newArrayList();
        Pair<Integer,Integer> currentInterval;
        int login, logout;
        SortedMultiset<Integer> logouts = TreeMultiset.create();
        int count = 0;
        while(intervals.hasNext()) {
            currentInterval = intervals.next();
            login = currentInterval.first();
            logout = currentInterval.second();
            Assert.assertTrue(login <= logout, "Logout time is before login time.");
            //check if sessions closed in between logins
            while(logouts.size() > 0 && logouts.firstEntry().getElement() < login) {
                currentSessions--;
                logouts.remove(logouts.firstEntry().getElement(), 1);
            }
            logouts.add(logout); //OOM happens here @ >600k entries
            if(count%1000000 == 999999) {
                if(ObjectSizeProfiler.getObjectSize(logouts) > 0) {
                    LOGGER.debug("SortedMultiset size: " + logouts.size() + "\t size in heap: " +
                        ObjectSizeProfiler.getObjectSize(logouts));
                } else {
                    LOGGER.debug("SortedMultiset size: " + logouts.size());
                }
            }

            currentSessions++;
            if(currentSessions > maxUsers) {
                maxUsers = currentSessions;
                maxLoadStart.clear();
                maxLoadStart.add(login);
            }
            count++;
        }
        maxLoadStart.add(maxUsers);
        return maxLoadStart;
    }

    /**
     * Sort event list by time (Integer). If multiple events collide EventType.IN must precede EventType.OUT.
     * @param events
     * @return Sorted list of event time and type pairs.
     */
    public List<Pair<Integer,Integer>> sortEvents(List<Pair<Integer,Integer>> events) {
        Collections.sort(events, new Comparator<Pair<Integer, Integer>>() {
            @Override
            public int compare(Pair<Integer, Integer> event1, Pair<Integer, Integer> event2) {
                int fstCompare = event1.first().compareTo(event2.first());
                if (fstCompare == 0) {
                    return event1.second().compareTo(event2.second());
                } else {
                    return fstCompare;
                }
            }
        });
        return events;
    }
}
