package pt.unl.fct.di.tardis.babel.iot.controlprotocols.replies;

import pt.unl.fct.di.novasys.iot.device.i2c.GroveGestureDetector;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceType;
import pt.unl.fct.di.tardis.babel.iot.api.replies.ErrorCode;
import pt.unl.fct.di.tardis.babel.iot.api.replies.IoTInputReply;

/**
 * Typed reply carrying the latest gesture observed by a Grove gesture
 * detector (PAJ7620 sensor).
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class GestureInputReply extends IoTInputReply {

    /**
     * Default Babel reply id used by this class. <b>ID:</b> {@value}.
     * Handler class: request/reply (shared pool — continues after the
     * protocol's requests). Owning protocol:
     * {@code I2CInputControlProtocol} (id 2100).
     */
    public static final short REPLY_ID = 2107;

    private final GroveGestureDetector.PAJ7620GestureType gesture;

    /** Failure reply with the default reply id. */
    public GestureInputReply(DeviceType type, String alias, ErrorCode e,
                             String err) {
        this(REPLY_ID, type, alias, e, err);
    }

    /** Failure reply with a caller-supplied reply id. */
    public GestureInputReply(short repID, DeviceType type, String alias,
                             ErrorCode e, String err) {
        super(repID, type, err, e, err);
        this.gesture = null;
    }

    /** Successful reply with the default reply id. */
    public GestureInputReply(DeviceHandle handle,
                             GroveGestureDetector.PAJ7620GestureType gesture) {
        this(REPLY_ID, handle, gesture);
    }

    /** Successful reply with a caller-supplied reply id. */
    public GestureInputReply(short repID, DeviceHandle handle,
                             GroveGestureDetector.PAJ7620GestureType gesture) {
        super(repID, handle);
        this.gesture = gesture;
    }

    /** @return the observed gesture, or {@code null} on failure. */
    public GroveGestureDetector.PAJ7620GestureType getGesture() {
        return this.gesture;
    }
}
