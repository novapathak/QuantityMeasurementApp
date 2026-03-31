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

A Java-based project that demonstrates how different types of measurements can be **compared**, **converted**, and **calculated** using core **Object-Oriented Programming (OOP)** principles.

---

## UC1: Equality Check for Feet

### Overview  
This use case checks whether two values measured in feet are equal. It safely handles **null values**, **type mismatches**, and **floating-point precision issues**.

### Flow  
- Take two values in **feet** as input  
- Validate that both inputs are **numeric**  
- Compare them and return `true` or `false`  

### Concepts Used  
- `equals()` implemented using **Double.compare()** instead of `==`  
- Use of **private final fields** for immutability  
- Proper **null and type validation**  

---

## UC2: Feet and Inches Equality

### Overview  
Extends the previous use case by supporting both **feet** and **inches** using separate classes. Introduces **static helper methods** to reduce dependency on the main method.

### Flow  
- Static method compares two **feet values**  
- Static method compares two **inch values**  
- Returns `true` or `false`  

### Concepts Used  
- Separate `Inches` class similar to `Feet`  
- Use of **static methods**  
- Presence of **code duplication** (resolved in UC3)  

---

## UC3: Generic Length Class

### Overview  
Refactors the design into a single **QuantityLength class** using a **LengthUnit enum**, enabling comparison across different units.

### Flow  
- Input **value + unit**  
- Convert values into a **base unit (feet)**  
- Compare the converted values  

### Concepts Used  
- **Enum-based conversion factors**  
- **DRY principle** (no duplication)  
- **Base unit normalization**  

---

## UC4: Support for Additional Units

### Overview  
Adds support for **yards** and **centimeters** without modifying core logic.

### Flow  
- Accept value with **unit type**  
- Convert to **feet (base unit)**  
- Perform comparison  

### Details  
- `1 yard = 3 feet`  
- `1 centimeter ≈ 0.0328 feet`  

---

## UC5: Unit Conversion

### Overview  
Introduces conversion between different units using a **standard conversion formula**.

### Flow  
- Validate **value and units**  
- Convert to **base unit**  
- Convert to **target unit**  
- Return result  

### Concepts Used  
- **Method overloading**  
- **Encapsulation using helper methods**  
- `toString()` for better readability  

### Formula  
`result = value × (source.factor / target.factor)`

---

## UC6: Addition of Lengths

### Overview  
Allows addition of two measurements, even with different units. Result is returned in the **unit of the first operand**.

### Flow  
- Validate inputs  
- Convert both to **base unit**  
- Perform **addition**  
- Convert result back  

### Concepts Used  
- **Immutability** (returns new object)  
- **Commutative property** (`A + B = B + A`)  
- **Method overloading**  

---

## UC7: Addition with Target Unit

### Overview  
Allows specifying a **custom target unit** for the result.

### Flow  
- Validate operands and **target unit**  
- Convert to **base unit**  
- Add values  
- Convert result to target unit  

### Concepts Used  
- **Flexible API design**  
- **Reusable internal logic**  
- Avoids **code duplication**  

---

## UC8: Standalone LengthUnit

### Overview  
Extracts `LengthUnit` into a standalone enum and assigns it the **conversion responsibility**.

### Flow  
- `LengthUnit` handles conversions  
- `QuantityLength` delegates logic  

### Concepts Used  
- **Single Responsibility Principle (SRP)**  
- **Clean architecture**  
- Improved **scalability**  

---

## UC9: Weight Measurement

### Overview  
Introduces weight measurement using **kilogram**, **gram**, and **pound**.

### Details  
- Base unit: **kilogram**  
- `1 gram = 0.001 kilogram`  
- `1 pound = 0.453592 kilogram`  

### Concepts Used  
- `WeightUnit` enum  
- **Category-level type safety**  
- Consistent `equals()` and `hashCode()`  

---

## UC10: Generic Quantity Class

### Overview  
Unifies all measurement types into a single **generic class** using a common interface.

### Architecture  
- `IMeasurable` → defines conversion behavior  
- Unit enums → implement interface  
- `Quantity<U>` → handles operations  

### Concepts Used  
- **Generics (<U extends IMeasurable>)**  
- High **code reusability**  
- Easy **extensibility**  

---

## UC11: Volume Measurement

### Overview  
Adds volume measurement without changing existing code.

### Details  
- Base unit: **litre**  
- `1 millilitre = 0.001 litre`  
- `1 gallon = 3.78541 litres`  

### Concepts Used  
- **Plug-and-play architecture**  
- No change in **core logic**  
- Strong **category isolation**  

---

## UC12: Subtraction and Division

### Overview  
Adds subtraction and division operations with proper validation.

### Operations  
- `subtract()` → returns **Quantity<U>**  
- `divide()` → returns **double**  

### Concepts Used  
- **Non-commutative operations**  
- **Division by zero handling**  
- **Same-category enforcement**  

---

## UC13: Centralized Arithmetic Logic

### Overview  
Refactors arithmetic operations to remove duplication and centralize logic.

### Structure  
- `ArithmeticOperation` enum → defines operations  
- Centralized **validation method**  
- Shared **base unit computation**  

### Concepts Used  
- **DRY principle**  
- **Consistent validation**  
- Better **maintainability**  

---

## Summary

This project evolves from basic equality checks to a **fully scalable measurement system**. It demonstrates strong use of **OOP concepts**, **clean architecture**, **reusability**, and **type safety** across multiple measurement categories.

---

## ✅ UC14: TEMPERATURE MEASUREMENT (NON-LINEAR UNITS)

### 📌 DESCRIPTION
Handles temperature conversion and comparison where units are **non-linear** and do not support arithmetic operations.

### ⭐ IMPORTANT POINTS
- Conversion is **formula-based (NOT multiplicative)**
- Base unit: **Celsius**
- Supports **Celsius and Fahrenheit**
- Enables **cross-unit comparison**
- ❌ Arithmetic operations are NOT allowed
- Throws **UnsupportedOperationException**
- Maintains **backward compatibility**

### 🌡️ CONVERSION FORMULA
C = (F − 32) × 5/9

### 🔁 BEHAVIOR EXAMPLES

#### ✔️ Equality
0°C == 32°F → true
100°C == 212°F → true
-40°C == -40°F → true

#### 🔄 Conversion
0°C → 32°F
50°C → 122°F

### ❌ RESTRICTED OPERATIONS
- Addition ❌  
- Subtraction ❌  
- Multiplication ❌  
- Division ❌  

---

## ------------------------------------------------------------

---

## ✅ UC15: N-TIER ARCHITECTURE REFACTOR

### 📌 DESCRIPTION
Refactors the application into a **professional layered (N-Tier) architecture**.

### 📦 PACKAGE STRUCTURE
com.apps.quantitymeasurement
├── controller/
├── service/
├── repository/
├── entity/
├── exception/
├── unit/
└── core/

### ⭐ IMPORTANT POINTS
- Clear **separation of concerns**
- Introduced:
  - `QuantityMeasurementEntity` → Database layer
  - `QuantityDTO` → Data transfer layer
- Improved **code maintainability**
- Structured for **scalability and enterprise development**

---

## ------------------------------------------------------------

---

## ✅ UC16: JDBC DATABASE INTEGRATION

### 📌 DESCRIPTION
Introduces **database persistence using JDBC with H2 embedded database**.

### ⭐ IMPORTANT POINTS
- Uses **H2 Embedded Database**
- Implements **Connection Pooling**
- Supports **Transaction Management (commit/rollback)**
- Prevents SQL Injection using **PreparedStatement**
- Provides full **CRUD operations**
- Includes **unit and integration testing**

### 🛠️ REPOSITORY OPERATIONS

| METHOD                  | DESCRIPTION                      |
|------------------------|----------------------------------|
| save()                 | Save data to database           |
| getAllMeasurements()   | Retrieve all records            |
| findByOperation()      | Filter by operation             |
| findByMeasurementType()| Filter by measurement type      |
| getCount()             | Get total record count          |
| deleteAll()            | Delete all records              |
| getPoolStatistics()    | View connection pool stats      |
| releaseResources()     | Close all connections           |

### 🗄️ DATABASE SCHEMA
- `quantity_measurement_entity` → Main table  
- `quantity_measurement_history` → Audit trail  

### ⚙️ CONFIGURATION
```properties
app.repository.type=database
-----------------------------------------------------------
