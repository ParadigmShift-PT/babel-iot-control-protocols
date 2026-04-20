package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

public class SetChainableLEDColorRGBRequest extends IoTEventRequest {

    public static final short REQUEST_ID = 8023;

    private final byte idx;
    private final byte red;
    private final byte green;
    private final byte blue;

    public SetChainableLEDColorRGBRequest(DeviceHandle h, byte idx, byte red,
                                           byte green, byte blue) {
        super(REQUEST_ID, h);
        this.idx = idx;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public byte getIdx() { return idx; }

    public byte getRed() { return this.red; }

    public byte getGreen() { return this.green; }

    public byte getBlue() { return this.blue; }
}
