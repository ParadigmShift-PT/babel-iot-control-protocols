package pt.unl.fct.di.tardis.babel.iot.controlprotocols.notifications;

import pt.unl.fct.di.novasys.iot.device.digital.GroveEncoder.Rotation;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.notifications.IoTInputNotification;

/**
 * Notification carrying a single rotation event observed on a
 * {@link pt.unl.fct.di.novasys.iot.device.digital.GroveEncoder}.
 * Emitted by {@code DigitalInputControlProtocol} for reactive encoder
 * requests.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class EncoderNotification extends IoTInputNotification<Rotation> {

    /** Babel notification id used to dispatch this event. */
    public static final short NOTIFICATION_ID = 4021;

    /**
     * @param handle the handle of the encoder that produced the rotation
     * @param value  the observed rotation direction
     */
    public EncoderNotification(DeviceHandle handle, Rotation value) {
        super(NOTIFICATION_ID, handle, value);
    }
}
