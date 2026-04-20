package pt.unl.fct.di.tardis.babel.iot.api;

import java.util.Comparator;
import java.util.Objects;
import java.util.Set;

public class Threshold<T> {
    private final ThresholdType type;
    private final T singleValue;
    private final T lowerBound;
    private final T upperBound;
    private final Set<T> multipleValues;
    private final Comparator<T> comparator;

    private enum ThresholdType {
        NONE,          // always trigger
        EQUAL,         // trigger if value is equal to the threshold value
        NOT_EQUAL,     // trigger if value is different from the threshold value
        LESS_THAN,     // trigger if value is less than the threshold value
        GREATER_THAN,  // trigger if value is greater than the threshold value
        IN_RANGE,      // trigger if value falls within range
        OUTSIDE_RANGE, // trigger if value falls outside range
        ANY            // trigger if value matches any of the threshold values
    }

    public static <T> Threshold<T> none() {
        return new Threshold<>(ThresholdType.NONE, null, null, null, null,
                               null);
    }

    public static <T> Threshold<T> equalTo(T value, Comparator<T> comparator) {
        return new Threshold<>(ThresholdType.EQUAL, value, null, null, null,
                               comparator);
    }

    public static <T> Threshold<T> equalTo(T value) {
        return new Threshold<>(ThresholdType.EQUAL, value, null, null, null,
                               null);
    }

    public static <T> Threshold<T> notEqualTo(T value,
                                              Comparator<T> comparator) {
        return new Threshold<>(ThresholdType.NOT_EQUAL, value, null, null, null,
                               comparator);
    }

    public static <T> Threshold<T> notEqualTo(T value) {
        return new Threshold<>(ThresholdType.NOT_EQUAL, value, null, null, null,
                               null);
    }

    public static <T> Threshold<T> lessThan(T value, Comparator<T> comparator) {
        return new Threshold<>(ThresholdType.LESS_THAN, value, null, null, null,
                               comparator);
    }

    public static <T> Threshold<T> greaterThan(T value,
                                               Comparator<T> comparator) {
        return new Threshold<>(ThresholdType.GREATER_THAN, value, null, null,
                               null, comparator);
    }

    public static <T> Threshold<T> inRange(T lowerBound, T upperBound,
                                           Comparator<T> comparator) {
        return new Threshold<>(ThresholdType.IN_RANGE, null, lowerBound,
                               upperBound, null, comparator);
    }

    public static <T> Threshold<T> outsideRange(T lowerBound, T upperBound,
                                                Comparator<T> comparator) {
        return new Threshold<>(ThresholdType.OUTSIDE_RANGE, null, lowerBound,
                               upperBound, null, comparator);
    }

    public static <T> Threshold<T> any(Set<T> values) {
        return new Threshold<>(ThresholdType.ANY, null, null, null, values,
                               null);
    }

    private Threshold(ThresholdType type, T singleValue, T lowerBound,
                      T upperBound, Set<T> multipleValues,
                      Comparator<T> comparator) {
        this.type = type;
        this.singleValue = singleValue;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.multipleValues = multipleValues;
        this.comparator = comparator;
    }

    public boolean test(T value) {
        if (value == null)
            return false;

        switch (type) {
        case NONE:
            return true;
        case EQUAL:
            return comparator == null
                ? Objects.equals(value, singleValue)
                : comparator.compare(value, singleValue) == 0;
        case NOT_EQUAL:
            return comparator == null
                ? !Objects.equals(value, singleValue)
                : comparator.compare(value, singleValue) != 0;
        case LESS_THAN:
            if (comparator == null) {
                throw new IllegalArgumentException(
                    "LESS_THAN comparisons require a comparator");
            }
            return comparator.compare(value, singleValue) < 0;
        case GREATER_THAN:
            if (comparator == null) {
                throw new IllegalArgumentException(
                    "GREATER_THAN comparisons require a comparator");
            }
            return comparator.compare(value, singleValue) > 0;
        case IN_RANGE:
            if (comparator == null) {
                throw new IllegalArgumentException(
                    "Range comparisons require a comparator");
            }
            return comparator.compare(value, lowerBound) >= 0 &&
                comparator.compare(value, upperBound) <= 0;
        case OUTSIDE_RANGE:
            if (comparator == null) {
                throw new IllegalArgumentException(
                    "Range comparisons require a comparator");
            }
            return comparator.compare(value, lowerBound) >= 0 &&
                comparator.compare(value, upperBound) <= 0;
        case ANY:
            return multipleValues.contains(value);
        default:
            return false;
        }
    }
}
