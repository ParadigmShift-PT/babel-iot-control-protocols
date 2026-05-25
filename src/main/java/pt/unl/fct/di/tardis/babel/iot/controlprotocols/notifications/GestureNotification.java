package pt.unl.fct.di.tardis.babel.iot.controlprotocols.notifications;

import pt.unl.fct.di.novasys.iot.device.i2c.GroveGestureDetector.PAJ7620GestureType;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.notifications.IoTInputNotification;

/**
 * Notification carrying a single gesture detected by a
 * {@link pt.unl.fct.di.novasys.iot.device.i2c.GroveGestureDetector}.
 * Emitted by {@code I2CInputControlProtocol} for reactive gesture
 * requests.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class GestureNotification extends IoTInputNotification<PAJ7620GestureType> {

    /**
     * Babel notification id used to dispatch this event. <b>ID:</b> {@value}.
     * Handler class: notification. Owning protocol:
     * {@code I2CInputControlProtocol} (id 2100).
     */
    public static final short NOTIFICATION_ID = 2101;

    /**
     * @param handle the handle of the gesture detector
     * @param value  the detected gesture
     */
    public GestureNotification(DeviceHandle handle,
                                       PAJ7620GestureType value) {
        super(NOTIFICATION_ID, handle, value);
    }
}
