package pt.unl.fct.di.tardis.babel.iot.controlprotocols.listeners;

import java.util.function.Consumer;
import pt.unl.fct.di.novasys.iot.device.digital.GroveEncoder;
import pt.unl.fct.di.novasys.iot.device.digital.GroveEncoder.Rotation;
import pt.unl.fct.di.tardis.babel.iot.api.Threshold;

public class EncoderListener
    extends IoTListener<GroveEncoder, Rotation> {

    public EncoderListener(GroveEncoder encoder,
                           Threshold<Rotation> threshold,
                           Consumer<Rotation> callback) {
        super(encoder, threshold, GroveEncoder::getRotation, callback);
    }
}
