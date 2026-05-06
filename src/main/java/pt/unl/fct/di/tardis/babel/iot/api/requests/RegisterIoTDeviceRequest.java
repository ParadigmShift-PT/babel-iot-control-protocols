package pt.unl.fct.di.tardis.babel.iot.api.requests;

import pt.unl.fct.di.novasys.babel.generic.ProtoRequest;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceInterface;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceType;

/**
 * Request asking an IoT control protocol to register a new device.
 * <p>
 * On success the protocol replies with a
 * {@link pt.unl.fct.di.tardis.babel.iot.api.replies.RegisterIoTDeviceReply}
 * carrying a {@link pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle}
 * which the caller must use for any subsequent interaction with the
 * device. Non-I²C devices require a GPIO line number; for I²C devices
 * the line argument is ignored and may be omitted (in which case it
 * defaults to {@code -1}).
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class RegisterIoTDeviceRequest extends ProtoRequest {

    /** Default Babel request id used by this class. */
    public static final short REQUEST_ID = 4000;

    private final DeviceType deviceType;
    private final String deviceAlias;
    private final int line;

    /**
     * Builds a registration request with the default request id.
     *
     * @param type  the type of device to register
     * @param alias a human-readable alias for the device
     * @param line  the GPIO line the device is wired to
     */
    public RegisterIoTDeviceRequest(DeviceType type, String alias, int line) {
        this(REQUEST_ID, type, alias, line);
    }

    /**
     * Builds a registration request for an I²C device (no GPIO line
     * required), using the default request id.
     *
     * @param type  the type of device to register
     * @param alias a human-readable alias for the device
     */
    public RegisterIoTDeviceRequest(DeviceType type, String alias) {
        this(REQUEST_ID, type, alias, -1);
    }

    /**
     * Builds a registration request with a caller-supplied request id.
     *
     * @param reqID the Babel request id to use
     * @param type  the type of device to register
     * @param alias a human-readable alias for the device
     * @param line  the GPIO line the device is wired to ({@code -1}
     *              for I²C devices)
     */
    public RegisterIoTDeviceRequest(short reqID, DeviceType type, String alias,
                                    int line) {
        super(reqID);
        this.deviceType = type;
        this.deviceAlias = alias;
        this.line = line;

        if (line < 0 &&
            !(type.getDeviceInterface().equals(DeviceInterface.I2C_IN) ||
              type.getDeviceInterface().equals(DeviceInterface.I2C_OUT))) {
            // if it's neithter I2C IN or OUT, then it needs a line number
            // want to throw exception?
            System.err.println("Non-I2C devices require a line number");
        }
    }

    /** @return the type of device being registered. */
    public DeviceType getDeviceType() { return this.deviceType; }

    /** @return the alias provided for the device. */
    public String getDeviceAlias() { return this.deviceAlias; }

    /**
     * @return the GPIO line the device is wired to, or {@code -1} for
     *         I²C devices.
     */
    public int getLine() { return line; }
}
