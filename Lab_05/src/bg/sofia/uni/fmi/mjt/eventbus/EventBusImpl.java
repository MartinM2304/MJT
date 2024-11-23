package bg.sofia.uni.fmi.mjt.eventbus;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;
import bg.sofia.uni.fmi.mjt.eventbus.exception.MissingSubscriptionException;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.Subscriber;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EventBusImpl implements EventBus {

    //private Map<?extends Event<?>,I>
    private Map<Class<? extends Event<?>>, Set<Subscriber<?>>> subscribtions = new HashMap<>();
    private Map<Class<? extends Event<?>>, Set<Event<?>>> eventLogs = new HashMap<>();

    /**
     * Subscribes the given subscriber to the given event type.
     *
     * @param eventType  the type of event to subscribe to
     * @param subscriber the subscriber to subscribe
     * @throws IllegalArgumentException if the event type is null
     * @throws IllegalArgumentException if the subscriber is null
     */
    public <T extends Event<?>> void subscribe(Class<T> eventType, Subscriber<? super T> subscriber) {
        if (eventType == null || subscriber == null) {
            throw new IllegalArgumentException("evenType or subscripber is null");
        }
        // Shano
        //subscribtions.putIfAbsent(eventType, new HashSet<>());
        if (subscribtions.containsKey(eventType)) {
            subscribtions.get(eventType).add(subscriber);
        } else {
            HashSet<Subscriber<?>> tmp = new HashSet<>();
            tmp.add(subscriber);
            subscribtions.put(eventType, tmp);
        }
    }

    /**
     * Unsubscribes the given subscriber from the given event type.
     *
     * @param eventType  the type of event to unsubscribe from
     * @param subscriber the subscriber to unsubscribe
     * @throws IllegalArgumentException     if the event type is null
     * @throws IllegalArgumentException     if the subscriber is null
     * @throws MissingSubscriptionException if the subscriber is not subscribed to the event type
     */
    public <T extends Event<?>> void unsubscribe(Class<T> eventType, Subscriber<? super T> subscriber)
            throws MissingSubscriptionException {
        if (eventType == null || subscriber == null) {
            throw new IllegalArgumentException("arg is null");
        }
        if (!subscribtions.containsKey(eventType)) {
            throw new MissingSubscriptionException("Cannot unsubscribe if event is not present");
        }
        if (!subscribtions.get(eventType).remove(subscriber)) {
            throw new MissingSubscriptionException("Subscriber is not subscribed");
        }
        if (subscribtions.get(eventType).isEmpty()) {
            subscribtions.remove(eventType);
        }
    }

    /**
     * Publishes the given event to all subscribers of the event type.
     *
     * @param event the event to publish
     * @throws IllegalArgumentException if the event is null
     */
    public <T extends Event<?>> void publish(T event) {
        if (event == null) {
            throw new IllegalArgumentException("event is null");
        }
        if (this.subscribtions.containsKey(event.getClass())) {
            for (Subscriber<?> subscriber : subscribtions.get(event.getClass())) {
                ((Subscriber<? super T>) subscriber).onEvent(event);
            }
        }
        //eventLogs.putIfAbsent((Class<? extends Event<?>>) event.getClass(), new HashSet<>());
        if (eventLogs.containsKey(event.getClass())) {
            eventLogs.get(event.getClass()).add(event);
        } else {
            System.out.println("making new set");
            HashSet<Event<?>> tmp = new HashSet<>();
            tmp.add(event);
            this.eventLogs.put((Class<? extends Event<?>>) event.getClass(), tmp);
        }
    }

    /**
     * Clears all subscribers and event logs.
     */
    public void clear() {
        subscribtions.clear();
        eventLogs.clear();
    }

    /**
     * Returns all events of the given event type that occurred between the given timestamps. If
     * {@code from} and {@code to} are equal the returned collection is empty.
     * <p> {@code from} - inclusive, {@code to} - exclusive. </p>
     *
     * @param eventType the type of event to get
     * @param from      the start timestamp (inclusive)
     * @param to        the end timestamp (exclusive)
     * @return an unmodifiable collection of events of the given event type that occurred between
     * the given timestamps
     * @throws IllegalArgumentException if the event type is null
     * @throws IllegalArgumentException if the start timestamp is null
     * @throws IllegalArgumentException if the end timestamp is null
     */
    public Collection<? extends Event<?>> getEventLogs(Class<? extends Event<?>> eventType, Instant from,
                                                       Instant to) {
        if (eventType == null || from == null || to == null) {
            throw new IllegalArgumentException("eventlogs recieved null");
        }
        if (!this.eventLogs.containsKey(eventType) || from.equals(to)) {
            return Collections.emptyList();
        }
        Set<Event<?>> events = eventLogs.get(eventType);
        List<Event<?>> result = new ArrayList<>();

        for (Event<?> event : events) {
            Instant timespamp = event.getTimestamp();
            if (timespamp.isBefore(to) && (timespamp.isAfter(from) || event.getTimestamp().equals(from))) {
                result.add(event);
            }
        }
        result.sort(Comparator.comparing(Event::getTimestamp));
        return Collections.unmodifiableCollection(result);
    }

    /**
     * Returns all subscribers for the given event type in an unmodifiable collection. If there are
     * no subscribers for the event type, the method returns an empty unmodifiable collection.
     *
     * @param eventType the type of event to get subscribers for
     * @return an unmodifiable collection of subscribers for the given event type
     * @throws IllegalArgumentException if the event type is null
     */
    public <T extends Event<?>> Collection<Subscriber<?>> getSubscribersForEvent(Class<T> eventType) {
        if (eventType == null) {
            throw new IllegalArgumentException("EventType is null");
        }
        if (!subscribtions.containsKey(eventType)) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableCollection(subscribtions.get(eventType));
    }
}
