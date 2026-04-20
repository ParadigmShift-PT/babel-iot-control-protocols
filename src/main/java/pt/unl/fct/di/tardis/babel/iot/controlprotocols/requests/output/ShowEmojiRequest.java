package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output;

import pt.unl.fct.di.novasys.iot.device.i2c.GroveLedMatrix.Emoji;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

public class ShowEmojiRequest extends IoTEventRequest {

    public static final short REQUEST_ID = 8029;

    private final Emoji emoji;

    public ShowEmojiRequest(DeviceHandle h, Emoji emoji) {
        super(ShowEmojiRequest.REQUEST_ID, h);
        this.emoji = emoji;
    }

    public Emoji getEmoji() { return this.emoji; }
}
