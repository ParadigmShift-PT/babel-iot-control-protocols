package pt.unl.fct.di.tardis.babel.iot.controlprotocols.replies;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceType;
import pt.unl.fct.di.tardis.babel.iot.api.InputType;
import pt.unl.fct.di.tardis.babel.iot.api.replies.ErrorCode;
import pt.unl.fct.di.tardis.babel.iot.api.replies.IoTInputReply;

/**
 * Typed reply carrying a distance measured by a Grove ultrasonic
 * ranger. The unit (centimetres / millimetres / inches) is communicated
 * through the {@link InputType.UltrasonicRanger} carried in the reply.
 * Failure replies set {@code distance} to {@code -1}; check
 * {@link #isSuccessful()} before consuming the value.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class UltrasonicRangerInputReply extends IoTInputReply {

    /**
     * Default Babel reply id used by this class. <b>ID:</b> {@value}.
     * Handler class: request/reply (shared pool — continues after the
     * protocol's requests). Owning protocol:
     * {@code DigitalInputControlProtocol} (id 2200).
     */
    public static final short REPLY_ID = 2205;

    private final InputType.UltrasonicRanger inputType;
    private final long distance;

    /** Failure reply with the default reply id. */
    public UltrasonicRangerInputReply(DeviceType type, String alias,
                                      ErrorCode e, String err) {
        this(REPLY_ID, type, alias, e, err);
    }

    /** Failure reply with a caller-supplied reply id. */
    public UltrasonicRangerInputReply(short repID, DeviceType type,
                                      String alias, ErrorCode e, String err) {
        super(repID, type, err, e, err);
        this.distance = -1;
        this.inputType = null;
    }

    /** Successful reply with the default reply id. */
    public UltrasonicRangerInputReply(DeviceHandle handle,
                                      InputType.UltrasonicRanger inputType,
                                      long distance) {
        this(REPLY_ID, handle, inputType, distance);
    }

    /** Successful reply with a caller-supplied reply id. */
    public UltrasonicRangerInputReply(short repID, DeviceHandle handle,
                                      InputType.UltrasonicRanger inputType,
                                      long distance) {
        super(repID, handle);
        this.distance = distance;
        this.inputType = null;
    }

    /** @return the unit of the carried distance. */
    public InputType.UltrasonicRanger getInputType() { return this.inputType; }

    /** @return the measured distance, or {@code -1} on failure. */
    public long getDistance() { return this.distance; }
}
