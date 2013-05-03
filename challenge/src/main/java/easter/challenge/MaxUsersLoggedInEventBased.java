package easter.challenge;

import com.google.common.collect.Lists;
import com.sun.tools.javac.util.Pair;
import org.testng.Assert;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Approach: Build an event list splitting every interval into a login and logout event. Then iterate through the list
 * of 2n elements and store in which interval the maximum number of users were logged in.
 *
 * The approach has to traverse the list of events and thus has a complexity of 2n (plus build the list which does not
 * increase complexity above linear). It does however return the full interval in which the maximum number of users
 * were logged in.
 */
public class MaxUsersLoggedInEventBased {

    public enum EventType {
        IN, OUT
    }

    /**
     * Calculates the max system usage and time interval given a sorted list of login and logout events
     * @param usage List of events represented as Pairs of time stamp (Integer) and type of event (EventType).
     * @return list of 3 Integers containing start and stop of max system load and number of users at max system load
     *  [maxLoad start, maxLoad stop, max users logged in]
     */
    public List<Integer> maxSystemUsage(List<Pair<Integer,EventType>> usage) {
        Assert.assertTrue(usage != null && usage.size() > 0, "Eventlist is null or empty.");
        int maxUsers = 0;
        int currentSessions = 0;
        List<Integer> maxLoadInterval = Lists.newArrayList();
        for(Pair<Integer,EventType> event:usage) {
            switch (event.snd) {
                case IN :
                    currentSessions++;
                    if(currentSessions > maxUsers) {
                        maxUsers = currentSessions;
                        maxLoadInterval.clear();
                        maxLoadInterval.add(event.fst);
                    }
                    break;
                case OUT:
                    if(currentSessions == maxUsers) {
                        if(maxLoadInterval.size() < 2) {
                            maxLoadInterval.add(event.fst);
                        }
                    }
                    currentSessions--;
                    break;
            }
        }
        maxLoadInterval.add(maxUsers);
        return maxLoadInterval;
    }

    /**
     * Converts intervals into a sorted list of login and logout events.
     * @param intervals
     * @return sorted list of events (time/type pairs)
     */
    public List<Pair<Integer,EventType>> convertAndSortIntervals(List<Pair<Integer, Integer>> intervals) {
        Assert.assertTrue(intervals != null && intervals.size() > 0, "Interval list is null or empty.");
        List<Pair<Integer,EventType>> result = Lists.newArrayList();
        for(Pair<Integer,Integer> interval:intervals) {
            Assert.assertTrue(interval.fst <= interval.snd, "Logout time is before login time.");
            result.add(new Pair<Integer, EventType>(interval.fst, EventType.IN));
            result.add(new Pair<Integer, EventType>(interval.snd, EventType.OUT));
        }
        return sortEvents(result);
    }

    /**
     * Sort event list by time (Integer). If multiple events collide EventType.IN must precede EventType.OUT.
     * @param events
     * @return Sorted list of event time and type pairs.
     */
    public List<Pair<Integer,EventType>> sortEvents(List<Pair<Integer,EventType>> events) {
        Collections.sort(events, new Comparator<Pair<Integer, EventType>>() {
            @Override
            public int compare(Pair<Integer, EventType> event1, Pair<Integer,
                    EventType> event2) {
                int fstCompare = event1.fst.compareTo(event2.fst);
                if (fstCompare == 0) {
                    return event1.snd.compareTo(event2.snd);
                } else {
                    return fstCompare;
                }
            }
        });
        return events;
    }
}
