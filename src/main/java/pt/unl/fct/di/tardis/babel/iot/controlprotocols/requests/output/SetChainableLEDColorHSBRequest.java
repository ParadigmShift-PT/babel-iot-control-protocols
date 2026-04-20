package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

public class SetChainableLEDColorHSBRequest extends IoTEventRequest {

    public static final short REQUEST_ID = 8022;

    private final byte idx;
    private final float hue;
    private final float saturation;
    private final float brightness;

    public SetChainableLEDColorHSBRequest(DeviceHandle h, byte idx, float hue,
                                           float saturation, float brightness) {
        super(REQUEST_ID, h);
        this.idx = idx;
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
    }

    public byte getIdx() { return idx; }

    public float getHue() { return this.hue; }

    public float getSaturation() { return this.saturation; }

    public float getBrightness() { return this.brightness; }
}
