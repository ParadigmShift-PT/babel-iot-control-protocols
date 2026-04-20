package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.input;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

public class GetEncoderRotationRequest extends IoTEventRequest {

    public static final short REQUEST_ID = 8013;

    public GetEncoderRotationRequest(DeviceHandle h) {
        super(REQUEST_ID, h);
    }
}
