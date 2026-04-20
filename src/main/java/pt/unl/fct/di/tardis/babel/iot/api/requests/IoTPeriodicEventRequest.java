package pt.unl.fct.di.tardis.babel.iot.api.requests;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;

public abstract class IoTPeriodicEventRequest extends IoTEventRequest {

    private final long period; // Period of measurement in milliseconds

    public IoTPeriodicEventRequest(short id, DeviceHandle handle, long p) {
        super(id, handle);
        this.period = p;
    }

    public long getPeriod() { return this.period; }
}
