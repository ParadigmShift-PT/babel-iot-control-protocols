package pt.unl.fct.di.tardis.babel.iot.controlprotocols.replies;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceType;
import pt.unl.fct.di.tardis.babel.iot.api.InputType;
import pt.unl.fct.di.tardis.babel.iot.api.replies.ErrorCode;
import pt.unl.fct.di.tardis.babel.iot.api.replies.IoTInputReply;

public class AccelerometerInputReply<T> extends IoTInputReply {

    public static final short REPLY_ID = 4012;

    private final InputType.Accelerometer inputType;
    private final T measurement;

    public AccelerometerInputReply(DeviceType type, String alias, ErrorCode e,
                                   String err) {
        this(REPLY_ID, type, alias, e, err);
    }

    public AccelerometerInputReply(short repID, DeviceType type, String alias,
                                   ErrorCode e, String err) {
        super(repID, type, err, e, err);
        this.measurement = null;
        this.inputType = null;
    }

    public AccelerometerInputReply(DeviceHandle handle,
                                   InputType.Accelerometer inputType,
                                   T measurement) {
        this(REPLY_ID, handle, inputType, measurement);
    }

    public AccelerometerInputReply(short repID, DeviceHandle handle,
                                   InputType.Accelerometer inputType,
                                   T measurement) {
        super(repID, handle);
        this.measurement = measurement;
        this.inputType = inputType;
    }

    public InputType.Accelerometer getInputType() { return this.inputType; }

    public T getMeasurement() { return this.measurement; }
}
