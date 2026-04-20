/********************************************************************************************
 * InputType
 *
 * This is just a simple enumeration of the currently supported measurements
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 ********************************************************************************************/

package pt.unl.fct.di.tardis.babel.iot.api;

public class InputType {
    static public enum UltrasonicRanger {
        CENTIMETERS,
        INCHES,
        MILLIMETERS,
    }

    static public enum Accelerometer {
        ALL_DATA,
        ACCELERATION_SIMPLE,
        ACCELERATION_DATA,
        XYZ,
    }

    static public enum Barometer {
        ALL_DATA,
        TEMPERATURE,
        PRESSURE,
        ALTITUDE,
    }
}
