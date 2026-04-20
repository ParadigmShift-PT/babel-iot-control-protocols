package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.input;

import pt.unl.fct.di.novasys.iot.device.i2c.GroveGestureDetector.PAJ7620GestureType;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.Threshold;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTReactiveEventRequest;

public class GetReactiveGestureRequest
    extends IoTReactiveEventRequest<PAJ7620GestureType> {

    public static final short REQUEST_ID = 8016;

    public GetReactiveGestureRequest(DeviceHandle h,
                                     Threshold<PAJ7620GestureType> t) {
        super(REQUEST_ID, h, t);
    }
}
