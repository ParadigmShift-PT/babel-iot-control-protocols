package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

public class SetDisplayColorRequest extends IoTEventRequest {

    public static final short REQUEST_ID = 8024;

    private final int red;
    private final int green;
    private final int blue;

    public SetDisplayColorRequest(DeviceHandle h, int red, int green,
                                  int blue) {
        super(REQUEST_ID, h);
        this.red = Math.max(0, Math.min(255, red));
        this.green = Math.max(0, Math.min(255, green));
        this.blue = Math.max(0, Math.min(255, blue));
    }

    public int getRed() { return this.red; }

    public int getGreen() { return this.green; }

    public int getBlue() { return this.blue; }
}
