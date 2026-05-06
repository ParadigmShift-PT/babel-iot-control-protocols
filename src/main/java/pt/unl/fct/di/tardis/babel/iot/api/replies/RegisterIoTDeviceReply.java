package pt.unl.fct.di.tardis.babel.iot.api.replies;

import pt.unl.fct.di.novasys.babel.generic.ProtoReply;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceType;

/**
 * Reply produced in response to a
 * {@link pt.unl.fct.di.tardis.babel.iot.api.requests.RegisterIoTDeviceRequest}.
 * <p>
 * On success the reply carries a non-null {@link DeviceHandle} that
 * the caller must retain in order to interact with the registered
 * device. On failure the reply carries an {@link ErrorCode} and a
 * descriptive message; {@link #isSuccessful()} is the standard
 * discriminator.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class RegisterIoTDeviceReply extends ProtoReply {

    /** Default Babel reply id used by this class. */
    public static final short REPLY_ID = 4010;

    private final DeviceType deviceType;
    private final String deviceAlias;
    private final DeviceHandle handle;
    private final ErrorCode errorCode;
    private final String errorMessage;

    /**
     * Builds a failure reply with the default reply id.
     *
     * @param type  the requested device type
     * @param alias the requested device alias
     * @param e     the error code
     * @param err   a human-readable error message
     */
    public RegisterIoTDeviceReply(DeviceType type, String alias, ErrorCode e,
                                  String err) {
        super(REPLY_ID);
        this.deviceType = type;
        this.deviceAlias = alias;
        this.handle = null;
        this.errorCode = e;
        this.errorMessage = err;
    }

    /**
     * Builds a failure reply with a caller-supplied reply id.
     *
     * @param repID the Babel reply id to use
     * @param type  the requested device type
     * @param alias the requested device alias
     * @param e     the error code
     * @param err   a human-readable error message
     */
    public RegisterIoTDeviceReply(short repID, DeviceType type, String alias,
                                  ErrorCode e, String err) {
        super(repID);
        this.deviceType = type;
        this.deviceAlias = alias;
        this.handle = null;
        this.errorCode = e;
        this.errorMessage = err;
    }

    /**
     * Builds a successful reply with the default reply id.
     *
     * @param handle the handle of the freshly registered device
     */
    public RegisterIoTDeviceReply(DeviceHandle handle) {
        super(REPLY_ID);
        this.handle = handle;
        this.deviceType = handle.getDeviceType();
        this.deviceAlias = handle.getDeviceAlias();
        this.errorCode = null;
        this.errorMessage = null;
    }

    /**
     * Builds a successful reply with a caller-supplied reply id.
     *
     * @param repID  the Babel reply id to use
     * @param handle the handle of the freshly registered device
     */
    public RegisterIoTDeviceReply(short repID, DeviceHandle handle) {
        super(repID);
        this.handle = handle;
        this.deviceType = handle.getDeviceType();
        this.deviceAlias = handle.getDeviceAlias();
        this.errorCode = null;
        this.errorMessage = null;
    }

    /** @return {@code true} when the registration succeeded. */
    public boolean isSuccessful() { return this.errorCode == null; }

    /** @return the device handle on success, {@code null} on failure. */
    public DeviceHandle getDeviceHandle() { return this.handle; }

    /** @return the type of the device the reply refers to. */
    public DeviceType getDeviceType() { return this.deviceType; }

    /** @return the alias of the device the reply refers to. */
    public String getDeviceAlias() { return this.deviceAlias; }

    /** @return the error code on failure, {@code null} on success. */
    public ErrorCode getErrorCode() { return this.errorCode; }

    /** @return the error message on failure, {@code null} on success. */
    public String getErrorMessage() { return this.errorMessage; }
}
