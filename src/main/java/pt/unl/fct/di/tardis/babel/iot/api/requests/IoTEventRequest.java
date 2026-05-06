package pt.unl.fct.di.tardis.babel.iot.api.requests;

import pt.unl.fct.di.novasys.babel.generic.ProtoRequest;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;

/**
 * Abstract base class for any IoT control request that targets an
 * already-registered device.
 * <p>
 * Concrete subclasses ({@link IoTPeriodicEventRequest},
 * {@link IoTReactiveEventRequest}, and the device-specific input /
 * output requests under {@code controlprotocols.requests}) extend
 * this class to add timing semantics, threshold predicates, or
 * operation-specific payloads. The handle of the targeted device is
 * always required.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public abstract class IoTEventRequest extends ProtoRequest {

	/** Handle of the device the request targets. */
	protected final DeviceHandle handle;

	/**
	 * @param reqId  the Babel request id
	 * @param handle the handle of the device the request targets
	 */
	public IoTEventRequest(short reqId, DeviceHandle handle) {
		super(reqId);
		this.handle = handle;
	}

	/** @return the handle of the device this request targets. */
	public DeviceHandle getDeviceHandle() {
		return this.handle;
	}
}
