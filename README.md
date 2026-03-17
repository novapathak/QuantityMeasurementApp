# QuantityMeasurementApp
# Overview

## Quantity Measurement: UC1 Summary

## ✅ **Object Equality**
- Overrode `equals()` for value-based comparison (1.0 ft == 1.0 ft)

## 🔧 **Precision Handling**
- Used `Double.compare()` instead of `==` for accurate floating-point comparison

## 🛡️ **Type & Null Safety**
- Added type checks (`getClass()`) and null validation to prevent exceptions

## 🧪 **Test-Driven Development**
- Implemented unit tests covering equality, inequality, null, and self-reference cases

## 🏗️ **Encapsulation**
- Created immutable `Feet` class with private final field and constructor initialization

  -----

  # Quantity Measurement: UC2 Summary

## ✅ **Extending Equality to Inches**
- Added `Inches` class mirroring `Feet` for inch-based equality checks

## 🔧 **Consistent Implementation**
- Applied same `equals()` override, `Double.compare()`, null/type safety from UC1

## 🧪 **Comprehensive Testing**
- Replicated test cases for inches: same value, different value, null, self-reference

## ⚠️ **DRY Violation Identified**
- Duplicate code across `Feet` and `Inches` classes (identical constructors, equals logic)

## 🔮 **Design Improvement**
- Future refactor: unify with a generic `Quantity` class or unit parameter to eliminate redundancy

---
# Quantity Measurement Application

A Java-based project that demonstrates how different types of measurements can be compared, converted, and calculated using core object-oriented programming principles.

---

## UC1: Equality Check for Feet

### Overview  
This use case checks whether two values measured in feet are equal. It safely handles null values, type mismatches, and floating-point precision issues.

### Flow  
- Take two values in feet as input  
- Validate that both inputs are numeric  
- Compare them and return true or false  

### Concepts Used  
- equals() implemented using Double.compare() instead of ==  
- private final fields for immutability  
- null and type validation  

---

## UC2: Feet and Inches Equality

### Overview  
This extends the previous use case by supporting both feet and inches using separate classes. Static helper methods reduce dependency on the main method.

### Flow  
- A static method compares two feet values  
- Another static method compares two inches values  
- Returns true or false for each comparison  

### Concepts Used  
- Separate Inches class similar to Feet  
- Static methods for comparison  
- Code duplication (handled in UC3)  

---

## UC3: Generic Length Class

### Overview  
This refactors the design by introducing a single QuantityLength class along with a LengthUnit enum. It enables comparison across different units.

### Flow  
- Input value with unit type  
- Convert values into a base unit (feet)  
- Compare the converted values  

### Concepts Used  
- Enum for conversion factors  
- DRY principle  
- Base unit normalization  

---

## UC4: Support for Additional Units

### Overview  
Adds support for yards and centimeters without modifying the core logic.

### Flow  
- Accept value with unit type  
- Convert the value to feet  
- Perform comparison  

### Details  
- 1 yard = 3 feet  
- 1 centimeter ≈ 0.0328 feet  

---

## UC5: Unit Conversion

### Overview  
Introduces functionality to convert values between different units.

### Flow  
- Validate value and units  
- Convert value to base unit  
- Convert base unit to target unit  
- Return the result  

### Concepts Used  
- Method overloading  
- Helper methods for encapsulation  
- toString() for readability  

### Formula  
result = value × (source.factor / target.factor)

---

## UC6: Addition of Lengths

### Overview  
Allows addition of two measurements, even if they belong to different units. The result is returned in the unit of the first operand.

### Flow  
- Validate both inputs  
- Convert values to base unit  
- Perform addition  
- Convert result back to first unit  

### Concepts Used  
- Immutability (returns new object)  
- Commutative property  
- Overloaded methods  

---

## UC7: Addition with Target Unit

### Overview  
Allows specifying a custom unit for the result.

### Flow  
- Validate inputs and target unit  
- Convert values to base unit  
- Add them  
- Convert result to specified unit  

### Concepts Used  
- Method overloading  
- Reusable internal logic  
- Flexible unit handling  

---

## UC8: Standalone LengthUnit

### Overview  
Moves LengthUnit into a separate class and assigns conversion responsibility to it.

### Flow  
- LengthUnit performs conversions  
- QuantityLength delegates conversion logic  

### Concepts Used  
- Single Responsibility Principle  
- Scalable design  
- Clean separation of concerns  

---

## UC9: Weight Measurement

### Overview  
Introduces weight measurement using kilogram, gram, and pound units.

### Details  
- Base unit: kilogram  
- 1 gram = 0.001 kilogram  
- 1 pound = 0.453592 kilogram  

### Concepts Used  
- WeightUnit enum  
- Category type safety  
- equals() and hashCode() consistency  

---

## UC10: Generic Quantity Class

### Overview  
Combines all measurement types into a single generic class using a shared interface.

### Architecture  
- IMeasurable defines conversion behavior  
- Unit enums implement the interface  
- Quantity class handles operations  
- Application class demonstrates usage  

### Concepts Used  
- Generics (<U extends IMeasurable>)  
- Code reuse  
- Easy extensibility  

---

## UC11: Volume Measurement

### Overview  
Adds volume measurement without modifying existing logic.

### Details  
- Base unit: litre  
- 1 millilitre = 0.001 litre  
- 1 gallon = 3.78541 litres  

### Concepts Used  
- Plug-and-play design  
- No changes to core logic  
- Category isolation  

---

## UC12: Subtraction and Division

### Overview  
Adds subtraction and division operations.

### Operations  
- Subtraction returns a new quantity  
- Division returns a numeric value  

### Concepts Used  
- Non-commutative operations  
- Division by zero handling  
- Same-category validation  

---

## UC13: Centralized Arithmetic Logic

### Overview  
Refactors arithmetic operations to remove duplicate logic.

### Structure  
- ArithmeticOperation enum defines operations  
- Centralized validation method  
- Base unit computation handled in one place  

### Concepts Used  
- DRY principle  
- Consistent validation  
- Improved maintainability  

---

## Summary

This project evolves from simple equality checks to a scalable measurement system. It demonstrates object-oriented design, clean architecture, reusability, and strong type safety across multiple measurement categories.
