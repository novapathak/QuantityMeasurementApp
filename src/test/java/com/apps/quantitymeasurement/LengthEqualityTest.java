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
	public void testEquality_InchToInch_SameValue() {
		Length l1 = new Length(1.0, LengthUnit.INCH);
		Length l2 = new Length(1.0, LengthUnit.INCH);
		assertTrue(l1.equals(l2));
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
}