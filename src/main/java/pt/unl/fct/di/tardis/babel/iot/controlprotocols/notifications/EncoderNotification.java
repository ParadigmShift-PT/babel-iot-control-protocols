package pt.unl.fct.di.tardis.babel.iot.controlprotocols.notifications;

import pt.unl.fct.di.novasys.iot.device.digital.GroveEncoder.Rotation;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.notifications.IoTInputNotification;

public class EncoderNotification extends IoTInputNotification<Rotation> {

    public static final short NOTIFICATION_ID = 4021;

    public EncoderNotification(DeviceHandle handle, Rotation value) {
        super(NOTIFICATION_ID, handle, value);
    }
}
