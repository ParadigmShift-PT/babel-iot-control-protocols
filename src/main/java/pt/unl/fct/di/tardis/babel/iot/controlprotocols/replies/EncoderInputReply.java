package pt.unl.fct.di.tardis.babel.iot.controlprotocols.replies;

import pt.unl.fct.di.novasys.iot.device.digital.GroveEncoder;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceType;
import pt.unl.fct.di.tardis.babel.iot.api.replies.ErrorCode;
import pt.unl.fct.di.tardis.babel.iot.api.replies.IoTInputReply;

public class EncoderInputReply extends IoTInputReply {

    public static final short REPLY_ID = 4014;

    private final GroveEncoder.Rotation rotation;

    public EncoderInputReply(DeviceType type, String alias, ErrorCode e,
                             String err) {
        this(REPLY_ID, type, alias, e, err);
    }

    public EncoderInputReply(short repID, DeviceType type, String alias,
                             ErrorCode e, String err) {
        super(repID, type, err, e, err);
        this.rotation = GroveEncoder.Rotation.NONE;
    }

    public EncoderInputReply(DeviceHandle handle,
                             GroveEncoder.Rotation rotation) {
        this(REPLY_ID, handle, rotation);
    }

    public EncoderInputReply(short repID, DeviceHandle handle,
                             GroveEncoder.Rotation rotation) {
        super(repID, handle);
        this.rotation = rotation;
    }

    public GroveEncoder.Rotation getRotation() {
        return this.rotation;
    }
}
