package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.input;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.InputType;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

/**
 * One-shot request asking {@code I2CInputControlProtocol} to read a
 * Grove barometer. The {@link InputType.Barometer} argument selects
 * the read mode (temperature, pressure, altitude, or all of them).
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class GetBarometerDataRequest extends IoTEventRequest {

    /** Default Babel request id used by this class. */
    public static final short REQUEST_ID = 8012;

    /** Read mode that will be applied to the barometer. */
    public final InputType.Barometer inputType;

    /**
     * @param h         the handle of the barometer to read
     * @param inputType the read mode to apply
     */
    public GetBarometerDataRequest(DeviceHandle h,
                                       InputType.Barometer inputType) {
        super(REQUEST_ID, h);
        this.inputType = inputType;
    }

    /** @return the read mode that will be applied. */
    public InputType.Barometer getInputType() { return this.inputType; }
}
