# QuantityMeasurementApp
# Overview

 Quantity Measurement: UC1 Summary

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
