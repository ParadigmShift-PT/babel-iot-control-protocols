package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.input;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

public class GetGestureRequest extends IoTEventRequest {

    public static final short REQUEST_ID = 8014;

    public GetGestureRequest(DeviceHandle h) {
        super(REQUEST_ID, h);
    }
}
