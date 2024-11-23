package bg.sofia.uni.fmi.mjt.eventbus.comparators;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;

import java.util.Comparator;

public class EventPriorityComparator<T extends Event<?>> implements Comparator<T> {

    @Override
    public int compare(T o1, T o2) {
        int priorityCompare = Integer.compare(o1.getPriority(), o2.getPriority());
        if (priorityCompare == 0) {
            return o1.getTimestamp().compareTo(o2.getTimestamp());
        }
        return priorityCompare;
    }
}
