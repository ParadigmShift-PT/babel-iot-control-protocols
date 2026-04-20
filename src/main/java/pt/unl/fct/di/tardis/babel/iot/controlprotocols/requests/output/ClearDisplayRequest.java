package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

public class ClearDisplayRequest extends IoTEventRequest {

    public static final short REQUEST_ID = 8021;

    public ClearDisplayRequest(DeviceHandle h) {
        super(REQUEST_ID, h);      
    }
}
