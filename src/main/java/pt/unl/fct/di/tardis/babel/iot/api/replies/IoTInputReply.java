package pt.unl.fct.di.tardis.babel.iot.api.replies;

import pt.unl.fct.di.novasys.babel.generic.ProtoReply;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceType;

public class IoTInputReply extends ProtoReply {

    protected final DeviceType deviceType;
    protected final String deviceAlias;
    protected final DeviceHandle handle;
    protected final ErrorCode errorCode;
    protected final String errorMessage;

    public IoTInputReply(short repID, DeviceType type, String alias,
                         ErrorCode e, String err) {
        super(repID);
        this.deviceType = type;
        this.deviceAlias = alias;
        this.handle = null;
        this.errorCode = e;
        this.errorMessage = err;
    }

    public IoTInputReply(short repID, DeviceHandle handle) {
        super(repID);
        this.handle = handle;
        this.deviceType = handle.getDeviceType();
        this.deviceAlias = handle.getDeviceAlias();
        this.errorCode = null;
        this.errorMessage = null;
    }

    public boolean isSuccessful() { return this.errorCode == null; }

    public DeviceHandle getDeviceHandle() { return this.handle; }

    public DeviceType getDeviceType() { return this.deviceType; }

    public String getDeviceAlias() { return this.deviceAlias; }

    public ErrorCode getErrorCode() { return this.errorCode; }

    public String getErrorMessage() { return this.errorMessage; }
}
