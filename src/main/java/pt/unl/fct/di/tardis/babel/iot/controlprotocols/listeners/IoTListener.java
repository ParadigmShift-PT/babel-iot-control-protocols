package pt.unl.fct.di.tardis.babel.iot.controlprotocols.listeners;

import java.util.function.Consumer;
import java.util.function.Function;
import pt.unl.fct.di.tardis.babel.iot.api.Threshold;

/**
 * Generic background poll loop for a single device, used by reactive
 * input requests.
 * <p>
 * Each tick the listener calls {@code getter} on its device, evaluates
 * the supplied {@link Threshold} against the result, and invokes
 * {@code callback} when (and only when) the threshold accepts the
 * value. Concrete subclasses ({@link EncoderListener},
 * {@link GestureListener}) bind the device class and the read method
 * up front; the parent protocol registers them with
 * {@link IoTMonitoringService}, which schedules the actual ticks.
 *
 * @param <R> the device type the listener polls.
 * @param <T> the type of value produced by reading the device.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class IoTListener<R, T> implements Runnable {
    /** The device this listener polls. */
    protected final R device;
    /** Predicate gating callback delivery. */
    protected final Threshold<T> threshold;
    private final Function<R, T> getter;
    /** Callback invoked when the threshold accepts a sampled value. */
    protected final Consumer<T> callback;

    /**
     * @param device    the device to poll
     * @param threshold predicate used to gate {@code callback}
     * @param getter    function returning the current device reading
     * @param callback  invoked with each value that passes the threshold
     */
    public IoTListener(R device, Threshold<T> threshold, Function<R, T> getter,
                       Consumer<T> callback) {
        this.device = device;
        this.threshold = threshold;
        this.getter = getter;
        this.callback = callback;
    }

    /**
     * Polls the device once and dispatches to the callback if the
     * threshold accepts the current value. Designed to be invoked from
     * a scheduled executor (see {@link IoTMonitoringService}).
     */
    @Override
    public void run() {
        T currentValue = getter.apply(device);
        if (currentValue != null && threshold.test(currentValue)) {
            callback.accept(currentValue);
        }
    }

    /** @return the device this listener polls. */
    public R getDevice() { return this.device; }

    /** @return the predicate gating callback delivery. */
    public Threshold<T> getThreshold() { return this.threshold; }
}
