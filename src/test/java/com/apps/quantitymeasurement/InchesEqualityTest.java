package com.apps.quantitymeasurement;
import static org.junit.jupiter.api.Assertions.*;
import  org.junit.jupiter.api.Test;


public class InchesEqualityTest {
	
	@Test
	
	public void testInchesEquality_SameValue() {
		Inches i1 = new Inches(1.0);
		Inches i2 = new Inches(1.0);
		assertTrue(i1.equals(i2));
	}
	
	@Test
	public void testInchesEquality_DifferentValue() {
		Inches i1 = new Inches(1.0);
		Inches i2 = new Inches(2.0);
		assertFalse(i1.equals(i2));
	}
	void testEquality_NullComparison() {
		Inches i1 = new Inches(1.0);
		assertFalse(i1.equals(null));
		
	}
	void testEquality_SameReference() {
		Inches i1 = new Inches(1.0);
		assertTrue(i1.equals(i1));
		
	}

}
