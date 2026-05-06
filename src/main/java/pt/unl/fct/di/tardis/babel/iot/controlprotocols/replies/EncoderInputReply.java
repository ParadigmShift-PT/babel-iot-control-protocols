package pt.unl.fct.di.tardis.babel.iot.controlprotocols.replies;

import pt.unl.fct.di.novasys.iot.device.digital.GroveEncoder;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceType;
import pt.unl.fct.di.tardis.babel.iot.api.replies.ErrorCode;
import pt.unl.fct.di.tardis.babel.iot.api.replies.IoTInputReply;

/**
 * Typed reply carrying the latest rotation observed on a Grove encoder.
 * <p>
 * Failure replies default the rotation to
 * {@link GroveEncoder.Rotation#NONE}; check {@link #isSuccessful()}
 * before consuming the value.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class EncoderInputReply extends IoTInputReply {

    /** Default Babel reply id used by this class. */
    public static final short REPLY_ID = 4014;

    private final GroveEncoder.Rotation rotation;

    /** Failure reply with the default reply id. */
    public EncoderInputReply(DeviceType type, String alias, ErrorCode e,
                             String err) {
        this(REPLY_ID, type, alias, e, err);
    }

    /** Failure reply with a caller-supplied reply id. */
    public EncoderInputReply(short repID, DeviceType type, String alias,
                             ErrorCode e, String err) {
        super(repID, type, err, e, err);
        this.rotation = GroveEncoder.Rotation.NONE;
    }

    /** Successful reply with the default reply id. */
    public EncoderInputReply(DeviceHandle handle,
                             GroveEncoder.Rotation rotation) {
        this(REPLY_ID, handle, rotation);
    }

    /** Successful reply with a caller-supplied reply id. */
    public EncoderInputReply(short repID, DeviceHandle handle,
                             GroveEncoder.Rotation rotation) {
        super(repID, handle);
        this.rotation = rotation;
    }

    /** @return the observed rotation; {@code NONE} on failure replies. */
    public GroveEncoder.Rotation getRotation() {
        return this.rotation;
    }
}
