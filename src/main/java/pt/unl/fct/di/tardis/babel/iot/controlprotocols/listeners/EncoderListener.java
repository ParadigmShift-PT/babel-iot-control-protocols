package pt.unl.fct.di.tardis.babel.iot.controlprotocols.listeners;

import java.util.function.Consumer;
import pt.unl.fct.di.novasys.iot.device.digital.GroveEncoder;
import pt.unl.fct.di.novasys.iot.device.digital.GroveEncoder.Rotation;
import pt.unl.fct.di.tardis.babel.iot.api.Threshold;

/**
 * Listener that polls a {@link GroveEncoder} for rotation events and
 * fires a callback when the configured {@link Threshold} accepts the
 * current rotation direction.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class EncoderListener
    extends IoTListener<GroveEncoder, Rotation> {

    /**
     * @param encoder   the encoder to poll
     * @param threshold the predicate that gates callback delivery
     * @param callback  invoked with each accepted {@link Rotation}
     */
    public EncoderListener(GroveEncoder encoder,
                           Threshold<Rotation> threshold,
                           Consumer<Rotation> callback) {
        super(encoder, threshold, GroveEncoder::getRotation, callback);
    }
}
