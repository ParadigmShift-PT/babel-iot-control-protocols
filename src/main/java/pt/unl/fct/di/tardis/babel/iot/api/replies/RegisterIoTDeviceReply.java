package pt.unl.fct.di.tardis.babel.iot.api.replies;

import pt.unl.fct.di.novasys.babel.generic.ProtoReply;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceType;

public class RegisterIoTDeviceReply extends ProtoReply {

    public static final short REPLY_ID = 4010;

    private final DeviceType deviceType;
    private final String deviceAlias;
    private final DeviceHandle handle;
    private final ErrorCode errorCode;
    private final String errorMessage;

    public RegisterIoTDeviceReply(DeviceType type, String alias, ErrorCode e,
                                  String err) {
        super(REPLY_ID);
        this.deviceType = type;
        this.deviceAlias = alias;
        this.handle = null;
        this.errorCode = e;
        this.errorMessage = err;
    }

    public RegisterIoTDeviceReply(short repID, DeviceType type, String alias,
                                  ErrorCode e, String err) {
        super(repID);
        this.deviceType = type;
        this.deviceAlias = alias;
        this.handle = null;
        this.errorCode = e;
        this.errorMessage = err;
    }

    public RegisterIoTDeviceReply(DeviceHandle handle) {
        super(REPLY_ID);
        this.handle = handle;
        this.deviceType = handle.getDeviceType();
        this.deviceAlias = handle.getDeviceAlias();
        this.errorCode = null;
        this.errorMessage = null;
    }

    public RegisterIoTDeviceReply(short repID, DeviceHandle handle) {
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
