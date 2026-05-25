package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

/**
 * Request asking {@code DigitalOutputControlProtocol} to drive a Grove
 * LED bar to a given level. The level is clamped to the range
 * {@code [0, 32]} on construction.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class DisplayBarRequest extends IoTEventRequest {

    /**
     * Default Babel request id used by this class. <b>ID:</b> {@value}.
     * Handler class: request/reply (shared pool). Owning protocol:
     * {@code I2COutputControlProtocol} (id 2000).
     */
    public static final short REQUEST_ID = 2007;

    private final int level;

    /**
     * @param h     the handle of the LED bar to drive
     * @param level the desired level (clamped to {@code [0, 32]})
     */
    public DisplayBarRequest(DeviceHandle h, int level) {
        super(DisplayBarRequest.REQUEST_ID, h);

        this.level = clamp(level, 0, 32);
    }

    /** @return the (clamped) display level. */
    public int getLevel() { return this.level; }

    /**
     * Clamps {@code value} into {@code [min, max]}.
     *
     * @param value the value to clamp
     * @param min   the inclusive lower bound
     * @param max   the inclusive upper bound
     * @return {@code value}, clipped to lie within the bounds
     */
    // TODO this should go elsewhere but just for testing
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
