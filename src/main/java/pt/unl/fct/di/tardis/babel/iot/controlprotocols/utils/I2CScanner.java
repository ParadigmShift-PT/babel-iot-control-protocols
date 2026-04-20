package pt.unl.fct.di.tardis.babel.iot.controlprotocols.utils;

import com.github.yafna.raspberry.grovepi.pi4j.GrovePi4J;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.unl.fct.di.novasys.iot.device.i2c.Grove3AxisAccelerometer;
import pt.unl.fct.di.novasys.iot.device.i2c.GroveGestureDetector;
import pt.unl.fct.di.novasys.iot.device.i2c.GroveLcd;
import pt.unl.fct.di.novasys.iot.device.i2c.GroveLedMatrix;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceType;

public class I2CScanner {
    private static final Logger logger = LogManager.getLogger(I2CScanner.class);
    private static final int SCAN_INTERVAL_MS = 60_000; // 1 minute

    private static I2CScanner instance;
    Set<DeviceType> connectedDevices;
    private static long lastScan;

    private I2CScanner() {
        this.connectedDevices = new HashSet<>();
        I2CScanner.lastScan = 0L;
    }

    /**
     * Gets the singleton instance of the I2C scanner.
     *
     * @return The singleton {@code I2CScanner} instance
     */
    public static I2CScanner getInstance() {
        if (instance == null) {
            instance = new I2CScanner();
        }

        return instance;
    }

    /**
     * Performs a scan of the I2C bus to detect connected devices.
     * Updates the last scan timestamp.
     *
     * @return A {@code Set<DeviceType>} containing all detected I2C devices
     */
    public static Set<DeviceType> scan() {
        lastScan = System.currentTimeMillis();
        Set<DeviceType> devices = new HashSet<>();
        try {
            // could not find a programatic way to "ping" devices so...
            // immediate scan (-y) with i2cdetect
            Process proc = new ProcessBuilder("i2cdetect", "-y",
                                              String.valueOf(GrovePi4J.I2C_BUS))
                               .start();
            InputStreamReader isr =
                new InputStreamReader(proc.getInputStream());
            BufferedReader r = new BufferedReader(isr);

            String line;
            while ((line = r.readLine()) != null) {
                String[] parts;
                if ((parts = line.split(":")).length > 1) {
                    String[] addrs = parts[1].split("\\s+");
                    for (String addr : addrs) {
                        if (addr.length() == 2 && !addr.equals("--")) {
                            int device_addr = Integer.parseInt(addr, 16);
                            devices.add(getI2CAddressDevice(device_addr));
                        }
                    }
                }
            }

            proc.waitFor();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return devices;
    }

    /**
     * Detects device activity based on a specified set of devices in use.
     * Updates the internal list of connected devices.
     *
     * @param inUse A {@code Set<DeviceType>} of devices currently in use
     */
    public void detectDeviceActivity(Set<DeviceType> inUse) {
        probeDevices(inUse, true);
    }

    /**
     * Detects device activity by scanning the I2C bus.
     * Updates the internal list of connected devices.
     */
    public void detectDeviceActivity() { probeDevices(scan(), false); }

    synchronized private void probeDevices(Set<DeviceType> currentDevices,
                                           boolean inUse) {
        for (DeviceType device : currentDevices) {
            if (connectedDevices.add(device)) {
                String addr_hex = getDeviceI2CAddressHex(device);
                logger.info("New device added at I2C address: " + addr_hex);
            }
        }

        for (DeviceType device : connectedDevices) {
            if (!currentDevices.contains(device)) {
                String addr_hex = getDeviceI2CAddressHex(device);
                if (inUse) {
                    logger.info("Disconnected device that was in use at I2C address: " +
                            addr_hex);

                } else {
                    logger.info("Disconnected device at I2C address: " +
                                addr_hex);
                }
                connectedDevices.remove(device);
            }
        }
    }

    private boolean scanAgain() {
        return System.currentTimeMillis() - lastScan >= SCAN_INTERVAL_MS;
    }

    /**
     * Gets the list of currently connected devices.
     * May perform a new scan if the scan interval has elapsed.
     *
     * @return A {@code Set<DeviceType>} of currently connected devices
     */
    public Set<DeviceType> getConnectedDevices() {
        return getConnectedDevices(scanAgain());
    }

    /**
     * Gets the list of I2C addresses for connected devices.
     * May perform a new scan if the scan interval has elapsed.
     *
     * @return A {@code List<Integer>} of I2C addresses for connected devices
     */
    public List<Integer> getConnectedDeviceI2CAddresses() {
        return getConnectedDeviceI2CAddresses(scanAgain());
    }

    /**
     * Gets the list of currently connected devices.
     *
     * @param refresh If {@code true}, performs a new scan before returning the
     *                result
     * @return A {@code Set<DeviceType>} of currently connected devices
     */
    public Set<DeviceType> getConnectedDevices(boolean refresh) {
        if (refresh) {
            detectDeviceActivity();
        }

        return connectedDevices;
    }

    /**
     * Gets the list of I2C addresses for connected devices.
     *
     * @param refresh If {@code true}, performs a new scan before returning the
     *                result
     * @return A {@code List<Integer>} of I2C addresses for connected devices
     */
    public List<Integer> getConnectedDeviceI2CAddresses(boolean refresh) {
        if (refresh) {
            detectDeviceActivity();
        }

        return connectedDevices.stream()
            .map(d -> getDeviceI2CAddress(d))
            .filter(Objects::nonNull)
            .toList();
    }

    /**
     * Gets the I2C address for a specific device type.
     *
     * @param type The {@code DeviceType} to get the address for
     * @return The I2C address as an {@code Integer}, or {@code null} if the
     *     device
     *         type is not supported
     */
    public static Integer getDeviceI2CAddress(DeviceType type) {
        switch (type) {
        case GROVE_LED_MATRIX:
            return GroveLedMatrix.LED_DISPLAY_ADDR;
        case GROVE_LCD:
            return GroveLcd.DISPLAY_TEXT_ADDR;
        case GROVE_GESTURE_DETECTOR:
            return GroveGestureDetector.PAJ7620_ADDR;
        case GROVE_3AXIS_ACCELEROMETER:
            return Grove3AxisAccelerometer.MMA7660_ADDR;
        default:
            return null;
        }
    }

    /**
     * Gets the device type associated with a specific I2C address.
     *
     * @param addr The I2C address to look up
     * @return The corresponding {@code DeviceType}, or {@code null} if the
     *     address
     *         is not recognized
     */
    public static DeviceType getI2CAddressDevice(int addr) {
        switch (addr) {
        case GroveLedMatrix.LED_DISPLAY_ADDR:
            return DeviceType.GROVE_LED_MATRIX;
        case GroveLcd.DISPLAY_TEXT_ADDR:
            return DeviceType.GROVE_LCD;
        case GroveGestureDetector.PAJ7620_ADDR:
            return DeviceType.GROVE_GESTURE_DETECTOR;
        case Grove3AxisAccelerometer.MMA7660_ADDR:
            return DeviceType.GROVE_3AXIS_ACCELEROMETER;
        default:
            return null;
        }
    }

    /**
     * Gets the hexadecimal representation of a device's I2C address.
     *
     * @param type The {@code DeviceType} to get the hexadecimal address for
     * @return The I2C address as a hexadecimal {@code String}, or {@code null}
     *     if
     *         the device type is not supported
     */
    public static String getDeviceI2CAddressHex(DeviceType type) {
        switch (type) {
        case GROVE_LED_MATRIX:
            return Integer.toHexString(GroveLedMatrix.LED_DISPLAY_ADDR);
        case GROVE_LCD:
            return Integer.toHexString(GroveLcd.DISPLAY_TEXT_ADDR);
        case GROVE_GESTURE_DETECTOR:
            return Integer.toHexString(GroveGestureDetector.PAJ7620_ADDR);
        case GROVE_3AXIS_ACCELEROMETER:
            return Integer.toHexString(Grove3AxisAccelerometer.MMA7660_ADDR);
        default:
            return null;
        }
    }
}
