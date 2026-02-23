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
