package com.apps.quantitymeasurement;
import org.junit.jupiter.api.Test;

import com.apps.quantitymeasurement.IMeasurable;
import com.apps.quantitymeasurement.LengthUnit;
import com.apps.quantitymeasurement.Quantity;
import com.apps.quantitymeasurement.WeightUnit;

import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

public class QuantityMeasurementAppTest {

    // IMeasurable Interface Tests

    @Test
    @DisplayName("WeightUnit correctly implements IMeasurable")
    void shouldImplementIMeasurable_WhenUsingWeightUnit() {
        assertNotNull(WeightUnit.KILOGRAM.getUnitName());
        assertEquals(1.0, WeightUnit.KILOGRAM.convertFromBaseUnit(
                WeightUnit.KILOGRAM.convertToBaseUnit(1.0)));
    }

    @Test
    @DisplayName("LengthUnit and WeightUnit behave consistently")
    void shouldBehaveConsistently_WhenUsingDifferentUnitCategories() {
        assertEquals(
                LengthUnit.FEET.convertToBaseUnit(1.0),
                LengthUnit.INCH.convertToBaseUnit(12.0)
        );

        assertEquals(
                WeightUnit.KILOGRAM.convertToBaseUnit(1.0),
                WeightUnit.GRAM.convertToBaseUnit(1000.0)
        );
    }

    // Equality Tests

    @Test
    void shouldBeEqual_WhenLengthValuesAreEquivalent() {
        Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(12.0, LengthUnit.INCH);
        assertEquals(q1, q2);
    }

    @Test
    void shouldBeEqual_WhenWeightValuesAreEquivalent() {
        Quantity<WeightUnit> q1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> q2 = new Quantity<>(1000.0, WeightUnit.GRAM);
        assertEquals(q1, q2);
    }

    // Conversion Tests

    @Test
    void shouldConvertFeetToInchesCorrectly() {
        Quantity<LengthUnit> q = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> converted = q.convertTo(LengthUnit.INCH);
        assertEquals(new Quantity<>(12.0, LengthUnit.INCH), converted);
    }

    @Test
    void shouldConvertKilogramToGramCorrectly() {
        Quantity<WeightUnit> q = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> converted = q.convertTo(WeightUnit.GRAM);
        assertEquals(new Quantity<>(1000.0, WeightUnit.GRAM), converted);
    }

    // Addition Tests

    @Test
    void shouldAddLengthValuesCorrectly() {
        Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(12.0, LengthUnit.INCH);
        Quantity<LengthUnit> result = q1.add(q2, LengthUnit.FEET);
        assertEquals(new Quantity<>(2.0, LengthUnit.FEET), result);
    }

    @Test
    void shouldAddWeightValuesCorrectly() {
        Quantity<WeightUnit> q1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> q2 = new Quantity<>(1000.0, WeightUnit.GRAM);
        Quantity<WeightUnit> result = q1.add(q2, WeightUnit.KILOGRAM);
        assertEquals(new Quantity<>(2.0, WeightUnit.KILOGRAM), result);
    }

    // Cross Category Safety

    @Test
    void shouldNotBeEqual_WhenComparingDifferentCategories() {
        Quantity<LengthUnit> length = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<WeightUnit> weight = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        assertNotEquals(length, weight);
    }

    // Constructor Validation

    @Test
    void shouldThrowException_WhenUnitIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(1.0, null));
    }

    @Test
    void shouldThrowException_WhenValueIsNaN() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(Double.NaN, LengthUnit.FEET));
    }

    // HashCode & Equals Contract

    @Test
    void shouldHaveSameHashCode_WhenQuantitiesAreEqual() {
        Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(12.0, LengthUnit.INCH);
        assertEquals(q1.hashCode(), q2.hashCode());
    }

    @Test
    void shouldFollowEqualsContract() {
        Quantity<LengthUnit> a = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(12.0, LengthUnit.INCH);
        Quantity<LengthUnit> c = new Quantity<>(1.0, LengthUnit.FEET);

        assertTrue(a.equals(a)); 
        assertTrue(a.equals(b)); 
        assertTrue(a.equals(b) && b.equals(c) && a.equals(c));
    }

    // Immutability Test

    @Test
    void shouldReturnNewObject_WhenConvertingQuantity() {
        Quantity<LengthUnit> q = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> converted = q.convertTo(LengthUnit.INCH);
        assertNotSame(q, converted);
    }

    // Scalability Test

    enum VolumeUnit implements IMeasurable {
        LITER(1.0),
        MILLILITER(0.001);

        private final double factor;

        VolumeUnit(double factor) {
            this.factor = factor;
        }

        public double convertToBaseUnit(double value) {
            return value * factor;
        }

        public double convertFromBaseUnit(double baseValue) {
            return baseValue / factor;
        }

        public String getUnitName() {
            return name();
        }

		@Override
		public double getConversionFactor() {
			return 0;
		}
    }

    @Test
    void testScalability_NewUnitEnumIntegration() {

        Quantity<VolumeUnit> v1 =
                new Quantity<>(1.0, VolumeUnit.LITER);

        Quantity<VolumeUnit> v2 =
                new Quantity<>(1000.0, VolumeUnit.MILLILITER);

        assertEquals(v1, v2);
    }

