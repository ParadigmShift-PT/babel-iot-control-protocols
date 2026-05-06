package pt.unl.fct.di.tardis.babel.iot.api;

/**
 * Namespace for the per-device-type measurement modes that an input
 * request can ask for.
 * <p>
 * Different sensors expose different read modes (an accelerometer can
 * return raw axes, smoothed acceleration, or the full data structure;
 * an ultrasonic ranger can return the same distance in different
 * units; a barometer can return temperature, pressure, altitude, or
 * all at once). Each supported sensor family declares its own
 * enumeration inside this class, and concrete input requests carry
 * one of these values to tell the protocol which read mode to use.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class InputType {

    /** Read modes for a Grove ultrasonic ranger (distance unit). */
    static public enum UltrasonicRanger {
        /** Distance in centimetres. */
        CENTIMETERS,
        /** Distance in inches. */
        INCHES,
        /** Distance in millimetres. */
        MILLIMETERS,
    }

    /** Read modes for a three-axis accelerometer. */
    static public enum Accelerometer {
        /** All available data fields packed into a single reading. */
        ALL_DATA,
        /** Magnitude of the acceleration vector. */
        ACCELERATION_SIMPLE,
        /** Per-axis acceleration values (x, y, z). */
        ACCELERATION_DATA,
        /** Raw axis readings (x, y, z) with no derivation. */
        XYZ,
    }

    /** Read modes for a Grove barometer. */
    static public enum Barometer {
        /** All available data fields packed into a single reading. */
        ALL_DATA,
        /** Temperature only. */
        TEMPERATURE,
        /** Pressure only. */
        PRESSURE,
        /** Derived altitude only. */
        ALTITUDE,
    }
}
