package pt.unl.fct.di.tardis.babel.iot.controlprotocols.replies;

import pt.unl.fct.di.novasys.iot.device.i2c.GroveGestureDetector;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceType;
import pt.unl.fct.di.tardis.babel.iot.api.replies.ErrorCode;
import pt.unl.fct.di.tardis.babel.iot.api.replies.IoTInputReply;

public class GestureInputReply extends IoTInputReply {

    public static final short REPLY_ID = 4015;

    private final GroveGestureDetector.PAJ7620GestureType gesture;

    public GestureInputReply(DeviceType type, String alias, ErrorCode e,
                             String err) {
        this(REPLY_ID, type, alias, e, err);
    }

    public GestureInputReply(short repID, DeviceType type, String alias,
                             ErrorCode e, String err) {
        super(repID, type, err, e, err);
        this.gesture = null;
    }

    public GestureInputReply(DeviceHandle handle,
                             GroveGestureDetector.PAJ7620GestureType gesture) {
        this(REPLY_ID, handle, gesture);
    }

    public GestureInputReply(short repID, DeviceHandle handle,
                             GroveGestureDetector.PAJ7620GestureType gesture) {
        super(repID, handle);
        this.gesture = gesture;
    }

    public GroveGestureDetector.PAJ7620GestureType getGesture() {
        return this.gesture;
    }
}
