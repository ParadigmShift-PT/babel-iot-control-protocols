package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output;

import pt.unl.fct.di.novasys.iot.device.i2c.GroveLedMatrix.Emoji;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

/**
 * Request asking {@code I2COutputControlProtocol} to render one of the
 * built-in emoji on a Grove LED matrix.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class ShowEmojiRequest extends IoTEventRequest {

    /**
     * Default Babel request id used by this class. <b>ID:</b> {@value}.
     * Handler class: request/reply (shared pool). Owning protocol:
     * {@code I2COutputControlProtocol} (id 2000).
     */
    public static final short REQUEST_ID = 2005;

    private final Emoji emoji;

    /**
     * @param h     the handle of the LED matrix
     * @param emoji the emoji to render
     */
    public ShowEmojiRequest(DeviceHandle h, Emoji emoji) {
        super(ShowEmojiRequest.REQUEST_ID, h);
        this.emoji = emoji;
    }

    /** @return the emoji to render. */
    public Emoji getEmoji() { return this.emoji; }
}
