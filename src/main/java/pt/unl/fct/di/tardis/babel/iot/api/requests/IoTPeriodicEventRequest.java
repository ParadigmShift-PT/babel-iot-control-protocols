package pt.unl.fct.di.tardis.babel.iot.api.requests;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;

/**
 * Abstract base for input requests that ask the protocol to sample a
 * device at a fixed period.
 * <p>
 * The period is expressed in milliseconds and applies until the
 * matching device is unregistered (or until a future request cancels
 * the periodic schedule). Concrete subclasses live alongside the
 * sensor-specific input requests under
 * {@code controlprotocols.requests.input}.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public abstract class IoTPeriodicEventRequest extends IoTEventRequest {

    private final long period; // Period of measurement in milliseconds

    /**
     * @param id     the Babel request id
     * @param handle the handle of the device to sample
     * @param p      the sampling period, in milliseconds
     */
    public IoTPeriodicEventRequest(short id, DeviceHandle handle, long p) {
        super(id, handle);
        this.period = p;
    }

    /** @return the sampling period, in milliseconds. */
    public long getPeriod() { return this.period; }
}
