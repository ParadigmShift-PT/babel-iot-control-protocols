package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

public class DisplayBarRequest extends IoTEventRequest {

    public static final short REQUEST_ID = 8031;

    private final int level;

    public DisplayBarRequest(DeviceHandle h, int level) {
        super(DisplayBarRequest.REQUEST_ID, h);

        this.level = clamp(level, 0, 32);
    }

    public int getLevel() { return this.level; }

    // TODO this should go elsewhere but just for testing
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
