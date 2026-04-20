package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output;

import pt.unl.fct.di.novasys.iot.device.i2c.GroveLedMatrix.Animation;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

public class ShowAnimationRequest extends IoTEventRequest {

    public static final short REQUEST_ID = 8027;

    private final Animation animation;

    public ShowAnimationRequest(DeviceHandle h, Animation animation) {
        super(ShowAnimationRequest.REQUEST_ID, h);
        this.animation = animation;
    }

    public Animation getAnimation() { return this.animation; }
}
