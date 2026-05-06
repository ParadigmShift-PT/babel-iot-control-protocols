package pt.unl.fct.di.tardis.babel.iot.api;

/**
 * Opaque handle that identifies a single registered IoT device within a
 * Babel control protocol.
 * <p>
 * A {@code DeviceHandle} is returned by the protocol in response to a
 * {@link pt.unl.fct.di.tardis.babel.iot.api.requests.RegisterIoTDeviceRequest}
 * and must subsequently be supplied with every input or output request
 * that targets the same device. The handle bundles together the
 * device type, the identity of the managing protocol, the device's
 * internal numeric identifier, and the user-provided alias.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class DeviceHandle {

    private final DeviceType deviceType;
    private final short protoManagerID;
    private final String protoManagerName;
    private final short identifier;
    private final String alias;

    /**
     * Builds a handle for a freshly registered device.
     *
     * @param type      the type of device this handle refers to
     * @param protoID   the Babel protocol id of the protocol that manages
     *                  the device
     * @param protoName the human-readable name of the managing protocol
     * @param id        the internal numeric identifier assigned by the
     *                  managing protocol to this device
     * @param alias     the user-provided alias for the device
     */
    public DeviceHandle(DeviceType type, short protoID, String protoName,
                        short id, String alias) {
        this.deviceType = type;
        this.protoManagerID = protoID;
        this.protoManagerName = protoName;
        this.identifier = id;
        this.alias = alias;
    }

    /** @return the type of device this handle refers to. */
    public DeviceType getDeviceType() { return deviceType; }

    /** @return the Babel protocol id of the protocol that manages the device. */
    public short getProtoManagerID() { return protoManagerID; }

    /** @return the human-readable name of the managing protocol. */
    public String getProtoManagerName() { return protoManagerName; }

    /** @return the internal numeric identifier assigned to this device. */
    public short getDeviceID() { return identifier; }

    /** @return the user-provided alias for this device. */
    public String getDeviceAlias() { return alias; }
}
