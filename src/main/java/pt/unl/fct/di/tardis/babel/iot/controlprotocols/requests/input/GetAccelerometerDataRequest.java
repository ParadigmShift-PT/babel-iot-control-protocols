package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.input;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.InputType;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

public class GetAccelerometerDataRequest extends IoTEventRequest {

    public static final short REQUEST_ID = 8011;
    public final InputType.Accelerometer inputType;

    public GetAccelerometerDataRequest(DeviceHandle h,
                                       InputType.Accelerometer inputType) {
        super(REQUEST_ID, h);
        this.inputType = inputType;
    }

    public InputType.Accelerometer getInputType() { return this.inputType; }
}
