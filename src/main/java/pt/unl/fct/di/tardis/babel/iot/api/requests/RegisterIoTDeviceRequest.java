package pt.unl.fct.di.tardis.babel.iot.api.requests;

import pt.unl.fct.di.novasys.babel.generic.ProtoRequest;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceInterface;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceType;

public class RegisterIoTDeviceRequest extends ProtoRequest {

    public static final short REQUEST_ID = 4000;

    private final DeviceType deviceType;
    private final String deviceAlias;
    private final int line;

    public RegisterIoTDeviceRequest(DeviceType type, String alias, int line) {
        this(REQUEST_ID, type, alias, line);
    }

    public RegisterIoTDeviceRequest(DeviceType type, String alias) {
        this(REQUEST_ID, type, alias, -1);
    }

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

    public DeviceType getDeviceType() { return this.deviceType; }

    public String getDeviceAlias() { return this.deviceAlias; }

    public int getLine() { return line; }
}
