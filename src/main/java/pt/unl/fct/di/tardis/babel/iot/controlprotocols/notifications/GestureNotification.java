package pt.unl.fct.di.tardis.babel.iot.controlprotocols.notifications;

import pt.unl.fct.di.novasys.iot.device.i2c.GroveGestureDetector.PAJ7620GestureType;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.notifications.IoTInputNotification;

public class GestureNotification extends IoTInputNotification<PAJ7620GestureType> {

    public static final short NOTIFICATION_ID = 4020;

    public GestureNotification(DeviceHandle handle,
                                       PAJ7620GestureType value) {
        super(NOTIFICATION_ID, handle, value);
    }
}
