package pt.unl.fct.di.tardis.babel.iot.api.replies;

import pt.unl.fct.di.novasys.babel.generic.ProtoReply;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceType;

public class UnregisterIoTDeviceReply extends ProtoReply {

    public static final short REPLY_ID = 4011;

    private final DeviceHandle handle;
    private final ErrorCode errorCode;
    private final String errorMessage;

    public UnregisterIoTDeviceReply(DeviceType type, String alias, ErrorCode e,
                                    String err) {
        super(REPLY_ID);
        this.handle = null;
        this.errorCode = e;
        this.errorMessage = err;
    }

    public UnregisterIoTDeviceReply(short repID, DeviceType type, String alias,
                                    ErrorCode e, String err) {
        super(repID);
        this.handle = null;
        this.errorCode = e;
        this.errorMessage = err;
    }

    public UnregisterIoTDeviceReply(DeviceHandle handle) {
        super(REPLY_ID);
        this.handle = handle;
        this.errorCode = null;
        this.errorMessage = null;
    }

    public UnregisterIoTDeviceReply(short repID, DeviceHandle handle) {
        super(repID);
        this.handle = handle;
        this.errorCode = null;
        this.errorMessage = null;
    }

    public boolean isSuccessful() { return this.errorCode == null; }

    public DeviceHandle getDeviceHandle() { return this.handle; }

    public ErrorCode getErrorCode() { return this.errorCode; }

    public String getErrorMessage() { return this.errorMessage; }
}
