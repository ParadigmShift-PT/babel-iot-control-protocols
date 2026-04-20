package pt.unl.fct.di.tardis.babel.iot.api.requests;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.Threshold;

public abstract class IoTReactiveEventRequest<T> extends IoTEventRequest {

    private final Threshold<T> threshold;

    public IoTReactiveEventRequest(short id, DeviceHandle handle,
                                   Threshold<T> threshold) {
        super(id, handle);
        this.threshold = threshold;
    }

    public Threshold<T> getThreshold() { return this.threshold; }
}
