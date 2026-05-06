package pt.unl.fct.di.tardis.babel.iot.controlprotocols.listeners;

import java.util.function.Consumer;
import pt.unl.fct.di.novasys.iot.device.i2c.GroveGestureDetector;
import pt.unl.fct.di.novasys.iot.device.i2c.GroveGestureDetector.PAJ7620GestureType;
import pt.unl.fct.di.tardis.babel.iot.api.Threshold;

/**
 * Listener that polls a {@link GroveGestureDetector} (a PAJ7620-based
 * I²C sensor) and fires a callback when the configured {@link Threshold}
 * accepts the detected gesture.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class GestureListener
    extends IoTListener<GroveGestureDetector, PAJ7620GestureType> {

    /**
     * @param detector  the gesture detector to poll
     * @param threshold predicate that gates callback delivery
     * @param callback  invoked with each accepted gesture
     */
    public GestureListener(GroveGestureDetector detector,
                           Threshold<PAJ7620GestureType> threshold,
                           Consumer<PAJ7620GestureType> callback) {
        super(detector, threshold, GroveGestureDetector::getGesture, callback);
    }
}
