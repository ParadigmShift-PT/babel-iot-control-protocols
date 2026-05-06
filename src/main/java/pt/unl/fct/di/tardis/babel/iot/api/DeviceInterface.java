package pt.unl.fct.di.tardis.babel.iot.api;

/**
 * Physical / logical bus over which an IoT device is wired to the host
 * (typically a Raspberry Pi gateway).
 * <p>
 * The interface determines what addressing information the protocol
 * needs in order to drive the device — for example, I²C devices are
 * addressed by their bus address while digital and analogue devices are
 * addressed by GPIO line number.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public enum DeviceInterface {
    /** I²C device used as an input (sensor reads). */
    I2C_IN,
    /** I²C device used as an output (actuator writes). */
    I2C_OUT,
    /** Digital GPIO input. */
    DIGITAL_IN,
    /** Digital GPIO output. */
    DIGITAL_OUT,
    /** Analogue input via an external ADC. */
    ANALOG,
    /** UART / serial-attached device. */
    UART
}
