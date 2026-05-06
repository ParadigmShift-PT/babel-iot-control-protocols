package pt.unl.fct.di.tardis.babel.iot.api.replies;

import pt.unl.fct.di.novasys.babel.generic.ProtoReply;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceType;

/**
 * Base class for replies that complete an IoT input request.
 * <p>
 * The reply may carry either a successful outcome (a non-null
 * {@link DeviceHandle}, with the error fields left null) or a failure
 * (a non-null {@link ErrorCode} together with a human-readable
 * message). {@link #isSuccessful()} provides the standard discriminator.
 * Subclasses extend this class to attach typed payloads (for example
 * an accelerometer reading or a distance measurement).
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class IoTInputReply extends ProtoReply {

    /** Type of the device the reply refers to. */
    protected final DeviceType deviceType;
    /** Alias of the device the reply refers to. */
    protected final String deviceAlias;
    /** Handle of the device the reply refers to (null on failure). */
    protected final DeviceHandle handle;
    /** Error code on failure, {@code null} on success. */
    protected final ErrorCode errorCode;
    /** Human-readable error message on failure, {@code null} on success. */
    protected final String errorMessage;

    /**
     * Builds a failure reply.
     *
     * @param repID the Babel reply id
     * @param type  the type of device involved in the request
     * @param alias the alias of the device involved in the request
     * @param e     the error code
     * @param err   a human-readable description of the failure
     */
    public IoTInputReply(short repID, DeviceType type, String alias,
                         ErrorCode e, String err) {
        super(repID);
        this.deviceType = type;
        this.deviceAlias = alias;
        this.handle = null;
        this.errorCode = e;
        this.errorMessage = err;
    }

    /**
     * Builds a successful reply.
     *
     * @param repID  the Babel reply id
     * @param handle the handle of the device that produced the result
     */
    public IoTInputReply(short repID, DeviceHandle handle) {
        super(repID);
        this.handle = handle;
        this.deviceType = handle.getDeviceType();
        this.deviceAlias = handle.getDeviceAlias();
        this.errorCode = null;
        this.errorMessage = null;
    }

    /** @return {@code true} when the reply represents a successful read. */
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
