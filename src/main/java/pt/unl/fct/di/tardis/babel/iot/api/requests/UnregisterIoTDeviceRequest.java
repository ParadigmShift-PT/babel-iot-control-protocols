package pt.unl.fct.di.tardis.babel.iot.api.requests;

import pt.unl.fct.di.novasys.babel.generic.ProtoRequest;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;

/**
 * Request asking an IoT control protocol to unregister a previously
 * registered device.
 * <p>
 * After the protocol replies the handle is invalidated; any subsequent
 * input or output request that re-uses it will fail with
 * {@link pt.unl.fct.di.tardis.babel.iot.api.replies.ErrorCode#UNKNOWN_HANDLE}.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class UnregisterIoTDeviceRequest extends ProtoRequest {

	/** Default Babel request id used by this class. */
	public static final short REQUEST_ID = 4001;

	private final DeviceHandle handle;

	/**
	 * @param handle the handle of the device to unregister
	 */
	public UnregisterIoTDeviceRequest(DeviceHandle handle) {
		super(REQUEST_ID);
		this.handle = handle;
	}

	/** @return the handle of the device being unregistered. */
	public DeviceHandle getDeviceHandle() {
		return this.handle;
	}
}
