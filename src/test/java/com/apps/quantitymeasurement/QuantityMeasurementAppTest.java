package com.apps.quantitymeasurement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class QuantityMeasurementAppTest {

	private static final double EPS = 1e-6;

	// EQUALITY TESTS

	@Test
	void testTemperatureEquality_CelsiusToCelsius_SameValue() {
		assertEquals(new Quantity<>(0.0, TemperatureUnit.CELSIUS), new Quantity<>(0.0, TemperatureUnit.CELSIUS));
	}

	@Test
	void testTemperatureEquality_FahrenheitToFahrenheit_SameValue() {
		assertEquals(new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT),
				new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT));
	}

	@Test
	void testTemperatureEquality_KelvinToKelvin_SameValue() {
		assertEquals(new Quantity<>(273.15, TemperatureUnit.KELVIN), new Quantity<>(273.15, TemperatureUnit.KELVIN));
	}

	@Test
	void testTemperatureEquality_CelsiusToFahrenheit_0Celsius32Fahrenheit() {
		assertEquals(new Quantity<>(0.0, TemperatureUnit.CELSIUS), new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT));
	}

	@Test
	void testTemperatureEquality_CelsiusToFahrenheit_100Celsius212Fahrenheit() {
		assertEquals(new Quantity<>(100.0, TemperatureUnit.CELSIUS), new Quantity<>(212.0, TemperatureUnit.FAHRENHEIT));
	}

	@Test
	void testTemperatureEquality_CelsiusToFahrenheit_Negative40Equal() {
		assertEquals(new Quantity<>(-40.0, TemperatureUnit.CELSIUS), new Quantity<>(-40.0, TemperatureUnit.FAHRENHEIT));
	}

	@Test
	void testTemperatureEquality_CelsiusToKelvin() {
		assertEquals(new Quantity<>(0.0, TemperatureUnit.CELSIUS), new Quantity<>(273.15, TemperatureUnit.KELVIN));
	}

	@Test
	void testTemperatureEquality_SymmetricProperty() {
		Quantity<TemperatureUnit> a = new Quantity<>(0.0, TemperatureUnit.CELSIUS);
		Quantity<TemperatureUnit> b = new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT);

		assertTrue(a.equals(b));
		assertTrue(b.equals(a));
	}

	@Test
	void testTemperatureEquality_ReflexiveProperty() {
		Quantity<TemperatureUnit> q = new Quantity<>(50.0, TemperatureUnit.CELSIUS);
		assertTrue(q.equals(q));
	}

	// CONVERSION TESTS

	@Test
	void testTemperatureConversion_CelsiusToFahrenheit_VariousValues() {
		assertEquals(122.0,
				new Quantity<>(50.0, TemperatureUnit.CELSIUS).convertTo(TemperatureUnit.FAHRENHEIT).getValue(), EPS);
	}

	@Test
	void testTemperatureConversion_FahrenheitToCelsius_VariousValues() {
		assertEquals(100.0,
				new Quantity<>(212.0, TemperatureUnit.FAHRENHEIT).convertTo(TemperatureUnit.CELSIUS).getValue(), EPS);
	}

	@Test
	void testTemperatureConversion_RoundTrip_PreservesValue() {
		double value = new Quantity<>(37.0, TemperatureUnit.CELSIUS).convertTo(TemperatureUnit.FAHRENHEIT)
				.convertTo(TemperatureUnit.CELSIUS).getValue();
		assertEquals(37.0, value, EPS);
	}

	@Test
	void testTemperatureConversion_SameUnit() {
		assertEquals(10.0, new Quantity<>(10.0, TemperatureUnit.CELSIUS).convertTo(TemperatureUnit.CELSIUS).getValue(),
				EPS);
	}

	@Test
	void testTemperatureConversion_ZeroValue() {
		assertEquals(32.0,
				new Quantity<>(0.0, TemperatureUnit.CELSIUS).convertTo(TemperatureUnit.FAHRENHEIT).getValue(), EPS);
	}

	@Test
	void testTemperatureConversion_NegativeValues() {
		assertEquals(-4.0,
				new Quantity<>(-20.0, TemperatureUnit.CELSIUS).convertTo(TemperatureUnit.FAHRENHEIT).getValue(), EPS);
	}

	@Test
	void testTemperatureConversion_LargeValues() {
		assertEquals(1832.0,
				new Quantity<>(1000.0, TemperatureUnit.CELSIUS).convertTo(TemperatureUnit.FAHRENHEIT).getValue(), EPS);
	}

	@Test
	void testTemperatureConversionPrecision_Epsilon() {
		Quantity<TemperatureUnit> q = new Quantity<>(0.000001, TemperatureUnit.CELSIUS);
		assertTrue(Math.abs(q.convertTo(TemperatureUnit.FAHRENHEIT).convertTo(TemperatureUnit.CELSIUS).getValue()
				- 0.000001) < EPS);
	}

	@Test
	void testTemperatureConversionEdgeCase_VerySmallDifference() {
		Quantity<TemperatureUnit> q1 = new Quantity<>(0.000001, TemperatureUnit.CELSIUS);
		Quantity<TemperatureUnit> q2 = new Quantity<>(0.00002, TemperatureUnit.CELSIUS);

		assertNotEquals(q1, q2);
	}

	// EDGE CASES

	@Test
	void testTemperatureAbsoluteZero() {
		assertEquals(new Quantity<>(-273.15, TemperatureUnit.CELSIUS), new Quantity<>(0.0, TemperatureUnit.KELVIN));
	}

	@Test
	void testTemperatureEqualPoint_Minus40() {
		assertEquals(new Quantity<>(-40.0, TemperatureUnit.CELSIUS), new Quantity<>(-40.0, TemperatureUnit.FAHRENHEIT));
	}

	@Test
	void testTemperatureDifferentValuesInequality() {
		assertNotEquals(new Quantity<>(50.0, TemperatureUnit.CELSIUS), new Quantity<>(100.0, TemperatureUnit.CELSIUS));
	}

	// UNSUPPORTED OPERATIONS

	@Test
	void testTemperatureUnsupportedOperation_Add() {
		assertThrows(UnsupportedOperationException.class, () -> new Quantity<>(100.0, TemperatureUnit.CELSIUS)
				.add(new Quantity<>(50.0, TemperatureUnit.CELSIUS)));
	}

	@Test
	void testTemperatureUnsupportedOperation_Subtract() {
		assertThrows(UnsupportedOperationException.class, () -> new Quantity<>(100.0, TemperatureUnit.CELSIUS)
				.subtract(new Quantity<>(50.0, TemperatureUnit.CELSIUS)));
	}

	@Test
	void testTemperatureUnsupportedOperation_Divide() {
		assertThrows(UnsupportedOperationException.class, () -> new Quantity<>(100.0, TemperatureUnit.CELSIUS)
				.divide(new Quantity<>(50.0, TemperatureUnit.CELSIUS)));
	}

	// CROSS CATEGORY PROTECTION

	@Test
	void testTemperatureVsLengthIncompatibility() {
		assertFalse(new Quantity<>(100.0, TemperatureUnit.CELSIUS).equals(new Quantity<>(100.0, LengthUnit.FEET)));
	}

	@Test
	void testTemperatureVsWeightIncompatibility() {
		assertFalse(new Quantity<>(50.0, TemperatureUnit.CELSIUS).equals(new Quantity<>(50.0, WeightUnit.KILOGRAM)));
	}

	@Test
	void testTemperatureVsVolumeIncompatibility() {
		assertFalse(new Quantity<>(25.0, TemperatureUnit.CELSIUS).equals(new Quantity<>(25.0, VolumeUnit.LITRE)));
	}

	// OPERATION SUPPORT METHODS

	@Test
	void testOperationSupportMethods_TemperatureUnitAddition() {
		assertFalse(TemperatureUnit.CELSIUS.supportsArithmetic());
	}

	@Test
	void testOperationSupportMethods_TemperatureUnitDivision() {
		assertFalse(TemperatureUnit.FAHRENHEIT.supportsArithmetic());
	}

	@Test
	void testOperationSupportMethods_LengthUnitAddition() {
		assertTrue(LengthUnit.FEET.supportsArithmetic());
	}

	@Test
	void testOperationSupportMethods_WeightUnitDivision() {
		assertTrue(WeightUnit.KILOGRAM.supportsArithmetic());
	}

	// INTERFACE + STRUCTURE TESTS

	@Test
	void testTemperatureNullUnitValidation() {
		assertThrows(IllegalArgumentException.class, () -> new Quantity<>(100.0, null));
	}

	@Test
	void testTemperatureNullOperandValidation_InComparison() {
		assertFalse(new Quantity<>(100.0, TemperatureUnit.CELSIUS).equals(null));
	}

	@Test
	void testTemperatureUnit_AllConstants() {
		assertNotNull(TemperatureUnit.CELSIUS);
		assertNotNull(TemperatureUnit.FAHRENHEIT);
		assertNotNull(TemperatureUnit.KELVIN);
	}

	@Test
	void testTemperatureUnit_NameMethod() {
		assertEquals("CELSIUS", TemperatureUnit.CELSIUS.getUnitName());
	}

	@Test
	void testTemperatureUnit_ConversionFactor() {
		assertThrows(UnsupportedOperationException.class, () -> TemperatureUnit.CELSIUS.getConversionFactor());
	}

	@Test
	void testTemperatureEnumImplementsIMeasurable() {
		assertTrue(IMeasurable.class.isAssignableFrom(TemperatureUnit.class));
	}

	@Test
	void testTemperatureDefaultMethodInheritance() {
		assertTrue(LengthUnit.FEET.supportsArithmetic());
	}

	@Test
	void testTemperatureCrossUnitAdditionAttempt() {
		assertThrows(UnsupportedOperationException.class, () -> new Quantity<>(0.0, TemperatureUnit.CELSIUS)
				.add(new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT)));
	}

	@Test
	void testTemperatureValidateOperationSupport_MethodBehavior() {
		assertThrows(UnsupportedOperationException.class,
				() -> TemperatureUnit.CELSIUS.validateOperationSupport("ADD"));
	}

	@Test
	void testTemperatureIntegrationWithGenericQuantity() {
		Quantity<TemperatureUnit> q = new Quantity<>(25.0, TemperatureUnit.CELSIUS);
		assertEquals(25.0, q.getValue(), EPS);
	}

}