package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.input;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.InputType;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

public class GetBarometerDataRequest extends IoTEventRequest {

    public static final short REQUEST_ID = 8012;
    public final InputType.Barometer inputType;

    public GetBarometerDataRequest(DeviceHandle h,
                                       InputType.Barometer inputType) {
        super(REQUEST_ID, h);
        this.inputType = inputType;
    }

    public InputType.Barometer getInputType() { return this.inputType; }
}
