package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

public class ShowDisplayRequest extends IoTEventRequest {

    public static final short REQUEST_ID = 8028;

    private final byte[] displayMap;

    public ShowDisplayRequest(DeviceHandle h, byte[] displayMap) {
        super(ShowDisplayRequest.REQUEST_ID, h);
        this.displayMap = displayMap;
    }

    public byte[] getDisplay() { return this.displayMap; }
}
