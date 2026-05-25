package pt.unl.fct.di.tardis.babel.iot.controlprotocols;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.library.pigpio.PiGpio;
import com.pi4j.plugin.gpiod.provider.gpio.digital.GpioDDigitalInputProvider;
import com.pi4j.plugin.gpiod.provider.gpio.digital.GpioDDigitalOutputProvider;
import com.pi4j.plugin.linuxfs.provider.i2c.LinuxFsI2CProvider;
import com.pi4j.plugin.pigpio.provider.gpio.digital.PiGpioDigitalInputProvider;
import com.pi4j.plugin.pigpio.provider.gpio.digital.PiGpioDigitalOutputProvider;
import com.pi4j.plugin.pigpio.provider.pwm.PiGpioPwmProvider;
import com.pi4j.plugin.pigpio.provider.serial.PiGpioSerialProvider;
import com.pi4j.plugin.pigpio.provider.spi.PiGpioSpiProvider;
import com.pi4j.plugin.raspberrypi.platform.RaspberryPiPlatform;

import pt.unl.fct.di.novasys.babel.core.Babel;
import pt.unl.fct.di.novasys.babel.core.GenericProtocol;
import pt.unl.fct.di.novasys.babel.exceptions.HandlerRegistrationException;
import pt.unl.fct.di.novasys.iot.device.Device;
import pt.unl.fct.di.novasys.iot.device.digital.GroveChainableRGB;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceInterface;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceType;
import pt.unl.fct.di.tardis.babel.iot.api.replies.ErrorCode;
import pt.unl.fct.di.tardis.babel.iot.api.replies.RegisterIoTDeviceReply;
import pt.unl.fct.di.tardis.babel.iot.api.requests.RegisterIoTDeviceRequest;
import pt.unl.fct.di.tardis.babel.iot.api.requests.UnregisterIoTDeviceRequest;
import pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output.SetChainableLEDColorHSBRequest;
import pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output.SetChainableLEDColorRGBRequest;
import pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output.SetMultipleChainableLEDColorHSBRequest;
import pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output.SetMultipleChainableLEDColorRGBRequest;

/**
 * Babel protocol that drives Grove digital-output actuators attached
 * to a Raspberry Pi (currently the chainable RGB LED strip).
 * <p>
 * The strip length is read from the configuration property
 * {@value #RGB_LED_COUNT} on {@link #init(Properties)}; a default of
 * one LED is assumed if the property is absent.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class DigitalOutputControlProtocol extends GenericProtocol {

	/** Babel protocol name reported to the runtime. */
	public final static String PROTOCOL_NAME = "DigitalOutputControlProtocol";
	/** Babel protocol id used to address this protocol. <b>ID:</b> {@value}. */
	public final static short PROTOCOL_ID = 2300;

	/** Configuration property name carrying the chainable-RGB strip length. */
	public final static String RGB_LED_COUNT = "rgb.led.count";

	private static final Logger logger = LogManager.getLogger(DigitalOutputControlProtocol.class);

	private final Context pi4j;

	private GroveChainableRGB rgb;

	private short numLeds;

	private final AtomicInteger ids;
	private final HashMap<Device, Short> deviceIds;
	private final HashMap<Short, Device> deviceIdsMapping;
	private final HashMap<DeviceHandle, Device> deviceMappings;
	private final HashMap<Device, Set<DeviceHandle>> deviceHandles;

	/**
	 * Builds a fresh protocol instance, including a dedicated Pi4J
	 * context. Created devices and handles are tracked per-instance.
	 */
	public DigitalOutputControlProtocol() {
		super(PROTOCOL_NAME, PROTOCOL_ID);

		final PiGpio piGpio = PiGpio.newNativeInstance();

		pi4j = Pi4J.newContextBuilder().noAutoDetect().add(new RaspberryPiPlatform() {
			@Override
			protected String[] getProviders() {
				return new String[] {};
			}
		}).add(PiGpioDigitalInputProvider.newInstance(piGpio), PiGpioDigitalOutputProvider.newInstance(piGpio),
				PiGpioPwmProvider.newInstance(piGpio), PiGpioSerialProvider.newInstance(piGpio),
				PiGpioSpiProvider.newInstance(piGpio), GpioDDigitalInputProvider.newInstance(),
				GpioDDigitalOutputProvider.newInstance(), LinuxFsI2CProvider.newInstance()).build();

		this.ids = new AtomicInteger(0);
		this.deviceIds = new HashMap<Device, Short>();
		this.deviceIdsMapping = new HashMap<Short, Device>();
		this.deviceMappings = new HashMap<DeviceHandle, Device>();
		this.deviceHandles = new HashMap<Device, Set<DeviceHandle>>();
	}

	/**
	 * Reads the chainable-RGB strip length from {@code props} (key
	 * {@value #RGB_LED_COUNT}; defaults to {@code 1}) and registers
	 * handlers for device registration / unregistration plus the four
	 * chainable-LED colour-setting requests.
	 */
	@Override
	public void init(Properties props) throws HandlerRegistrationException, IOException {
		if (props.containsKey(RGB_LED_COUNT)) {
			this.numLeds = Short.parseShort(props.getProperty(RGB_LED_COUNT));
		} else {
			this.numLeds = 1;
			logger.debug(
					"Assuming 1 Chainable RGB LED is attached.\n" + "Configurable via .props file with rgb.led.count");
		}

		registerRequestHandler(RegisterIoTDeviceRequest.REQUEST_ID, 
				this::handleRegisterIoTDeviceRequest);
		registerRequestHandler(UnregisterIoTDeviceRequest.REQUEST_ID, 
				this::handleUnregisterIoTDeviceRequest);

		registerRequestHandler(SetChainableLEDColorRGBRequest.REQUEST_ID, 
				this::handleChainableLEDColorRGBRequest);
		registerRequestHandler(SetChainableLEDColorHSBRequest.REQUEST_ID, 
				this::handleChainableLEDColorHSBRequest);
		registerRequestHandler(SetMultipleChainableLEDColorRGBRequest.REQUEST_ID,
				this::handleMultipleChainableLEDColorRGBRequest);
		registerRequestHandler(SetMultipleChainableLEDColorHSBRequest.REQUEST_ID,
				this::handleMultipleChainableLEDColorHSBRequest);
	}

	/*******************************************************************************
	 * handles for Protoocol Requests
	 ******************************************************************************/

	/*
	 * GROVE_DIGITAL_INPUT_DEVICE(DeviceInterface.DIGITAL),
	 * GROVE_DIGITAL_OUTPUT_DEVICE(DeviceInterface.DIGITAL),
	 * GROVE_4DIGIT_DISPLAY(DeviceInterface.DIGITAL),
	 * GROVE_BUZZER(DeviceInterface.DIGITAL),
	 * GROVE_CHAINABLE_RGB(DeviceInterface.DIGITAL),
	 * GROVE_LED_BAR(DeviceInterface.DIGITAL),
	 * GROVE_ULTRASONIC_RANGER(DeviceInterface.DIGITAL);
	 */

	/**
	 * Handles a device-registration request. Rejects devices whose
	 * {@link DeviceInterface} is not {@code DIGITAL_OUT}; otherwise
	 * lazily initialises the underlying driver and replies with a
	 * fresh {@link DeviceHandle}.
	 */
	public void handleRegisterIoTDeviceRequest(RegisterIoTDeviceRequest req, short protocolId) {

		if (!req.getDeviceType().getDeviceInterface().equals(DeviceInterface.DIGITAL_OUT)) {
			sendReply(
					new RegisterIoTDeviceReply(req.getDeviceType(), 
							req.getDeviceAlias(), ErrorCode.INVALID_INTERFACE,
							"Device interface is invalid, this protocol " + 
					"operates on Digital input devices"),
					protocolId);
			return;
		}

		DeviceHandle handle = null;

		switch (req.getDeviceType()) {
		case GROVE_CHAINABLE_RGB:
			if (this.rgb == null) { // initialize device
				try {
					this.rgb = new GroveChainableRGB(pi4j, req.getDeviceAlias(), 
							req.getLine(), req.getId(), numLeds);
				} catch (Exception pe) { // TODO better exception handling
					logger.error("Could not initialize the Chainable RGB LEDs: ", pe);
					sendReply(new RegisterIoTDeviceReply(req.getDeviceType(), 
							req.getDeviceAlias(),
							ErrorCode.DEVICE_INIT_ERR, "Pi4JException: " 
					+ pe.getMessage()), protocolId);
					this.rgb = null;
				}

				if (this.rgb == null)
					return;

				// Assign an ID to the initialized Device
				short id = (short) ids.incrementAndGet();
				this.deviceIds.put(this.rgb, id);
				this.deviceIdsMapping.put(id, this.rgb);
			}

			if (this.rgb == null)
				return;

			// Device is initialized register Handle
			handle = new DeviceHandle(DeviceType.GROVE_CHAINABLE_RGB, protocolId,
					Babel.getInstance().getProtoNameById(protocolId), 
					this.deviceIds.get(this.rgb),
					req.getDeviceAlias());

			this.deviceMappings.put(handle, rgb);
			if (!this.deviceHandles.containsKey(rgb))
				this.deviceHandles.put(rgb, new HashSet<DeviceHandle>());
			this.deviceHandles.get(rgb).add(handle);

			sendReply(new RegisterIoTDeviceReply(handle), protocolId);

			break;
		default:
			sendReply(new RegisterIoTDeviceReply(req.getDeviceType(), 
					req.getDeviceAlias(), ErrorCode.UNKNOWN_DEVICE,
					"Unknown DeviceType"), protocolId);
		}
	}

	/** Drives a single pixel of the chainable RGB strip from an 8-bit RGB triplet. */
	public void handleChainableLEDColorRGBRequest(SetChainableLEDColorRGBRequest req, short protocolId) {
		logger.debug("Received a SetChainableLEDColorRGBRequest");
		DeviceHandle h = req.getDeviceHandle();

		Device d = this.deviceIdsMapping.get(h.getDeviceID());
		if (d instanceof GroveChainableRGB && this.rgb != null) {

			this.rgb.setColorRGB(req.getIdx(), req.getRed(), 
					req.getGreen(), req.getBlue());
		}
	}

	/** Drives a single pixel of the chainable RGB strip from HSB coordinates. */
	public void handleChainableLEDColorHSBRequest(SetChainableLEDColorHSBRequest req, short protocolId) {
		logger.debug("Received a SetChainableLEDColorHSBRequest");
		DeviceHandle h = req.getDeviceHandle();

		Device d = this.deviceIdsMapping.get(h.getDeviceID());
		if (d instanceof GroveChainableRGB && this.rgb != null) {

			this.rgb.setColorHSB(req.getIdx(), req.getHue(), 
					req.getSaturation(), req.getBrightness());
		}
	}

	/** Drives several pixels of the chainable RGB strip in one shot, each from an 8-bit RGB triplet. */
	public void handleMultipleChainableLEDColorRGBRequest(SetMultipleChainableLEDColorRGBRequest req,
			short protocolId) {
		logger.debug("Received a SetChainableLEDColorRGBRequest");
		DeviceHandle h = req.getDeviceHandle();

		Device d = this.deviceIdsMapping.get(h.getDeviceID());
		if (d instanceof GroveChainableRGB && this.rgb != null) {

			Iterator<Byte> ite = req.getPositionsIterator();

			while (ite.hasNext()) {
				byte pos = ite.next();
				this.rgb.setColorRGB(pos, req.getPositionRed(pos), 
						req.getPositionGreen(pos), req.getPositionBlue(pos));
			}

		}
	}

	/** Drives several pixels of the chainable RGB strip in one shot, each from HSB coordinates. */
	public void handleMultipleChainableLEDColorHSBRequest(SetMultipleChainableLEDColorHSBRequest req,
			short protocolId) {
		logger.debug("Received a SetChainableLEDColorHSBRequest");
		DeviceHandle h = req.getDeviceHandle();

		Device d = this.deviceIdsMapping.get(h.getDeviceID());
		if (d instanceof GroveChainableRGB && this.rgb != null) {

			Iterator<Byte> ite = req.getPositionsIterator();

			while (ite.hasNext()) {
				byte pos = ite.next();
				this.rgb.setColorHSB(pos, req.getPositionHue(pos), 
						req.getPositionSaturation(pos), req.getPositionBrightness(pos));
			}
		}
	}

	/** Currently a stub — handler not yet implemented. */
	public void handleUnregisterIoTDeviceRequest(UnregisterIoTDeviceRequest req, short protocolId) {
	}

}
