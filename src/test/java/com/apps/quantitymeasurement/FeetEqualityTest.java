package com.apps.quantitymeasurement;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class FeetEqualityTest {
	
	@Test
	
	void testEquality_SameValue() {
		
		Feet f1 = new Feet(1.0);
		Feet f2 = new Feet(1.0);
		assertTrue(f1.equals(f2), "1.0 should be equal to 1.0");
	}
	
	@Test
	void testEquality_DifferentValue() {
		Feet f1 = new Feet(1.0);
		Feet f2 = new Feet(2.0);
		assertFalse(f1.equals(f2), "1.0 ft should not be equal to 2.0 ft");
		
	}
	
	@Test 
	void testEquality_NullComparison() {
		Feet f1 = new Feet(1.0);
		assertFalse(f1.equals(null), "Feet object should not equal non-numeric input");
		
	}
	void testEquality_SameReference() {
		Feet f1 = new Feet(1.0);
		assertTrue(f1.equals(f1), "Object must be equal to itself ");
		
	}
	
}
