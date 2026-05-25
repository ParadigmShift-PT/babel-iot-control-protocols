package pt.unl.fct.di.tardis.babel.iot.controlprotocols;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.exception.Pi4JException;
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
import pt.unl.fct.di.novasys.iot.device.digital.GroveEncoder;
import pt.unl.fct.di.novasys.iot.device.digital.GroveUltrasonicRanger;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceInterface;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceType;
import pt.unl.fct.di.tardis.babel.iot.api.replies.ErrorCode;
import pt.unl.fct.di.tardis.babel.iot.api.replies.RegisterIoTDeviceReply;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTPeriodicEventRequest;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTReactiveEventRequest;
import pt.unl.fct.di.tardis.babel.iot.api.requests.RegisterIoTDeviceRequest;
import pt.unl.fct.di.tardis.babel.iot.api.requests.UnregisterIoTDeviceRequest;
import pt.unl.fct.di.tardis.babel.iot.controlprotocols.listeners.EncoderListener;
import pt.unl.fct.di.tardis.babel.iot.controlprotocols.listeners.IoTMonitoringService;
import pt.unl.fct.di.tardis.babel.iot.controlprotocols.notifications.EncoderNotification;
import pt.unl.fct.di.tardis.babel.iot.controlprotocols.replies.EncoderInputReply;
import pt.unl.fct.di.tardis.babel.iot.controlprotocols.replies.UltrasonicRangerInputReply;
import pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.input.GetReactiveEncoderRequest;
import pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.input.GetUltrasonicRangerMeasurementRequest;

/**
 * Babel protocol that drives Grove digital-input sensors attached to a
 * Raspberry Pi (currently the ultrasonic ranger and the rotary encoder).
 * <p>
 * The protocol owns a Pi4J {@link Context} configured with the standard
 * digital-input / digital-output / I²C providers, and maintains the
 * mapping between
 * {@link pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle} values and
 * the underlying device drivers. Reactive encoder requests are
 * delegated to {@link IoTMonitoringService}, which fires
 * {@code EncoderNotification}s whenever the configured threshold
 * accepts an observed rotation.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class DigitalInputControlProtocol extends GenericProtocol {

    /** Babel protocol name reported to the runtime. */
    public final static String PROTOCOL_NAME = "DigitalInputControlProtocol";
    /** Babel protocol id used to address this protocol. <b>ID:</b> {@value}. */
    public final static short PROTOCOL_ID = 2200;

    private static final Logger logger =
        LogManager.getLogger(DigitalInputControlProtocol.class);

    private final Context pi4j;

    private GroveUltrasonicRanger ranger;
    private GroveEncoder encoder;

    private final IoTMonitoringService monitor;

    private final AtomicInteger ids;
    private final HashMap<Device, Short> deviceIds;
    private final HashMap<Short, Device> deviceIdsMapping;
    private final HashMap<DeviceHandle, Device> deviceMappings;
    private final HashMap<Device, Set<DeviceHandle>> deviceHandles;

    /**
     * Builds a fresh protocol instance, including a dedicated Pi4J
     * context. Created devices and handles are tracked per-instance.
     */
    public DigitalInputControlProtocol() {
        super(PROTOCOL_NAME, PROTOCOL_ID);

        final PiGpio piGpio = PiGpio.newNativeInstance();

        pi4j = Pi4J.newContextBuilder()
                   .noAutoDetect()
                   .add(new RaspberryPiPlatform() {
                       @Override
                       protected String[] getProviders() {
                           return new String[] {};
                       }
                   })
                   .add(PiGpioDigitalInputProvider.newInstance(piGpio),
                        PiGpioDigitalOutputProvider.newInstance(piGpio),
                        PiGpioPwmProvider.newInstance(piGpio),
                        PiGpioSerialProvider.newInstance(piGpio),
                        PiGpioSpiProvider.newInstance(piGpio),
                        GpioDDigitalInputProvider.newInstance(),
                        GpioDDigitalOutputProvider.newInstance(),
                        LinuxFsI2CProvider.newInstance())
                   .build();

        this.monitor = IoTMonitoringService.getInstance();

        this.ids = new AtomicInteger(0);
        this.deviceIds = new HashMap<Device, Short>();
        this.deviceIdsMapping = new HashMap<Short, Device>();
        this.deviceMappings = new HashMap<DeviceHandle, Device>();
        this.deviceHandles = new HashMap<Device, Set<DeviceHandle>>();
    }

    /**
     * Registers handlers for the Babel requests this protocol consumes:
     * device registration / unregistration plus the reactive encoder
     * request. {@code props} is currently unused.
     */
    @Override
    public void init(Properties props)
        throws HandlerRegistrationException, IOException {
        registerRequestHandler(RegisterIoTDeviceRequest.REQUEST_ID,
                               this::handleRegisterIoTDeviceRequest);
        registerRequestHandler(UnregisterIoTDeviceRequest.REQUEST_ID,
                               this::handleUnregisterIoTDeviceRequest);

        registerRequestHandler(GetReactiveEncoderRequest.REQUEST_ID,
                               this::handleReactiveEncoderInputRequest);
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
     * {@link DeviceInterface} is not {@code DIGITAL_IN}; otherwise
     * lazily initialises the underlying driver and replies with a
     * fresh {@link DeviceHandle}.
     */
    public void handleRegisterIoTDeviceRequest(RegisterIoTDeviceRequest req,
                                               short protocolId) {

        if (!req.getDeviceType().getDeviceInterface().equals(
                DeviceInterface.DIGITAL_IN)) {
            sendReply(new RegisterIoTDeviceReply(
                          req.getDeviceType(), req.getDeviceAlias(),
                          ErrorCode.INVALID_INTERFACE,
                          "Device interface is invalid, this protocol "
                              + "operates on Digital input devices"),
                      protocolId);
            return;
        }

        DeviceHandle handle = null;

        switch (req.getDeviceType()) {
            
        case GROVE_ULTRASONIC_RANGER:
            if (this.ranger == null) { // initialize device
                try {
                    this.ranger = new GroveUltrasonicRanger(
                        pi4j, req.getDeviceAlias(), req.getLine(), req.getId());
                } catch (Pi4JException pe) { // TODO better exception handling
                    logger.error("Could not initialize the Ultrasonic Ranger: ",
                                 pe);
                    sendReply(new RegisterIoTDeviceReply(
                                  req.getDeviceType(), req.getDeviceAlias(),
                                  ErrorCode.DEVICE_INIT_ERR,
                                  "Pi4JException: " + pe.getMessage()),
                              protocolId);
                    this.ranger = null;
                }

                if (this.ranger == null)
                    return;

                // Assign an ID to the initialized Device
                short id = (short)ids.incrementAndGet();
                this.deviceIds.put(this.ranger, id);
                this.deviceIdsMapping.put(id, this.ranger);
            }

            if (this.ranger == null)
                return;

            // Device is initialized register Handle
            handle = new DeviceHandle(
                DeviceType.GROVE_ULTRASONIC_RANGER, protocolId,
                Babel.getInstance().getProtoNameById(protocolId),
                this.deviceIds.get(this.ranger), req.getDeviceAlias());

            this.deviceMappings.put(handle, ranger);
            if (!this.deviceHandles.containsKey(ranger))
                this.deviceHandles.put(ranger, new HashSet<DeviceHandle>());
            this.deviceHandles.get(ranger).add(handle);

            sendReply(new RegisterIoTDeviceReply(handle), protocolId);

            break;
        default:
            sendReply(new RegisterIoTDeviceReply(
                          req.getDeviceType(), req.getDeviceAlias(),
                          ErrorCode.UNKNOWN_DEVICE, "Unknown DeviceType"),
                      protocolId);
        }
    }

    private void registerUltrasonicRanger(RegisterIoTDeviceRequest req,
                                          short protocolId) {
        if (this.ranger == null) { // initialize device
            try {
                this.ranger = new GroveUltrasonicRanger(
                    pi4j, req.getDeviceAlias(), req.getLine(), req.getId());
            } catch (Pi4JException pe) { // TODO better exception handling
                logger.error("Could not initialize the Ultrasonic Ranger: ",
                             pe);
                sendReply(new RegisterIoTDeviceReply(
                              req.getDeviceType(), req.getDeviceAlias(),
                              ErrorCode.DEVICE_INIT_ERR,
                              "Pi4JException: " + pe.getMessage()),
                          protocolId);
                this.ranger = null;
            }

            if (this.ranger == null)
                return;

            // Assign an ID to the initialized Device
            short id = (short)ids.incrementAndGet();
            this.deviceIds.put(this.ranger, id);
            this.deviceIdsMapping.put(id, this.ranger);
        }

        if (this.ranger == null)
            return;

        // Device is initialized register Handle
        DeviceHandle handle = new DeviceHandle(
            DeviceType.GROVE_ULTRASONIC_RANGER, protocolId,
            Babel.getInstance().getProtoNameById(protocolId),
            this.deviceIds.get(this.ranger), req.getDeviceAlias());

        this.deviceMappings.put(handle, ranger);
        if (!this.deviceHandles.containsKey(ranger))
            this.deviceHandles.put(ranger, new HashSet<DeviceHandle>());
        this.deviceHandles.get(ranger).add(handle);

        sendReply(new RegisterIoTDeviceReply(handle), protocolId);
    }

    private void registerEncoder(RegisterIoTDeviceRequest req,
                                          short protocolId) {
        if (this.encoder == null) { // initialize device
            try {
                this.encoder = new GroveEncoder(
                    pi4j, req.getDeviceAlias(), req.getLine(), req.getId());
            } catch (Pi4JException pe) { // TODO better exception handling
                logger.error("Could not initialize the Ultrasonic Ranger: ",
                             pe);
                sendReply(new RegisterIoTDeviceReply(
                              req.getDeviceType(), req.getDeviceAlias(),
                              ErrorCode.DEVICE_INIT_ERR,
                              "Pi4JException: " + pe.getMessage()),
                          protocolId);
                this.encoder = null;
            }

            if (this.encoder == null)
                return;

            // Assign an ID to the initialized Device
            short id = (short)ids.incrementAndGet();
            this.deviceIds.put(this.encoder, id);
            this.deviceIdsMapping.put(id, this.encoder);
        }

        if (this.encoder == null)
            return;

        // Device is initialized register Handle
        DeviceHandle handle = new DeviceHandle(
            DeviceType.GROVE_ENCODER, protocolId,
            Babel.getInstance().getProtoNameById(protocolId),
            this.deviceIds.get(this.encoder), req.getDeviceAlias());

        this.deviceMappings.put(handle, encoder);
        if (!this.deviceHandles.containsKey(encoder))
            this.deviceHandles.put(encoder, new HashSet<DeviceHandle>());
        this.deviceHandles.get(encoder).add(handle);

        sendReply(new RegisterIoTDeviceReply(handle), protocolId);
    }

    /** Currently a stub — handler not yet implemented. */
    public void handleUnregisterIoTDeviceRequest(UnregisterIoTDeviceRequest req,
                                                 short protocolId) {}

    /**
     * Schedules an {@link EncoderListener} on the
     * {@link IoTMonitoringService} to fire an
     * {@link EncoderNotification} whenever the supplied threshold
     * accepts the observed rotation.
     */
    public void handleReactiveEncoderInputRequest(GetReactiveEncoderRequest req,
                                                  short protocolId) {
        logger.debug("Received a GetReactiveGestureRequest");
        DeviceHandle h = req.getDeviceHandle();

        Device d = this.deviceIdsMapping.get(h.getDeviceID());
        if (d instanceof GroveEncoder && this.encoder != null) {
            EncoderListener el = new EncoderListener(
                encoder, req.getThreshold(), (detectedRotation) -> {
                    triggerNotification(
                        new EncoderNotification(h, detectedRotation));
                });

            monitor.registerIoTListener(el);

        } else {
            sendReply(
                new EncoderInputReply(req.getDeviceHandle().getDeviceType(),
                                      req.getDeviceHandle().getDeviceAlias(),
                                      ErrorCode.DEVICE_NOT_AVAILABLE,
                                      "GroveEncoder is not available "
                                          + "to deteect activity"),
                protocolId);

            return;
        }
    }

    /**
     * Samples the registered {@code GroveUltrasonicRanger} in the unit
     * carried by the request and replies with the result. Replies
     * {@code DEVICE_NOT_AVAILABLE} if the ranger is not present and
     * {@code FAILED_MEASUREMENT} for unsupported units.
     */
    public void
    handleRangerMeasurmentRequest(GetUltrasonicRangerMeasurementRequest req,
                                  short protocolId) {
        logger.debug("Received an GetUltrasonicRangerMeasurementRequest");
        DeviceHandle h = req.getDeviceHandle();

        Device d = this.deviceIdsMapping.get(h.getDeviceID());
        if (d instanceof GroveUltrasonicRanger && this.ranger != null) {

            switch (req.getInputType()) {
            case CENTIMETERS:
                sendReply(new UltrasonicRangerInputReply(
                              h, req.getInputType(),
                              this.ranger.measureInCentimeters()),
                          protocolId);
                break;
            case MILLIMETERS:
                sendReply(new UltrasonicRangerInputReply(
                              h, req.getInputType(),
                              this.ranger.measureInMillimeters()),
                          protocolId);
                break;
            case INCHES:
                sendReply(
                    new UltrasonicRangerInputReply(
                        h, req.getInputType(), this.ranger.measureInInches()),
                    protocolId);
                break;
            default:
                sendReply(
                    new UltrasonicRangerInputReply(
                        req.getDeviceHandle().getDeviceType(),
                        req.getDeviceHandle().getDeviceAlias(),
                        ErrorCode.FAILED_MEASUREMENT,
                        "Unsupported measurement on GroveUltrasonicRanger"),
                    protocolId);
                break;
            }
        } else {
            sendReply(new UltrasonicRangerInputReply(
                          req.getDeviceHandle().getDeviceType(),
                          req.getDeviceHandle().getDeviceAlias(),
                          ErrorCode.DEVICE_NOT_AVAILABLE,
                          "GroveUltrasonicRanger is not available to take "
                              + "measurements"),
                      protocolId);
        }
    }

    /** Generic periodic-input handler — currently a stub. */
    public void handleIoTPeriodicInputRequest(IoTPeriodicEventRequest req,
                                              short protocolId) {}

    /** Generic reactive-input handler — currently a stub. */
    public void handleIoTReactiveInputRequest(IoTReactiveEventRequest<?> req,
                                              short protocolId) {}
}
