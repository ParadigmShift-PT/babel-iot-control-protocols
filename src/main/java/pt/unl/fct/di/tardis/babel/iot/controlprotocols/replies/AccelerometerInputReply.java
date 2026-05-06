package pt.unl.fct.di.tardis.babel.iot.controlprotocols.replies;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceType;
import pt.unl.fct.di.tardis.babel.iot.api.InputType;
import pt.unl.fct.di.tardis.babel.iot.api.replies.ErrorCode;
import pt.unl.fct.di.tardis.babel.iot.api.replies.IoTInputReply;

/**
 * Typed reply carrying a reading from a Grove three-axis accelerometer.
 * <p>
 * The {@code measurement} payload is generic because the read mode
 * (selected via {@link InputType.Accelerometer}) determines what shape
 * the value takes — a plain magnitude, a per-axis tuple, the raw
 * registers, or the full data structure exposed by the device driver.
 *
 * @param <T> the concrete type of the carried measurement.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class AccelerometerInputReply<T> extends IoTInputReply {

    /** Default Babel reply id used by this class. */
    public static final short REPLY_ID = 4012;

    private final InputType.Accelerometer inputType;
    private final T measurement;

    /**
     * Failure reply with the default reply id.
     *
     * @param type  device type
     * @param alias device alias
     * @param e     error code
     * @param err   error message
     */
    public AccelerometerInputReply(DeviceType type, String alias, ErrorCode e,
                                   String err) {
        this(REPLY_ID, type, alias, e, err);
    }

    /**
     * Failure reply with a caller-supplied reply id.
     *
     * @param repID reply id to use
     * @param type  device type
     * @param alias device alias
     * @param e     error code
     * @param err   error message
     */
    public AccelerometerInputReply(short repID, DeviceType type, String alias,
                                   ErrorCode e, String err) {
        super(repID, type, err, e, err);
        this.measurement = null;
        this.inputType = null;
    }

    /**
     * Successful reply with the default reply id.
     *
     * @param handle      handle of the source device
     * @param inputType   read mode that produced the measurement
     * @param measurement the measurement
     */
    public AccelerometerInputReply(DeviceHandle handle,
                                   InputType.Accelerometer inputType,
                                   T measurement) {
        this(REPLY_ID, handle, inputType, measurement);
    }

    /**
     * Successful reply with a caller-supplied reply id.
     *
     * @param repID       reply id to use
     * @param handle      handle of the source device
     * @param inputType   read mode that produced the measurement
     * @param measurement the measurement
     */
    public AccelerometerInputReply(short repID, DeviceHandle handle,
                                   InputType.Accelerometer inputType,
                                   T measurement) {
        super(repID, handle);
        this.measurement = measurement;
        this.inputType = inputType;
    }

    /** @return the read mode that produced the measurement. */
    public InputType.Accelerometer getInputType() { return this.inputType; }

    /** @return the measurement payload, or {@code null} on failure. */
    public T getMeasurement() { return this.measurement; }
}
