package bg.sofia.uni.fmi.mjt.eventbus.events;

import java.util.Collection;

public class PayloadInteger<T> implements Payload<T> {

    private final T payLoad;

    public PayloadInteger(T payLoad) {
        this.payLoad = payLoad;
    }

    /**
     * @return the size of the payload
     */
    public int getSize() {

        if (payLoad instanceof Collection) {
            return ((Collection<?>) payLoad).size();
        } else if (payLoad instanceof String) {
            return ((String) payLoad).length();
        } else {
            return payLoad != null ? 1 : 0;
        }

    }

    /**
     * @return the payload
     */
    public T getPayload() {
        return payLoad;
    }
}
