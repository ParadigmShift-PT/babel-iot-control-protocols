package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.input;

import pt.unl.fct.di.novasys.iot.device.digital.GroveEncoder;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.Threshold;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTReactiveEventRequest;

public class GetReactiveEncoderRequest
    extends IoTReactiveEventRequest<GroveEncoder.Rotation> {

    public static final short REQUEST_ID = 8015;

    public GetReactiveEncoderRequest(DeviceHandle h,
                                     Threshold<GroveEncoder.Rotation> t) {
        super(REQUEST_ID, h, t);
    }
}
