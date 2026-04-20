package pt.unl.fct.di.tardis.babel.iot.api.requests;

import pt.unl.fct.di.novasys.babel.generic.ProtoRequest;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;

public class UnregisterIoTDeviceRequest extends ProtoRequest {

	public static final short REQUEST_ID = 4001;
	
	private final DeviceHandle handle;
	
	public UnregisterIoTDeviceRequest(DeviceHandle handle) {
		super(REQUEST_ID);
		this.handle = handle;
	}
	
	public DeviceHandle getDeviceHandle() {
		return this.handle;
	}
}
