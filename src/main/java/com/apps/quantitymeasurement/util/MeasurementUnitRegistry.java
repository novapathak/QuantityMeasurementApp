package com.apps.quantitymeasurement.util;

import com.apps.quantitymeasurement.core.IMeasurable;
import com.apps.quantitymeasurement.units.LengthUnit;
import com.apps.quantitymeasurement.units.TemperatureUnit;
import com.apps.quantitymeasurement.units.VolumeUnit;
import com.apps.quantitymeasurement.units.WeightUnit;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public final class MeasurementUnitRegistry {

    private static final Map<String, IMeasurable> UNIT_ALIASES = new LinkedHashMap<>();
    private static final Map<String, String> MEASUREMENT_TYPE_ALIASES = new LinkedHashMap<>();

    static {
        registerMeasurementType("LengthUnit", "length", "lengthunit");
        registerMeasurementType("WeightUnit", "weight", "weightunit");
        registerMeasurementType("VolumeUnit", "volume", "volumeunit");
        registerMeasurementType("TemperatureUnit", "temperature", "temperatureunit");

        registerLengthUnits();
        registerWeightUnits();
        registerVolumeUnits();
        registerTemperatureUnits();
    }

    private MeasurementUnitRegistry() {
    }

    public static Optional<IMeasurable> resolveUnit(String unitName) {
        if (unitName == null || unitName.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(UNIT_ALIASES.get(normalize(unitName)));
    }

    public static Optional<String> canonicalUnitName(String unitName) {
        return resolveUnit(unitName).map(IMeasurable::getUnitName);
    }

    public static Optional<String> canonicalMeasurementType(String measurementType) {
        if (measurementType == null || measurementType.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(MEASUREMENT_TYPE_ALIASES.get(normalize(measurementType)));
    }

    public static Optional<String> measurementTypeForUnit(String unitName) {
        return resolveUnit(unitName).map(IMeasurable::getMeasurementType);
    }

    public static boolean isValidUnitForMeasurementType(String unitName, String measurementType) {
        Optional<IMeasurable> unit = resolveUnit(unitName);
        Optional<String> type = canonicalMeasurementType(measurementType);

        if (unit.isEmpty() || type.isEmpty()) {
            return false;
        }

        return type.get().equalsIgnoreCase(unit.get().getMeasurementType());
    }

    public static Map<String, List<String>> supportedUnitsByMeasurementType() {
        Map<String, List<String>> supportedUnits = new LinkedHashMap<>();
        supportedUnits.put("LengthUnit", unitNames(LengthUnit.values()));
        supportedUnits.put("WeightUnit", unitNames(WeightUnit.values()));
        supportedUnits.put("VolumeUnit", unitNames(VolumeUnit.values()));
        supportedUnits.put("TemperatureUnit", unitNames(TemperatureUnit.values()));
        return supportedUnits;
    }

    public static List<String> supportedUnitsForMeasurementType(String measurementType) {
        return canonicalMeasurementType(measurementType)
                .map(type -> supportedUnitsByMeasurementType().getOrDefault(type, List.of()))
                .orElse(List.of());
    }

    public static boolean supportsArithmeticForMeasurementType(String measurementType) {
        return canonicalMeasurementType(measurementType)
                .map(type -> switch (type) {
                    case "LengthUnit" -> LengthUnit.INCH.supportsArithmetic();
                    case "WeightUnit" -> WeightUnit.GRAM.supportsArithmetic();
                    case "VolumeUnit" -> VolumeUnit.LITRE.supportsArithmetic();
                    case "TemperatureUnit" -> TemperatureUnit.CELSIUS.supportsArithmetic();
                    default -> false;
                })
                .orElse(false);
    }

    private static void registerMeasurementType(String canonicalType, String... aliases) {
        MEASUREMENT_TYPE_ALIASES.put(normalize(canonicalType), canonicalType);
        for (String alias : aliases) {
            MEASUREMENT_TYPE_ALIASES.put(normalize(alias), canonicalType);
        }
    }

    private static void registerLengthUnits() {
        registerAliases(LengthUnit.INCH, "inch", "inches");
        registerAliases(LengthUnit.FEET, "feet", "foot");
        registerAliases(LengthUnit.YARDS, "yard", "yards");
        registerAliases(LengthUnit.CENTIMETERS, "centimeter", "centimeters", "centimetre", "centimetres");
    }

    private static void registerWeightUnits() {
        registerAliases(WeightUnit.GRAM, "gram", "grams");
        registerAliases(WeightUnit.KILOGRAM, "kilogram", "kilograms");
        registerAliases(WeightUnit.POUND, "pound", "pounds");
    }

    private static void registerVolumeUnits() {
        registerAliases(VolumeUnit.LITRE, "litre", "litres", "liter", "liters");
        registerAliases(VolumeUnit.MILLILITRE, "millilitre", "millilitres", "milliliter", "milliliters");
        registerAliases(VolumeUnit.GALLON, "gallon", "gallons");
    }

    private static void registerTemperatureUnits() {
        registerAliases(TemperatureUnit.CELSIUS, "celsius");
        registerAliases(TemperatureUnit.FAHRENHEIT, "fahrenheit");
        registerAliases(TemperatureUnit.KELVIN, "kelvin");
    }

    private static void registerAliases(IMeasurable measurable, String... aliases) {
        UNIT_ALIASES.put(normalize(measurable.getUnitName()), measurable);
        for (String alias : aliases) {
            UNIT_ALIASES.put(normalize(alias), measurable);
        }
    }

    private static List<String> unitNames(IMeasurable[] measurables) {
        return Arrays.stream(measurables)
                .map(IMeasurable::getUnitName)
                .toList();
    }

    private static String normalize(String value) {
        return value.replaceAll("[_\\s-]", "")
                .toLowerCase(Locale.ROOT)
                .trim();
    }
}
