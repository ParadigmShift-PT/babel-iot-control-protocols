package pt.unl.fct.di.tardis.babel.iot.api.notifications;

import pt.unl.fct.di.novasys.babel.generic.ProtoNotification;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;

public class IoTInputNotification<T> extends ProtoNotification implements Cloneable {

    private final DeviceHandle handle;
    private T value;
    private long timestamp;

    public IoTInputNotification(short id, DeviceHandle handle, T value) {
        super(id);
        this.handle = handle;
        this.value = value;
        this.timestamp = System.currentTimeMillis();
    }

    public DeviceHandle getDeviceHandle() { return this.handle; }

    @Override
    public Object clone() throws CloneNotSupportedException {
        IoTInputNotification<?> copy = (IoTInputNotification<?>)this.clone();
        copy.timestamp = System.currentTimeMillis();
        return copy;
    }

    public T getValue() { return this.value; }

    public long getTimestamp() { return this.timestamp; }

    public IoTInputNotification<T> generateNewInput(T value)
        throws CloneNotSupportedException {
        @SuppressWarnings("unchecked")
        IoTInputNotification<T> copy = (IoTInputNotification<T>)this.clone();
        copy.value = value;
        return copy;
    }
}
