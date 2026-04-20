package pt.unl.fct.di.tardis.babel.iot.controlprotocols.replies;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceType;
import pt.unl.fct.di.tardis.babel.iot.api.InputType;
import pt.unl.fct.di.tardis.babel.iot.api.replies.ErrorCode;
import pt.unl.fct.di.tardis.babel.iot.api.replies.IoTInputReply;

public class UltrasonicRangerInputReply extends IoTInputReply {

    public static final short REPLY_ID = 4016;

    private final InputType.UltrasonicRanger inputType;
    private final long distance;

    public UltrasonicRangerInputReply(DeviceType type, String alias,
                                      ErrorCode e, String err) {
        this(REPLY_ID, type, alias, e, err);
    }

    public UltrasonicRangerInputReply(short repID, DeviceType type,
                                      String alias, ErrorCode e, String err) {
        super(repID, type, err, e, err);
        this.distance = -1;
        this.inputType = null;
    }

    public UltrasonicRangerInputReply(DeviceHandle handle,
                                      InputType.UltrasonicRanger inputType,
                                      long distance) {
        this(REPLY_ID, handle, inputType, distance);
    }

    public UltrasonicRangerInputReply(short repID, DeviceHandle handle,
                                      InputType.UltrasonicRanger inputType,
                                      long distance) {
        super(repID, handle);
        this.distance = distance;
        this.inputType = null;
    }

    public InputType.UltrasonicRanger getInputType() { return this.inputType; }

    public long getDistance() { return this.distance; }
}
