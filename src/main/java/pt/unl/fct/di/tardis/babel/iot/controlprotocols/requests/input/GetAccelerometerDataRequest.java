package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.input;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.InputType;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

/**
 * One-shot request asking {@code I2CInputControlProtocol} to read a
 * Grove three-axis accelerometer. The {@link InputType.Accelerometer}
 * argument selects the read mode (raw axes, magnitude, or full data
 * structure).
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class GetAccelerometerDataRequest extends IoTEventRequest {

    /**
     * Default Babel request id used by this class. <b>ID:</b> {@value}.
     * Handler class: request/reply (shared pool). Owning protocol:
     * {@code I2CInputControlProtocol} (id 2100).
     */
    public static final short REQUEST_ID = 2101;

    /** Read mode that will be applied to the accelerometer. */
    public final InputType.Accelerometer inputType;

    /**
     * @param h         the handle of the accelerometer to read
     * @param inputType the read mode to apply
     */
    public GetAccelerometerDataRequest(DeviceHandle h,
                                       InputType.Accelerometer inputType) {
        super(REQUEST_ID, h);
        this.inputType = inputType;
    }

    /** @return the read mode that will be applied. */
    public InputType.Accelerometer getInputType() { return this.inputType; }
}
