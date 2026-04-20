package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.input;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.InputType;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

public class GetUltrasonicRangerMeasurementRequest extends IoTEventRequest {

    public static final short REQUEST_ID = 8017;
    public final InputType.UltrasonicRanger inputType;

    public GetUltrasonicRangerMeasurementRequest(
        DeviceHandle h, InputType.UltrasonicRanger inputType) {
        super(REQUEST_ID, h);
        this.inputType = inputType;
    }

    public InputType.UltrasonicRanger getInputType() { return this.inputType; }
}
