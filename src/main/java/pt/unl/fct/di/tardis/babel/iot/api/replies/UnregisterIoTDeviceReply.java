package pt.unl.fct.di.tardis.babel.iot.api.replies;

import pt.unl.fct.di.novasys.babel.generic.ProtoReply;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceType;

/**
 * Reply produced in response to a
 * {@link pt.unl.fct.di.tardis.babel.iot.api.requests.UnregisterIoTDeviceRequest}.
 * <p>
 * On success the reply carries the handle of the device that was
 * unregistered (the handle is invalidated by the protocol immediately
 * after the reply is built). On failure the reply carries an
 * {@link ErrorCode} together with a human-readable message;
 * {@link #isSuccessful()} is the standard discriminator.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class UnregisterIoTDeviceReply extends ProtoReply {

    /** Default Babel reply id used by this class. */
    public static final short REPLY_ID = 4011;

    private final DeviceHandle handle;
    private final ErrorCode errorCode;
    private final String errorMessage;

    /**
     * Builds a failure reply with the default reply id.
     *
     * @param type  the type of device the unregister attempt was for
     * @param alias the alias of the device the unregister attempt was for
     * @param e     the error code
     * @param err   a human-readable error message
     */
    public UnregisterIoTDeviceReply(DeviceType type, String alias, ErrorCode e,
                                    String err) {
        super(REPLY_ID);
        this.handle = null;
        this.errorCode = e;
        this.errorMessage = err;
    }

    /**
     * Builds a failure reply with a caller-supplied reply id.
     *
     * @param repID the Babel reply id to use
     * @param type  the type of device the unregister attempt was for
     * @param alias the alias of the device the unregister attempt was for
     * @param e     the error code
     * @param err   a human-readable error message
     */
    public UnregisterIoTDeviceReply(short repID, DeviceType type, String alias,
                                    ErrorCode e, String err) {
        super(repID);
        this.handle = null;
        this.errorCode = e;
        this.errorMessage = err;
    }

    /**
     * Builds a successful reply with the default reply id.
     *
     * @param handle the handle of the device that was unregistered
     */
    public UnregisterIoTDeviceReply(DeviceHandle handle) {
        super(REPLY_ID);
        this.handle = handle;
        this.errorCode = null;
        this.errorMessage = null;
    }

    /**
     * Builds a successful reply with a caller-supplied reply id.
     *
     * @param repID  the Babel reply id to use
     * @param handle the handle of the device that was unregistered
     */
    public UnregisterIoTDeviceReply(short repID, DeviceHandle handle) {
        super(repID);
        this.handle = handle;
        this.errorCode = null;
        this.errorMessage = null;
    }

    /** @return {@code true} when the unregister succeeded. */
    public boolean isSuccessful() { return this.errorCode == null; }

    /** @return the device handle on success, {@code null} on failure. */
    public DeviceHandle getDeviceHandle() { return this.handle; }

    /** @return the error code on failure, {@code null} on success. */
    public ErrorCode getErrorCode() { return this.errorCode; }

    /** @return the error message on failure, {@code null} on success. */
    public String getErrorMessage() { return this.errorMessage; }
}
