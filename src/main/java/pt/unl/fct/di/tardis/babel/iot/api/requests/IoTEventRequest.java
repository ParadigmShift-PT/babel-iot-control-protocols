package pt.unl.fct.di.tardis.babel.iot.api.requests;

import pt.unl.fct.di.novasys.babel.generic.ProtoRequest;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;

public abstract class IoTEventRequest extends ProtoRequest {

	protected final DeviceHandle handle;
	
	public IoTEventRequest(short reqId, DeviceHandle handle) {
		super(reqId);
		this.handle = handle;
	}
	
	public DeviceHandle getDeviceHandle() {
		return this.handle;
	}
}
