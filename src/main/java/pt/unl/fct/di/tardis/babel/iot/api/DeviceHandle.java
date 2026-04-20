/********************************************************************************************
 * DeviceHandle (represents a device handle to be used in interactions with the
 *control)
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 ********************************************************************************************/

package pt.unl.fct.di.tardis.babel.iot.api;

public class DeviceHandle {

    private final DeviceType deviceType;
    private final short protoManagerID;
    private final String protoManagerName;
    private final short identifier;
    private final String alias;

    public DeviceHandle(DeviceType type, short protoID, String protoName,
                        short id, String alias) {
        this.deviceType = type;
        this.protoManagerID = protoID;
        this.protoManagerName = protoName;
        this.identifier = id;
        this.alias = alias;
    }

    public DeviceType getDeviceType() { return deviceType; }

    public short getProtoManagerID() { return protoManagerID; }

    public String getProtoManagerName() { return protoManagerName; }

    public short getDeviceID() { return identifier; }

    public String getDeviceAlias() { return alias; }
}
