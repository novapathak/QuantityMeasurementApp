package com.apps.quantitymeasurement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LengthEqualityTest {

	@Test
	public void testEquality_FeetToFeet_SameValue() {
		Length l1 = new Length(1.0, LengthUnit.FEET);
		Length l2 = new Length(1.0, LengthUnit.FEET);
		assertTrue(l1.equals(l2));
	}

	@Test
	public void testEquality_InchToInch_SameValue() {
		Length l1 = new Length(1.0, LengthUnit.INCH);
		Length l2 = new Length(1.0, LengthUnit.INCH);
		assertTrue(l1.equals(l2));
	}

	@Test
	public void testEquality_FeetToInch_EquivalentValue() {
		Length l1 = new Length(1.0, LengthUnit.FEET);
		Length l2 = new Length(12.0, LengthUnit.INCH);
		assertTrue(l1.equals(l2));
	}

	@Test
	public void testEquality_FeetToFeet_DifferentValue() {
		Length l1 = new Length(1.0, LengthUnit.FEET);
		Length l2 = new Length(2.0, LengthUnit.FEET);
		assertFalse(l1.equals(l2));
	}

	@Test
	public void testEquality_SameReference() {
		Length l1 = new Length(1.0, LengthUnit.FEET);
		assertTrue(l1.equals(l1));
	}

	@Test
	public void testEquality_NullComparison() {
		Length l1 = new Length(1.0, LengthUnit.FEET);
		assertFalse(l1.equals(null));
	}

	@Test
	public void testEquality_DifferentType() {
		Length l1 = new Length(1.0, LengthUnit.FEET);
		assertFalse(l1.equals("1.0"));
	}

	@Test
	public void testEquality_TransitiveProperty() {
		Length l1 = new Length(1.0, LengthUnit.FEET);
		Length l2 = new Length(12.0, LengthUnit.INCH);
		Length l3 = new Length(1.0, LengthUnit.FEET);

		assertTrue(l1.equals(l2));
		assertTrue(l2.equals(l3));
		assertTrue(l1.equals(l3));
	}

	// Check if same yard values are equal
	@Test
	public void testEquality_YardToYard_SameValue() {
		assertTrue(new Length(1.0, LengthUnit.YARDS).equals(new Length(1.0, LengthUnit.YARDS)));
	}

	// Check if different yard values are not equal
	@Test
	public void testEquality_YardToYard_DifferentValue() {
		assertFalse(new Length(1.0, LengthUnit.YARDS).equals(new Length(2.0, LengthUnit.YARDS)));
	}

	// Check yard to feet equivalent conversion (1 yard = 3 feet)
	@Test
	public void testEquality_YardToFeet_EquivalentValue() {
		assertTrue(new Length(1.0, LengthUnit.YARDS).equals(new Length(3.0, LengthUnit.FEET)));
	}

	// Check feet to yard equivalent conversion (symmetry)
	@Test
	public void testEquality_FeetToYard_EquivalentValue() {
		assertTrue(new Length(3.0, LengthUnit.FEET).equals(new Length(1.0, LengthUnit.YARDS)));
	}

	// Check yard to inches equivalent conversion (1 yard = 36 inches)
	@Test
	public void testEquality_YardToInches_EquivalentValue() {
		assertTrue(new Length(1.0, LengthUnit.YARDS).equals(new Length(36.0, LengthUnit.INCH)));
	}

	// Check inches to yard equivalent conversion
	@Test
	public void testEquality_InchesToYard_EquivalentValue() {
		assertTrue(new Length(36.0, LengthUnit.INCH).equals(new Length(1.0, LengthUnit.YARDS)));
	}

	// Check yard to feet non-equivalent case
	@Test
	public void testEquality_YardToFeet_NonEquivalentValue() {
		assertFalse(new Length(1.0, LengthUnit.YARDS).equals(new Length(2.0, LengthUnit.FEET)));
	}

	// Check centimeter to inches equivalent conversion
	@Test
	public void testEquality_CentimetersToInches_EquivalentValue() {
		assertTrue(new Length(1.0, LengthUnit.CENTIMETERS).equals(new Length(0.393701, LengthUnit.INCH)));
	}

	// Check centimeter to feet non-equivalent case
	@Test
	public void testEquality_CentimetersToFeet_NonEquivalentValue() {
		assertFalse(new Length(1.0, LengthUnit.CENTIMETERS).equals(new Length(1.0, LengthUnit.FEET)));
	}

	// Check transitive property across multiple units
	@Test
	public void testEquality_MultiUnit_TransitiveProperty() {

		Length yard = new Length(1.0, LengthUnit.YARDS);
		Length feet = new Length(3.0, LengthUnit.FEET);
		Length inches = new Length(36.0, LengthUnit.INCH);

		assertTrue(yard.equals(feet));
		assertTrue(feet.equals(inches));
		assertTrue(yard.equals(inches));
	}

	// Check complex equality across yards, feet and inches
	@Test
	public void testEquality_AllUnits_ComplexScenario() {

		Length yards = new Length(2.0, LengthUnit.YARDS);
		Length feet = new Length(6.0, LengthUnit.FEET);
		Length inches = new Length(72.0, LengthUnit.INCH);

		assertTrue(yards.equals(feet));
		assertTrue(feet.equals(inches));
		assertTrue(yards.equals(inches));
	}

	// Check reflexive property for yard
	@Test
	public void testEquality_YardSameReference() {
		Length yard = new Length(1.0, LengthUnit.YARDS);
		assertTrue(yard.equals(yard));
	}

	// Check yard is not equal to null
	@Test
	public void testEquality_YardNullComparison() {
		Length yard = new Length(1.0, LengthUnit.YARDS);
		assertFalse(yard.equals(null));
	}

	// Check reflexive property for centimeter
	@Test
	public void testEquality_CentimetersSameReference() {
		Length cm = new Length(1.0, LengthUnit.CENTIMETERS);
		assertTrue(cm.equals(cm));
	}

	// Check centimeter is not equal to null
	@Test
	public void testEquality_CentimetersNullComparison() {
		Length cm = new Length(1.0, LengthUnit.CENTIMETERS);
		assertFalse(cm.equals(null));
	}
}