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
import pt.unl.fct.di.novasys.iot.device.i2c.GroveLcd;
import pt.unl.fct.di.novasys.iot.device.i2c.GroveLedMatrix;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceInterface;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceType;
import pt.unl.fct.di.tardis.babel.iot.api.replies.ErrorCode;
import pt.unl.fct.di.tardis.babel.iot.api.replies.RegisterIoTDeviceReply;
import pt.unl.fct.di.tardis.babel.iot.api.requests.RegisterIoTDeviceRequest;
import pt.unl.fct.di.tardis.babel.iot.api.requests.UnregisterIoTDeviceRequest;
import pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output.ClearDisplayRequest;
import pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output.SetDisplayColorRequest;
import pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output.ShowAnimationRequest;
import pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output.DisplayBarRequest;
import pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output.ShowDisplayRequest;
import pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output.ShowEmojiRequest;
import pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output.ShowTextRequest;
import pt.unl.fct.di.tardis.babel.iot.controlprotocols.utils.I2CScanner;

/**
 * Babel protocol that drives Grove I²C-output actuators attached to a
 * Raspberry Pi (currently the LCD and the LED matrix).
 * <p>
 * Each registration consults the {@link I2CScanner} singleton to
 * confirm the device is physically present on the bus before the
 * driver is instantiated. Concrete display payloads are carried by
 * the request types under
 * {@code controlprotocols.requests.output} (clear / show animation /
 * show emoji / show text / display bar / show display / set colour).
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class I2COutputControlProtocol extends GenericProtocol {

    /** Babel protocol name reported to the runtime. */
    public final static String PROTOCOL_NAME = "I2COutputControlProtocol";
    /** Babel protocol id used to address this protocol. <b>ID:</b> {@value}. */
    public final static short PROTOCOL_ID = 2000;

    private static final Logger logger =
        LogManager.getLogger(I2COutputControlProtocol.class);

    private final Context pi4j;
    private GroveLedMatrix matrix;

    private GroveLcd lcd;

    private final I2CScanner scanner;

    private final AtomicInteger ids;
    private final HashMap<Device, Short> deviceIds;
    private final HashMap<Short, Device> deviceIdsMapping;
    private final HashMap<DeviceHandle, Device> deviceMappings;
    private final HashMap<Device, Set<DeviceHandle>> deviceHandles;

    /**
     * Builds a fresh protocol instance, including a dedicated Pi4J
     * context plus a reference to the shared {@link I2CScanner}
     * singleton.
     */
    public I2COutputControlProtocol() {
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

        this.ids = new AtomicInteger(0);
        this.deviceIds = new HashMap<Device, Short>();
        this.deviceIdsMapping = new HashMap<Short, Device>();
        this.deviceMappings = new HashMap<DeviceHandle, Device>();
        this.deviceHandles = new HashMap<Device, Set<DeviceHandle>>();
    }

    /**
     * Registers handlers for device registration / unregistration plus
     * the seven supported output operations (set colour, show
     * animation, show emoji, show text, show display, display bar,
     * clear display). {@code props} is currently unused.
     */
    @Override
    public void init(Properties props)
        throws HandlerRegistrationException, IOException {
        registerRequestHandler(RegisterIoTDeviceRequest.REQUEST_ID,
                               this::handleRegisterIoTDeviceRequest);
        registerRequestHandler(UnregisterIoTDeviceRequest.REQUEST_ID,
                               this::handleUnregisterIoTDeviceRequest);
        registerRequestHandler(SetDisplayColorRequest.REQUEST_ID,
                               this::handleSetDisplayColorRequest);
        registerRequestHandler(ShowAnimationRequest.REQUEST_ID,
                               this::handleShowAnimationRequest);
        registerRequestHandler(ShowEmojiRequest.REQUEST_ID,
                               this::handleShowEmojiRequest);
        registerRequestHandler(ShowTextRequest.REQUEST_ID,
                               this::handleShowTextRequest);
        registerRequestHandler(ShowDisplayRequest.REQUEST_ID, 
        					   this::handleShowDisplayRequest);
        registerRequestHandler(DisplayBarRequest.REQUEST_ID, 
        					   this::handleDisplayBarRequest);
        registerRequestHandler(ClearDisplayRequest.REQUEST_ID,
        					   this::handleClearDisplayRequest);
    }

    /*******************************************************************************
     * handles for Protoocol Requests
     ******************************************************************************/

    /**
     * Handles a device-registration request. Rejects devices whose
     * {@link DeviceInterface} is not {@code I2C_OUT}, devices not
     * detected on the I²C bus, and unknown {@code DeviceType}s;
     * otherwise lazily initialises the underlying driver and replies
     * with a fresh {@link DeviceHandle}.
     */
    public void handleRegisterIoTDeviceRequest(RegisterIoTDeviceRequest req,
                                               short protocolId) {

        if (!req.getDeviceType().getDeviceInterface().equals(
                DeviceInterface.I2C_OUT)) {
            sendReply(new RegisterIoTDeviceReply(
                          req.getDeviceType(), req.getDeviceAlias(),
                          ErrorCode.INVALID_INTERFACE,
                          "Device interface is invalid, this protocol "
                              + "operates on I2C output devices"),
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
        case GROVE_LCD:
            if (this.lcd == null) { // initialize device
                try {
                    this.lcd = new GroveLcd(pi4j);
                } catch (IOException e) {
                    logger.error("Could not initialize the LCD: ", e);
                    sendReply(new RegisterIoTDeviceReply(
                                  req.getDeviceType(), req.getDeviceAlias(),
                                  ErrorCode.DEVICE_INIT_ERR,
                                  "IOException: " + e.getMessage()),
                              protocolId);
                    this.lcd = null;
                } catch (Exception pe) { // TODO better exception handling
                    logger.error("Could not initialize the LCD: ", pe);
                    sendReply(new RegisterIoTDeviceReply(
                                  req.getDeviceType(), req.getDeviceAlias(),
                                  ErrorCode.DEVICE_INIT_ERR,
                                  "Pi4JException: " + pe.getMessage()),
                              protocolId);
                    this.lcd = null;
                }

                if (this.lcd == null)
                    return;

                // Assign an ID to the initialized Device
                short id = (short)ids.incrementAndGet();
                this.deviceIds.put(this.lcd, id);
                this.deviceIdsMapping.put(id, this.lcd);

                // Initialize support variables
                this.lcd.setText(
                    "Device initialized."); // TODO: Do we want to do this?
            }

            if (this.lcd == null)
                return;

            // Device is initialized register Handle
            handle = new DeviceHandle(
                DeviceType.GROVE_LCD, protocolId,
                Babel.getInstance().getProtoNameById(protocolId),
                this.deviceIds.get(this.lcd), req.getDeviceAlias());

            this.deviceMappings.put(handle, lcd);
            if (!this.deviceHandles.containsKey(lcd))
                this.deviceHandles.put(lcd, new HashSet<DeviceHandle>());
            this.deviceHandles.get(lcd).add(handle);

            sendReply(new RegisterIoTDeviceReply(handle), protocolId);

            break;
        case GROVE_LED_MATRIX:
            if (this.matrix == null) { // initialize device
                try {
                    this.matrix = new GroveLedMatrix(pi4j);
                } catch (IOException e) {
                    logger.error("Could not initialize the LedMatrix: ", e);
                    sendReply(new RegisterIoTDeviceReply(
                                  req.getDeviceType(), req.getDeviceAlias(),
                                  ErrorCode.DEVICE_INIT_ERR,
                                  "IOException: " + e.getMessage()),
                              protocolId);
                    this.matrix = null;
                } catch (Exception pe) { // TODO better exception handling
                    logger.error("Could not initialize the LedMatrix: ", pe);
                    sendReply(new RegisterIoTDeviceReply(
                                  req.getDeviceType(), req.getDeviceAlias(),
                                  ErrorCode.DEVICE_INIT_ERR,
                                  "Pi4JException: " + pe.getMessage()),
                              protocolId);
                    this.matrix = null;
                }

                if (this.matrix == null)
                    return;

                // Assign an ID to the initialized Device
                short id = (short)ids.incrementAndGet();
                this.deviceIds.put(this.matrix, id);
                this.deviceIdsMapping.put(id, this.matrix);

                // Initialize support variables
                this.matrix.clearDisplay();
                this.matrix.setAllColor((byte)255, (byte)0,
                                        (byte)0); // TODO: Do we want this here?
            }

            if (this.matrix == null)
                return;

            // Device is initialized register Handle
            handle = new DeviceHandle(
                DeviceType.GROVE_LED_MATRIX, protocolId,
                Babel.getInstance().getProtoNameById(protocolId),
                this.deviceIds.get(this.matrix), req.getDeviceAlias());

            this.deviceMappings.put(handle, matrix);
            if (!this.deviceHandles.containsKey(matrix))
                this.deviceHandles.put(matrix, new HashSet<DeviceHandle>());
            this.deviceHandles.get(matrix).add(handle);

            sendReply(new RegisterIoTDeviceReply(handle), protocolId);

            break;
        default:
            sendReply(new RegisterIoTDeviceReply(
                          req.getDeviceType(), req.getDeviceAlias(),
                          ErrorCode.UNKNOWN_DEVICE, "Unknown DeviceType"),
                      protocolId);
        }
    }

    /** Currently a stub — handler not yet implemented. */
    public void handleUnregisterIoTDeviceRequest(UnregisterIoTDeviceRequest req,
                                                 short protocolId) {}

    /** Sets the global colour of the registered LED matrix. */
    public void handleSetDisplayColorRequest(SetDisplayColorRequest req,
                                             short protocolId) {
        logger.debug("Received a SetDisplayColorRequest");
        DeviceHandle h = req.getDeviceHandle();

        Device d = this.deviceIdsMapping.get(h.getDeviceID());
        if (d instanceof GroveLedMatrix && this.matrix != null) {

            this.matrix.setAllColor((byte)req.getRed(), (byte)req.getGreen(),
                                    (byte)req.getBlue());
        }
    }

    /** Drives the LED matrix as a level bar at the request's level (0–32). */
    public void handleDisplayBarRequest(DisplayBarRequest req,
    									  short protocolId) {
    	logger.debug("Received a DisplayBarRequest");
        DeviceHandle h = req.getDeviceHandle();

        Device d = this.deviceIdsMapping.get(h.getDeviceID());
        if (d instanceof GroveLedMatrix && this.matrix != null) {

            this.matrix.displayColorBar(req.getLevel());
        }
    }

    /** Clears the LED matrix. */
    public void handleClearDisplayRequest(ClearDisplayRequest req,
    									  short protocolId) {
    	logger.debug("Received a ClearDisplayRequest");
        DeviceHandle h = req.getDeviceHandle();

        Device d = this.deviceIdsMapping.get(h.getDeviceID());
        if (d instanceof GroveLedMatrix && this.matrix != null) {

            this.matrix.clearDisplay();
        }
    }
    
    /** Loads a raw bitmap onto the LED matrix. */
    public void handleShowDisplayRequest(ShowDisplayRequest req,
                                         short protocolId) {
        logger.debug("Received a ShowDisplayRequest");
        DeviceHandle h = req.getDeviceHandle();

        Device d = this.deviceIdsMapping.get(h.getDeviceID());
        if (d instanceof GroveLedMatrix && this.matrix != null) {

            this.matrix.loadSnapshot(req.getDisplay());
        }
    }

    /** Plays one of the LED matrix's built-in animations. */
    public void handleShowAnimationRequest(ShowAnimationRequest req,
                                           short protocolId) {
        logger.debug("Received a ShowAnimationRequest");
        DeviceHandle h = req.getDeviceHandle();

        Device d = this.deviceIdsMapping.get(h.getDeviceID());
        if (d instanceof GroveLedMatrix && this.matrix != null) {

            this.matrix.displayColorAnimation(req.getAnimation());
        }
    }

    /** Renders one of the LED matrix's built-in emoji. */
    public void handleShowEmojiRequest(ShowEmojiRequest req, short protocolId) {
        logger.debug("Received a ShowEmojiRequest");
        DeviceHandle h = req.getDeviceHandle();

        Device d = this.deviceIdsMapping.get(h.getDeviceID());
        if (d instanceof GroveLedMatrix && this.matrix != null) {

            this.matrix.displayEmoji(req.getEmoji());
        }
    }

    /** Writes the request's text onto the registered LCD. */
    public void handleShowTextRequest(ShowTextRequest req, short protocolId) {
        logger.debug("Received a ShowTextRequest");
        DeviceHandle h = req.getDeviceHandle();

        Device d = this.deviceIdsMapping.get(h.getDeviceID());
        if (d instanceof GroveLcd && this.lcd != null) {

            this.lcd.setText(req.getText());
        }
    }
}
