package pt.unl.fct.di.tardis.babel.iot.api.notifications;

import pt.unl.fct.di.novasys.babel.generic.ProtoNotification;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;

/**
 * Notification carrying a single sensor reading from a registered IoT
 * device.
 * <p>
 * Emitted by an IoT control protocol whenever a periodic read fires or
 * a reactive threshold is crossed. The notification is parameterised
 * by the value type produced by the underlying sensor (for example
 * {@code Integer} for an ultrasonic ranger).
 *
 * @param <T> the type of value carried by the notification.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class IoTInputNotification<T> extends ProtoNotification implements Cloneable {

    private final DeviceHandle handle;
    private T value;
    private long timestamp;

    /**
     * @param id     the Babel notification id used to dispatch this event
     * @param handle the device that produced the reading
     * @param value  the sampled value
     */
    public IoTInputNotification(short id, DeviceHandle handle, T value) {
        super(id);
        this.handle = handle;
        this.value = value;
        this.timestamp = System.currentTimeMillis();
    }

    /** @return the handle of the device that produced this reading. */
    public DeviceHandle getDeviceHandle() { return this.handle; }

    /**
     * Returns a clone of this notification with its timestamp refreshed
     * to {@link System#currentTimeMillis()}.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        IoTInputNotification<?> copy = (IoTInputNotification<?>)super.clone();
        copy.timestamp = System.currentTimeMillis();
        return copy;
    }

    /** @return the sampled value carried by this notification. */
    public T getValue() { return this.value; }

    /**
     * @return the wall-clock time, in milliseconds since the epoch, at
     *         which this notification (or its current clone) was created.
     */
    public long getTimestamp() { return this.timestamp; }

    /**
     * Produces a fresh notification for the same device with a new
     * value and a refreshed timestamp.
     *
     * @param value the value to carry in the new notification.
     * @return a clone of this notification with {@code value} replaced.
     * @throws CloneNotSupportedException if cloning is not permitted by
     *         the underlying Babel runtime.
     */
    public IoTInputNotification<T> generateNewInput(T value)
        throws CloneNotSupportedException {
        @SuppressWarnings("unchecked")
        IoTInputNotification<T> copy = (IoTInputNotification<T>)this.clone();
        copy.value = value;
        return copy;
    }
}
