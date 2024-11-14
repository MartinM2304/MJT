package bg.sofia.uni.fmi.mjt.eventbus.events;

import java.time.Instant;

public record EventTemp<T extends Payload<?>> (Instant timestamp, int priority, String source, T payload) implements Event<T> {

    public EventTemp(int priority, String source, T payload) {
        this(Instant.now(), priority, source, payload);
        if (source == null || source.isBlank()) {
            throw new IllegalArgumentException("Source cannot be null or empty");
        }
        if (payload == null) {
            throw new IllegalArgumentException("Payload cannot be null");
        }
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public T getPayload() {
        return payload;
    }
}
