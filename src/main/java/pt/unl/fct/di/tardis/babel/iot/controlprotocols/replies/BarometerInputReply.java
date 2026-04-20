package pt.unl.fct.di.tardis.babel.iot.controlprotocols.replies;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceType;
import pt.unl.fct.di.tardis.babel.iot.api.InputType;
import pt.unl.fct.di.tardis.babel.iot.api.replies.ErrorCode;
import pt.unl.fct.di.tardis.babel.iot.api.replies.IoTInputReply;

public class BarometerInputReply<T> extends IoTInputReply {

    public static final short REPLY_ID = 4013;

    private final InputType.Barometer inputType;
    private final T measurement;

    public BarometerInputReply(DeviceType type, String alias, ErrorCode e,
                                   String err) {
        this(REPLY_ID, type, alias, e, err);
    }

    public BarometerInputReply(short repID, DeviceType type, String alias,
                                   ErrorCode e, String err) {
        super(repID, type, err, e, err);
        this.measurement = null;
        this.inputType = null;
    }

    public BarometerInputReply(DeviceHandle handle,
                                   InputType.Barometer inputType,
                                   T measurement) {
        this(REPLY_ID, handle, inputType, measurement);
    }

    public BarometerInputReply(short repID, DeviceHandle handle,
                                   InputType.Barometer inputType,
                                   T measurement) {
        super(repID, handle);
        this.measurement = measurement;
        this.inputType = inputType;
    }

    public InputType.Barometer getInputType() { return this.inputType; }

    public T getMeasurement() { return this.measurement; }
}
