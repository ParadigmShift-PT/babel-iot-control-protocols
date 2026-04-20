package pt.unl.fct.di.tardis.babel.iot.controlprotocols.listeners;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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


    public static IoTMonitoringService getInstance() {
        if (instance == null) {
            instance = new IoTMonitoringService();
        }

        return instance;
    }

    public <R, T> void registerIoTListener(IoTListener<R, T> listener) {
        ScheduledFuture<?> future = executor.scheduleWithFixedDelay(
            listener, 0, 50,
            TimeUnit.MILLISECONDS); // TODO configure via props?
        activeTasks.put(listener, future);
    }

    public void unregisterListener(Runnable listener) {
        ScheduledFuture<?> f = activeTasks.remove(listener);
        if (f != null) {
            f.cancel(true);
        }
    }

    public void unregisterAll() {
        activeTasks.values().forEach(f -> f.cancel(true));
        activeTasks.clear();
    }

    public void shutdown() {
        unregisterAll();
        executor.shutdown();
    }
}
