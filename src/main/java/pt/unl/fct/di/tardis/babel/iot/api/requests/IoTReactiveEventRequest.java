package pt.unl.fct.di.tardis.babel.iot.api.requests;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.Threshold;

/**
 * Abstract base for input requests that ask the protocol to monitor a
 * device and emit a notification only when a sampled value satisfies
 * a {@link Threshold} predicate.
 * <p>
 * Concrete subclasses live alongside the sensor-specific input
 * requests under {@code controlprotocols.requests.input}.
 *
 * @param <T> the type of value produced by the targeted sensor.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public abstract class IoTReactiveEventRequest<T> extends IoTEventRequest {

    private final Threshold<T> threshold;

    /**
     * @param id        the Babel request id
     * @param handle    the handle of the device to monitor
     * @param threshold the predicate that gates notification delivery
     */
    public IoTReactiveEventRequest(short id, DeviceHandle handle,
                                   Threshold<T> threshold) {
        super(id, handle);
        this.threshold = threshold;
    }

    /** @return the predicate that gates notification delivery. */
    public Threshold<T> getThreshold() { return this.threshold; }
}
