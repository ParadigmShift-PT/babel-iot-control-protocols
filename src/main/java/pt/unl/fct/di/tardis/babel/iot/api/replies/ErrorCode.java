package pt.unl.fct.di.tardis.babel.iot.api.replies;

/**
 * Error codes carried by IoT control replies when a request cannot be
 * fulfilled.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public enum ErrorCode {
    /** The requested {@code DeviceType} is not known to the protocol. */
    UNKNOWN_DEVICE,
    /** The supplied {@code DeviceHandle} does not match any registered device. */
    UNKNOWN_HANDLE,
    /** The targeted device is registered but currently unreachable. */
    DEVICE_NOT_AVAILABLE,
    /** Initialisation of the device driver failed. */
    DEVICE_INIT_ERR,
    /** The targeted device is not registered. */
    NOT_REGISTERED,
    /** The driver returned an error while sampling the device. */
    FAILED_MEASUREMENT,
    /** The requested operation is not valid for the device's bus interface. */
    INVALID_INTERFACE
}
