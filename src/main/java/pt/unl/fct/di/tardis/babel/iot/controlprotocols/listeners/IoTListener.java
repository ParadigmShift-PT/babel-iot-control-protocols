package pt.unl.fct.di.tardis.babel.iot.controlprotocols.listeners;

import java.util.function.Consumer;
import java.util.function.Function;
import pt.unl.fct.di.tardis.babel.iot.api.Threshold;

public class IoTListener<R, T> implements Runnable {
    protected final R device;
    protected final Threshold<T> threshold;
    private final Function<R, T> getter;
    protected final Consumer<T> callback;

    public IoTListener(R device, Threshold<T> threshold, Function<R, T> getter,
                       Consumer<T> callback) {
        this.device = device;
        this.threshold = threshold;
        this.getter = getter;
        this.callback = callback;
    }

    @Override
    public void run() {
        T currentValue = getter.apply(device);
        if (currentValue != null && threshold.test(currentValue)) {
            callback.accept(currentValue);
        }
    }

    public R getDevice() { return this.device; }

    public Threshold<T> getThreshold() { return this.threshold; }
}
