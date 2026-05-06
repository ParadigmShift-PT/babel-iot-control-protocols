package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output;

import pt.unl.fct.di.novasys.iot.device.i2c.GroveLedMatrix.Animation;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

/**
 * Request asking {@code I2COutputControlProtocol} to play one of the
 * built-in animations on a Grove LED matrix.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class ShowAnimationRequest extends IoTEventRequest {

    /** Default Babel request id used by this class. */
    public static final short REQUEST_ID = 8027;

    private final Animation animation;

    /**
     * @param h         the handle of the LED matrix
     * @param animation the animation to play
     */
    public ShowAnimationRequest(DeviceHandle h, Animation animation) {
        super(ShowAnimationRequest.REQUEST_ID, h);
        this.animation = animation;
    }

    /** @return the animation to play. */
    public Animation getAnimation() { return this.animation; }
}
