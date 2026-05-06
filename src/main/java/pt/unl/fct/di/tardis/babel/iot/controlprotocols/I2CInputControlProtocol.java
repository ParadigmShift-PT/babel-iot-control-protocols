package pt.unl.fct.di.tardis.babel.iot.controlprotocols;

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
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.unl.fct.di.novasys.babel.core.Babel;
import pt.unl.fct.di.novasys.babel.core.GenericProtocol;
import pt.unl.fct.di.novasys.babel.exceptions.HandlerRegistrationException;
import pt.unl.fct.di.novasys.iot.device.Device;
import pt.unl.fct.di.novasys.iot.device.i2c.Grove3AxisAccelerometer;
import pt.unl.fct.di.novasys.iot.device.i2c.GroveBarometer;
import pt.unl.fct.di.novasys.iot.device.i2c.Grove3AxisAccelerometer.AccelData;
import pt.unl.fct.di.novasys.iot.device.i2c.GroveGestureDetector;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceInterface;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceType;
import pt.unl.fct.di.tardis.babel.iot.api.replies.ErrorCode;
import pt.unl.fct.di.tardis.babel.iot.api.replies.RegisterIoTDeviceReply;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTPeriodicEventRequest;
import pt.unl.fct.di.tardis.babel.iot.api.requests.RegisterIoTDeviceRequest;
import pt.unl.fct.di.tardis.babel.iot.api.requests.UnregisterIoTDeviceRequest;
import pt.unl.fct.di.tardis.babel.iot.controlprotocols.listeners.GestureListener;
import pt.unl.fct.di.tardis.babel.iot.controlprotocols.listeners.IoTMonitoringService;
import pt.unl.fct.di.tardis.babel.iot.controlprotocols.notifications.GestureNotification;
import pt.unl.fct.di.tardis.babel.iot.controlprotocols.replies.AccelerometerInputReply;
import pt.unl.fct.di.tardis.babel.iot.controlprotocols.replies.GestureInputReply;
import pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.input.GetAccelerometerDataRequest;
import pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.input.GetGestureRequest;
import pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.input.GetReactiveGestureRequest;
import pt.unl.fct.di.tardis.babel.iot.controlprotocols.utils.I2CScanner;

/**
 * Babel protocol that drives Grove I²C-input sensors attached to a
 * Raspberry Pi (gesture detector, three-axis accelerometer; barometer
 * support is wired but not yet exposed through the registration
 * switch).
 * <p>
 * Each registration consults the {@link I2CScanner} singleton to
 * confirm the device is physically present on the bus before the
 * driver is instantiated. Reactive gesture requests are delegated to
 * {@link IoTMonitoringService}, which fires
 * {@code GestureNotification}s whenever the configured threshold
 * accepts a detected gesture.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class I2CInputControlProtocol extends GenericProtocol {

    /** Babel protocol name reported to the runtime. */
    public final static String PROTOCOL_NAME = "I2CInputControlProtocol";
    /** Babel protocol id used to address this protocol. */
    public final static short PROTOCOL_ID = 4001;

    private static final Logger logger =
        LogManager.getLogger(I2CInputControlProtocol.class);

    private final Context pi4j;

    private Grove3AxisAccelerometer accelerometer;
    private GroveGestureDetector gesture;
    private GroveBarometer barometer;

    private final I2CScanner scanner;
    private final IoTMonitoringService monitor;

    private final AtomicInteger ids;
    private final HashMap<Device, Short> deviceIds;
    private final HashMap<Short, Device> deviceIdsMapping;
    private final HashMap<DeviceHandle, Device> deviceMappings;
    private final HashMap<Device, Set<DeviceHandle>> deviceHandles;

    /**
     * Builds a fresh protocol instance, including a dedicated Pi4J
     * context plus references to the shared {@link I2CScanner} and
     * {@link IoTMonitoringService} singletons.
     */
    public I2CInputControlProtocol() {
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

        this.scanner = I2CScanner.getInstance();
        this.monitor = IoTMonitoringService.getInstance();

        this.ids = new AtomicInteger(0);
        this.deviceIds = new HashMap<Device, Short>();
        this.deviceIdsMapping = new HashMap<Short, Device>();
        this.deviceMappings = new HashMap<DeviceHandle, Device>();
        this.deviceHandles = new HashMap<Device, Set<DeviceHandle>>();
    }

    /**
     * Registers handlers for device registration / unregistration,
     * the immediate gesture and accelerometer reads, and the reactive
     * gesture watcher. {@code props} is currently unused.
     */
    @Override
    public void init(Properties props)
        throws HandlerRegistrationException, IOException {
        registerRequestHandler(RegisterIoTDeviceRequest.REQUEST_ID,
                               this::handleRegisterIoTDeviceRequest);
        registerRequestHandler(UnregisterIoTDeviceRequest.REQUEST_ID,
                               this::handleUnregisterIoTDeviceRequest);

        // immediate
        registerRequestHandler(GetGestureRequest.REQUEST_ID,
                               this::handleGestureInputRequest);
        registerRequestHandler(GetAccelerometerDataRequest.REQUEST_ID,
                               this::handleAccelerometerInputRequest);

        // reactive
        registerRequestHandler(GetReactiveGestureRequest.REQUEST_ID,
                               this::handleReactiveGestureInputRequest);

        // periodic
        /*
         * registerRequestHandler(IoTPeriodicInputRequest.REQUEST_ID,
         * this::handleIoTPeriodicInputRequest);
         */
    }

    /*******************************************************************************
     * handles for Protoocol Requests
     ******************************************************************************/

    /**
     * Handles a device-registration request. Rejects devices whose
     * {@link DeviceInterface} is not {@code I2C_IN}, devices not
     * detected on the I²C bus, and unknown {@code DeviceType}s;
     * otherwise lazily initialises the underlying driver and replies
     * with a fresh {@link DeviceHandle}.
     */
    public void handleRegisterIoTDeviceRequest(RegisterIoTDeviceRequest req,
                                               short protocolId) {

        if (!req.getDeviceType().getDeviceInterface().equals(
                DeviceInterface.I2C_IN)) {
            sendReply(new RegisterIoTDeviceReply(
                          req.getDeviceType(), req.getDeviceAlias(),
                          ErrorCode.INVALID_INTERFACE,
                          "Device interface is invalid, this protocol "
                              + "operates on I2C input devices"),
                      protocolId);
            return;
        }

        Set<DeviceType> devices = this.scanner.getConnectedDevices();
        if (!devices.contains(req.getDeviceType())) {
            sendReply(new RegisterIoTDeviceReply(req.getDeviceType(),
                                                 req.getDeviceAlias(),
                                                 ErrorCode.DEVICE_NOT_AVAILABLE,
                                                 "Device is not connected"),
                      protocolId);
            return;
        }

        DeviceHandle handle = null;

        switch (req.getDeviceType()) {
        case GROVE_GESTURE_DETECTOR:
            if (this.gesture == null) { // initialize device
                try {
                    this.gesture = new GroveGestureDetector(pi4j);
                } catch (IOException e) {
                    logger.error("Could not initialize the Gesture Detector: ",
                                 e);
                    sendReply(new RegisterIoTDeviceReply(
                                  req.getDeviceType(), req.getDeviceAlias(),
                                  ErrorCode.DEVICE_INIT_ERR,
                                  "IOException: " + e.getMessage()),
                              protocolId);
                    this.gesture = null;
                } catch (Exception pe) { // TODO better exception handling
                    logger.error("Could not initialize the Gesture Detector: ",
                                 pe);
                    sendReply(new RegisterIoTDeviceReply(
                                  req.getDeviceType(), req.getDeviceAlias(),
                                  ErrorCode.DEVICE_INIT_ERR,
                                  "Pi4JException: " + pe.getMessage()),
                              protocolId);
                    this.gesture = null;
                }

                if (this.gesture == null)
                    return;

                // Assign an ID to the initialized Device
                short id = (short)ids.incrementAndGet();
                this.deviceIds.put(this.gesture, id);
                this.deviceIdsMapping.put(id, this.gesture);
            }

            if (this.gesture == null)
                return;

            // Device is initialized register Handle
            handle = new DeviceHandle(
                DeviceType.GROVE_GESTURE_DETECTOR, protocolId,
                Babel.getInstance().getProtoNameById(protocolId),
                this.deviceIds.get(this.gesture), req.getDeviceAlias());

            this.deviceMappings.put(handle, gesture);
            if (!this.deviceHandles.containsKey(gesture))
                this.deviceHandles.put(gesture, new HashSet<DeviceHandle>());
            this.deviceHandles.get(gesture).add(handle);

            sendReply(new RegisterIoTDeviceReply(handle), protocolId);

            break;
        case GROVE_3AXIS_ACCELEROMETER:
            if (this.accelerometer == null) { // initialize device
                try {
                    this.accelerometer = new Grove3AxisAccelerometer(pi4j);
                } catch (IOException e) {
                    logger.error("Could not initialize the LedMatrix: ", e);
                    sendReply(new RegisterIoTDeviceReply(
                                  req.getDeviceType(), req.getDeviceAlias(),
                                  ErrorCode.DEVICE_INIT_ERR,
                                  "IOException: " + e.getMessage()),
                              protocolId);
                    this.accelerometer = null;
                } catch (Exception pe) { // TODO better exception handling
                    logger.error("Could not initialize the LedMatrix: ", pe);
                    sendReply(new RegisterIoTDeviceReply(
                                  req.getDeviceType(), req.getDeviceAlias(),
                                  ErrorCode.DEVICE_INIT_ERR,
                                  "Pi4JException: " + pe.getMessage()),
                              protocolId);
                    this.accelerometer = null;
                }

                if (this.accelerometer == null)
                    return;

                // Assign an ID to the initialized Device
                short id = (short)ids.incrementAndGet();
                this.deviceIds.put(this.accelerometer, id);
                this.deviceIdsMapping.put(id, this.accelerometer);
            }

            if (this.accelerometer == null)
                return;

            // Device is initialized register Handle
            handle = new DeviceHandle(
                DeviceType.GROVE_3AXIS_ACCELEROMETER, protocolId,
                Babel.getInstance().getProtoNameById(protocolId),
                this.deviceIds.get(this.accelerometer), req.getDeviceAlias());

            this.deviceMappings.put(handle, accelerometer);
            if (!this.deviceHandles.containsKey(accelerometer))
                this.deviceHandles.put(accelerometer,
                                       new HashSet<DeviceHandle>());
            this.deviceHandles.get(accelerometer).add(handle);

            sendReply(new RegisterIoTDeviceReply(handle), protocolId);

            break;
        default:
            sendReply(new RegisterIoTDeviceReply(
                          req.getDeviceType(), req.getDeviceAlias(),
                          ErrorCode.UNKNOWN_DEVICE, "Unknown DeviceType"),
                      protocolId);
        }
    }

    private void registerGestureDetector(RegisterIoTDeviceRequest req,
                                         short protocolId) {
        if (this.gesture == null) { // initialize device
            try {
                this.gesture = new GroveGestureDetector(pi4j);
            } catch (IOException e) {
                logger.error("Could not initialize the Gesture Detector: ", e);
                sendReply(new RegisterIoTDeviceReply(
                              req.getDeviceType(), req.getDeviceAlias(),
                              ErrorCode.DEVICE_INIT_ERR,
                              "IOException: " + e.getMessage()),
                          protocolId);
                this.gesture = null;
            } catch (Exception pe) { // TODO better exception handling
                logger.error("Could not initialize the Gesture Detector: ", pe);
                sendReply(new RegisterIoTDeviceReply(
                              req.getDeviceType(), req.getDeviceAlias(),
                              ErrorCode.DEVICE_INIT_ERR,
                              "Pi4JException: " + pe.getMessage()),
                          protocolId);
                this.gesture = null;
            }

            if (this.gesture == null)
                return;

            // Assign an ID to the initialized Device
            short id = (short)ids.incrementAndGet();
            this.deviceIds.put(this.gesture, id);
            this.deviceIdsMapping.put(id, this.gesture);
        }

        if (this.gesture == null)
            return;

        // Device is initialized register Handle
        DeviceHandle handle = new DeviceHandle(
            DeviceType.GROVE_GESTURE_DETECTOR, protocolId,
            Babel.getInstance().getProtoNameById(protocolId),
            this.deviceIds.get(this.gesture), req.getDeviceAlias());

        this.deviceMappings.put(handle, gesture);
        if (!this.deviceHandles.containsKey(gesture))
            this.deviceHandles.put(gesture, new HashSet<DeviceHandle>());
        this.deviceHandles.get(gesture).add(handle);

        sendReply(new RegisterIoTDeviceReply(handle), protocolId);
    }

    private void registerAccelerometer(RegisterIoTDeviceRequest req,
                                       short protocolId) {
        if (this.accelerometer == null) { // initialize device
            try {
                this.accelerometer = new Grove3AxisAccelerometer(pi4j);
            } catch (IOException e) {
                logger.error("Could not initialize the Accelerometer: ", e);
                sendReply(new RegisterIoTDeviceReply(
                              req.getDeviceType(), req.getDeviceAlias(),
                              ErrorCode.DEVICE_INIT_ERR,
                              "IOException: " + e.getMessage()),
                          protocolId);
                this.accelerometer = null;
            } catch (Exception pe) { // TODO better exception handling
                logger.error("Could not initialize the Accelerometer: ", pe);
                sendReply(new RegisterIoTDeviceReply(
                              req.getDeviceType(), req.getDeviceAlias(),
                              ErrorCode.DEVICE_INIT_ERR,
                              "Pi4JException: " + pe.getMessage()),
                          protocolId);
                this.accelerometer = null;
            }

            if (this.accelerometer == null)
                return;

            // Assign an ID to the initialized Device
            short id = (short)ids.incrementAndGet();
            this.deviceIds.put(this.accelerometer, id);
            this.deviceIdsMapping.put(id, this.accelerometer);
        }

        if (this.accelerometer == null)
            return;

        // Device is initialized register Handle
        DeviceHandle handle = new DeviceHandle(
            DeviceType.GROVE_3AXIS_ACCELEROMETER, protocolId,
            Babel.getInstance().getProtoNameById(protocolId),
            this.deviceIds.get(this.accelerometer), req.getDeviceAlias());

        this.deviceMappings.put(handle, accelerometer);
        if (!this.deviceHandles.containsKey(accelerometer))
            this.deviceHandles.put(accelerometer, new HashSet<DeviceHandle>());
        this.deviceHandles.get(accelerometer).add(handle);

        sendReply(new RegisterIoTDeviceReply(handle), protocolId);
    }

    private void registerBarometer(RegisterIoTDeviceRequest req,
                                       short protocolId) {
        if (this.barometer == null) { // initialize device
            try {
                this.barometer = new GroveBarometer(pi4j);
            } catch (IOException e) {
                logger.error("Could not initialize the Barometer: ", e);
                sendReply(new RegisterIoTDeviceReply(
                              req.getDeviceType(), req.getDeviceAlias(),
                              ErrorCode.DEVICE_INIT_ERR,
                              "IOException: " + e.getMessage()),
                          protocolId);
                this.barometer = null;
            } catch (Exception pe) { // TODO better exception handling
                logger.error("Could not initialize the Barometer: ", pe);
                sendReply(new RegisterIoTDeviceReply(
                              req.getDeviceType(), req.getDeviceAlias(),
                              ErrorCode.DEVICE_INIT_ERR,
                              "Pi4JException: " + pe.getMessage()),
                          protocolId);
                this.barometer = null;
            }

            if (this.barometer == null)
                return;

            // Assign an ID to the initialized Device
            short id = (short)ids.incrementAndGet();
            this.deviceIds.put(this.barometer, id);
            this.deviceIdsMapping.put(id, this.barometer);
        }

        if (this.barometer == null)
            return;

        // Device is initialized register Handle
        DeviceHandle handle = new DeviceHandle(
            DeviceType.GROVE_BAROMETER, protocolId,
            Babel.getInstance().getProtoNameById(protocolId),
            this.deviceIds.get(this.barometer), req.getDeviceAlias());

        this.deviceMappings.put(handle, barometer);
        if (!this.deviceHandles.containsKey(barometer))
            this.deviceHandles.put(barometer, new HashSet<DeviceHandle>());
        this.deviceHandles.get(barometer).add(handle);

        sendReply(new RegisterIoTDeviceReply(handle), protocolId);
    }

    /** Currently a stub — handler not yet implemented. */
    public void handleUnregisterIoTDeviceRequest(UnregisterIoTDeviceRequest req,
                                                 short protocolId) {}

    /**
     * Samples the registered Grove three-axis accelerometer in the
     * read mode carried by the request and replies with the result.
     * Replies {@code DEVICE_NOT_AVAILABLE} if the accelerometer is
     * not present and {@code FAILED_MEASUREMENT} for unsupported read
     * modes.
     */
    public void handleAccelerometerInputRequest(GetAccelerometerDataRequest req,
                                                short protocolId) {
        logger.debug("Received an GetAccelerometerDataRequest");
        DeviceHandle h = req.getDeviceHandle();

        Device d = this.deviceIdsMapping.get(h.getDeviceID());
        if (d instanceof Grove3AxisAccelerometer &&
            this.accelerometer != null) {
            switch (req.getInputType()) {
            case ACCELERATION_DATA:
                /*
                 * sendReply(new AccelerometerAccelDataReply(
                 * h, this.accelerometer.getAccelerationData()),
                 * protocolId);
                 */
                sendReply(new AccelerometerInputReply<AccelData>(
                              h, req.getInputType(),
                              this.accelerometer.getAccelerationData()),
                          protocolId);
                break;
            case ACCELERATION_SIMPLE:
                /*
                 * sendReply(new AccelerometerAccelSimpleReply(
                 * h, this.accelerometer.getAcceleration()),
                 * protocolId);
                 */
                sendReply(new AccelerometerInputReply<float[]>(
                              h, req.getInputType(),
                              this.accelerometer.getAcceleration()),
                          protocolId);
                break;
            case XYZ:
                /*
                 * sendReply(
                 * new AccelerometerXYZReply(h, this.accelerometer.getXYZ()),
                 * protocolId);
                 */
                sendReply(
                    new AccelerometerInputReply<int[]>(
                        h, req.getInputType(), this.accelerometer.getXYZ()),
                    protocolId);
                break;
            default:
                sendReply(
                    new AccelerometerInputReply<Object>(
                        req.getDeviceHandle().getDeviceType(),
                        req.getDeviceHandle().getDeviceAlias(),
                        ErrorCode.FAILED_MEASUREMENT,
                        "Unsupported measurement on Grove3AxisAccelerometer"),
                    protocolId);
                break;
            }
        } else {
            sendReply(new AccelerometerInputReply<Object>(
                          req.getDeviceHandle().getDeviceType(),
                          req.getDeviceHandle().getDeviceAlias(),
                          ErrorCode.DEVICE_NOT_AVAILABLE,
                          "Grove3AxisAccelerometer is not available to take "
                              + "measurements"),
                      protocolId);
        }
    }

    /**
     * Reads the latest gesture from the Grove gesture detector and
     * replies with a {@link GestureInputReply}. Replies
     * {@code DEVICE_NOT_AVAILABLE} if the detector is not present.
     */
    public void handleGestureInputRequest(GetGestureRequest req,
                                          short protocolId) {
        logger.debug("Received a GetGestureRequest");
        DeviceHandle h = req.getDeviceHandle();

        Device d = this.deviceIdsMapping.get(h.getDeviceID());
        if (d instanceof GroveGestureDetector && this.gesture != null) {
            sendReply(new GestureInputReply(h, gesture.getGesture()),
                      protocolId);

        } else {
            sendReply(
                new GestureInputReply(req.getDeviceHandle().getDeviceType(),
                                      req.getDeviceHandle().getDeviceAlias(),
                                      ErrorCode.DEVICE_NOT_AVAILABLE,
                                      "GroveGestureDetector is not available "
                                          + "to deteect activity"),
                protocolId);

            return;
        }
    }

    /** Generic periodic-event handler — currently a stub. */
    public void handleIoTPeriodicEventRequest(IoTPeriodicEventRequest req,
                                              short protocolId) {}

    /**
     * Schedules a {@link GestureListener} on the
     * {@link IoTMonitoringService} to fire a {@link GestureNotification}
     * whenever the supplied threshold accepts a detected gesture.
     */
    public void handleReactiveGestureInputRequest(GetReactiveGestureRequest req,
                                                  short protocolId) {
        logger.debug("Received a GetReactiveGestureRequest");
        DeviceHandle h = req.getDeviceHandle();

        Device d = this.deviceIdsMapping.get(h.getDeviceID());
        if (d instanceof GroveGestureDetector && this.gesture != null) {
            GestureListener gl = new GestureListener(
                gesture, req.getThreshold(), (detectedGesture) -> {
                    triggerNotification(
                        new GestureNotification(h, detectedGesture));
                });

            monitor.registerIoTListener(gl);

        } else {
            sendReply(
                new GestureInputReply(req.getDeviceHandle().getDeviceType(),
                                      req.getDeviceHandle().getDeviceAlias(),
                                      ErrorCode.DEVICE_NOT_AVAILABLE,
                                      "GroveGestureDetector is not available "
                                          + "to deteect activity"),
                protocolId);

            return;
        }
    }
}
