package pt.unl.fct.di.tardis.babel.iot.controlprotocols.listeners;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Process-wide singleton that owns the background poll loop used by
 * reactive input requests.
 * <p>
 * Listeners (instances of {@link IoTListener}) are registered with
 * {@link #registerIoTListener(IoTListener)} and scheduled on a single
 * daemon thread at a fixed 50&nbsp;ms cadence. Each tick simply calls
 * {@link IoTListener#run()}, which polls the device and invokes the
 * caller's callback when the threshold matches.
 * <p>
 * The executor uses daemon threads so it does not prevent JVM
 * shutdown; call {@link #shutdown()} from the application's exit path
 * if you want to drain the queue cleanly.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class IoTMonitoringService {
    private static IoTMonitoringService instance;
    // TODO map listeners to IDs, here or in main protocol?

    private final ScheduledExecutorService executor =
        Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });

    private final Map<Runnable, ScheduledFuture<?>> activeTasks =
        new ConcurrentHashMap<>();


    /**
     * @return the singleton instance, lazily created on first call.
     *         Not thread-safe at first construction; expected to be
     *         primed during protocol initialisation.
     */
    public static IoTMonitoringService getInstance() {
        if (instance == null) {
            instance = new IoTMonitoringService();
        }

        return instance;
    }

    /**
     * Schedules a listener at the fixed 50&nbsp;ms poll cadence.
     *
     * @param listener the listener to schedule
     * @param <R>      device type carried by the listener
     * @param <T>      value type produced by polling the device
     */
    public <R, T> void registerIoTListener(IoTListener<R, T> listener) {
        ScheduledFuture<?> future = executor.scheduleWithFixedDelay(
            listener, 0, 50,
            TimeUnit.MILLISECONDS); // TODO configure via props?
        activeTasks.put(listener, future);
    }

    /**
     * Cancels and removes a previously registered listener. No-op if
     * the listener was never registered.
     */
    public void unregisterListener(Runnable listener) {
        ScheduledFuture<?> f = activeTasks.remove(listener);
        if (f != null) {
            f.cancel(true);
        }
    }

    /** Cancels every registered listener but keeps the executor open. */
    public void unregisterAll() {
        activeTasks.values().forEach(f -> f.cancel(true));
        activeTasks.clear();
    }

    /** Cancels all listeners and shuts the executor down. */
    public void shutdown() {
        unregisterAll();
        executor.shutdown();
    }
}
