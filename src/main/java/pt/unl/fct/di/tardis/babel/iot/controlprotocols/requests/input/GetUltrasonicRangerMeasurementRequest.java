package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.input;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.InputType;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

/**
 * One-shot request asking {@code DigitalInputControlProtocol} to
 * sample a Grove ultrasonic ranger. The
 * {@link InputType.UltrasonicRanger} argument selects the unit the
 * reply will carry (centimetres / millimetres / inches).
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class GetUltrasonicRangerMeasurementRequest extends IoTEventRequest {

    /** Default Babel request id used by this class. */
    public static final short REQUEST_ID = 8017;

    /** Unit in which the reply will carry the distance. */
    public final InputType.UltrasonicRanger inputType;

    /**
     * @param h         the handle of the ultrasonic ranger to sample
     * @param inputType the unit in which the reply should carry the distance
     */
    public GetUltrasonicRangerMeasurementRequest(
        DeviceHandle h, InputType.UltrasonicRanger inputType) {
        super(REQUEST_ID, h);
        this.inputType = inputType;
    }

    /** @return the unit in which the reply will carry the distance. */
    public InputType.UltrasonicRanger getInputType() { return this.inputType; }
}
