package pt.unl.fct.di.tardis.babel.iot.api;

/**
 * Catalogue of IoT device models supported by the Babel control
 * protocols. Each constant carries the {@link DeviceInterface} over
 * which the device is driven, which lets the protocol decide how to
 * address and interact with it.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public enum DeviceType {
    // ---- I2C_OUT -----------------------------------------------------------
    /** Grove LED matrix display, driven over I²C. */
    GROVE_LED_MATRIX(DeviceInterface.I2C_OUT),
    /** Grove LCD display (16×2 / RGB variants), driven over I²C. */
    GROVE_LCD(DeviceInterface.I2C_OUT),

    // ---- I2C_IN ------------------------------------------------------------
    /** Grove three-axis accelerometer, read over I²C. */
    GROVE_3AXIS_ACCELEROMETER(DeviceInterface.I2C_IN),
    /** Grove barometer (pressure / temperature / altitude), read over I²C. */
    GROVE_BAROMETER(DeviceInterface.I2C_IN),
    /** Grove gesture detector, read over I²C. */
    GROVE_GESTURE_DETECTOR(DeviceInterface.I2C_IN),

    // ---- DIGITAL_OUT -------------------------------------------------------
    /** Generic digital-output device wired to a GPIO line. */
    GROVE_DIGITAL_OUTPUT_DEVICE(DeviceInterface.DIGITAL_OUT),
    /** Grove four-digit seven-segment display. */
    GROVE_4DIGIT_DISPLAY(DeviceInterface.DIGITAL_OUT),
    /** Grove buzzer. */
    GROVE_BUZZER(DeviceInterface.DIGITAL_OUT),
    /** Grove chainable RGB LED. */
    GROVE_CHAINABLE_RGB(DeviceInterface.DIGITAL_OUT),
    /** Grove ten-segment LED bar. */
    GROVE_LED_BAR(DeviceInterface.DIGITAL_OUT),

    // ---- DIGITAL_IN --------------------------------------------------------
    /** Generic digital-input device wired to a GPIO line. */
    GROVE_DIGITAL_INPUT_DEVICE(DeviceInterface.DIGITAL_IN),
    /** Grove rotary encoder (with push-button). */
    GROVE_ENCODER(DeviceInterface.DIGITAL_IN),
    /** Grove ultrasonic ranger. */
    GROVE_ULTRASONIC_RANGER(DeviceInterface.DIGITAL_IN),

    // ---- UART --------------------------------------------------------------
    /** Grove Air530 GPS, read over UART. */
    GROVE_GPS_AIR530(DeviceInterface.UART),

    // ---- ANALOG ------------------------------------------------------------
    /** Generic analogue-input device read through an external ADC. */
    GROVE_ANALOG_INPUT_DEVICE(DeviceInterface.ANALOG);

    private final DeviceInterface iface;

    DeviceType(DeviceInterface iface) { this.iface = iface; }

    /** @return the bus over which devices of this type are driven. */
    public DeviceInterface getDeviceInterface() { return this.iface; }
}
