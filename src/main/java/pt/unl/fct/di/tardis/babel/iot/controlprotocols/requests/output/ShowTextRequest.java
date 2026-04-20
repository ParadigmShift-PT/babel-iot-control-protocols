package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

public class ShowTextRequest extends IoTEventRequest {

    public static final short REQUEST_ID = 8030;

    private final String text;

    public ShowTextRequest(DeviceHandle h, String text) {
        super(ShowTextRequest.REQUEST_ID, h);
        this.text = text;
    }

    public String getText() { return this.text; }
}
