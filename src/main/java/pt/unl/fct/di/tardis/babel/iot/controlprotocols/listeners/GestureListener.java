package pt.unl.fct.di.tardis.babel.iot.controlprotocols.listeners;

import java.util.function.Consumer;
import pt.unl.fct.di.novasys.iot.device.i2c.GroveGestureDetector;
import pt.unl.fct.di.novasys.iot.device.i2c.GroveGestureDetector.PAJ7620GestureType;
import pt.unl.fct.di.tardis.babel.iot.api.Threshold;

public class GestureListener
    extends IoTListener<GroveGestureDetector, PAJ7620GestureType> {

    public GestureListener(GroveGestureDetector detector,
                           Threshold<PAJ7620GestureType> threshold,
                           Consumer<PAJ7620GestureType> callback) {
        super(detector, threshold, GroveGestureDetector::getGesture, callback);
    }
}
