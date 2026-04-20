/********************************************************************************************
 * DeviceType
 *
 * This is just a simple enumeration of the devices currently supported by the
 *library
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 ********************************************************************************************/

package pt.unl.fct.di.tardis.babel.iot.api;

public enum DeviceType {
    // I2C_OUT
    GROVE_LED_MATRIX(DeviceInterface.I2C_OUT),
    GROVE_LCD(DeviceInterface.I2C_OUT),
    // I2C_IN
    GROVE_3AXIS_ACCELEROMETER(DeviceInterface.I2C_IN),
    GROVE_BAROMETER(DeviceInterface.I2C_IN),
    GROVE_GESTURE_DETECTOR(DeviceInterface.I2C_IN),
    // DIGITAL_OUT
    GROVE_DIGITAL_OUTPUT_DEVICE(DeviceInterface.DIGITAL_OUT),
    GROVE_4DIGIT_DISPLAY(DeviceInterface.DIGITAL_OUT),
    GROVE_BUZZER(DeviceInterface.DIGITAL_OUT),
    GROVE_CHAINABLE_RGB(DeviceInterface.DIGITAL_OUT),
    GROVE_LED_BAR(DeviceInterface.DIGITAL_OUT),
    // DIGITAL_IN
    GROVE_DIGITAL_INPUT_DEVICE(DeviceInterface.DIGITAL_IN),
    GROVE_ENCODER(DeviceInterface.DIGITAL_IN),
    GROVE_ULTRASONIC_RANGER(DeviceInterface.DIGITAL_IN),
    // UART
    GROVE_GPS_AIR530(DeviceInterface.UART),
    // ANALOG
    GROVE_ANALOG_INPUT_DEVICE(DeviceInterface.ANALOG);

    private final DeviceInterface iface;

    DeviceType(DeviceInterface iface) { this.iface = iface; }

    public DeviceInterface getDeviceInterface() { return this.iface; }
}
