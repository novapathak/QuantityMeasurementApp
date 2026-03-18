package com.apps.quantitymeasurement;

import java.sql.*;
import java.sql.Connection;
import java.sql.Statement;
import org.h2.tools.Server;
import com.apps.quantitymeasurement.controller.QuantityMeasurementController;
import com.apps.quantitymeasurement.core.IMeasurable;
import com.apps.quantitymeasurement.core.Quantity;
import com.apps.quantitymeasurement.model.QuantityDTO;
import com.apps.quantitymeasurement.repository.QuantityMeasurementDatabaseRepository;
import com.apps.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.apps.quantitymeasurement.service.IQuantityMeasurementService;
import com.apps.quantitymeasurement.service.QuantityMeasurementServiceImpl;
import com.apps.quantitymeasurement.units.LengthUnit;
import com.apps.quantitymeasurement.units.TemperatureUnit;
import com.apps.quantitymeasurement.units.VolumeUnit;
import com.apps.quantitymeasurement.units.WeightUnit;
import com.apps.quantitymeasurement.util.ConnectionPool;



/* public class QuantityMeasurementApp {

	public static <U extends IMeasurable> boolean demonstrateEquality(Quantity<U> q1, Quantity<U> q2) {
		return q1.equals(q2);
	}

	public static <U extends IMeasurable> Quantity<U> demonstrateConversion(Quantity<U> quantity, U targetUnit) {
		return quantity.convertTo(targetUnit);
	}

	public static <U extends IMeasurable> Quantity<U> demonstrateAddition(Quantity<U> q1, Quantity<U> q2,
			U targetUnit) {
		return q1.add(q2, targetUnit);
	}

	// Subtraction
	public static <U extends IMeasurable> Quantity<U> demonstrateSubtraction(Quantity<U> q1, Quantity<U> q2,
			U targetUnit) {
		return q1.subtract(q2, targetUnit);
	}

	// Division
	public static <U extends IMeasurable> double demonstrateDivision(Quantity<U> q1, Quantity<U> q2) {
		return q1.divide(q2);
	}

	public static void main(String[] args) {

		// LENGTH
		Quantity<LengthUnit> length1 = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<LengthUnit> length2 = new Quantity<>(12.0, LengthUnit.INCH);
		System.out.println("Length Equality: " + demonstrateEquality(length1, length2));
		System.out.println("Length Conversion: " + demonstrateConversion(length1, LengthUnit.INCH));
		System.out.println("Length Addition: " + demonstrateAddition(length1, length2, LengthUnit.FEET));
		System.out.println("Length Subtraction: " + demonstrateSubtraction(length1, length2, LengthUnit.FEET));
		System.out.println("Length Division: " + demonstrateDivision(length1, length2));

		// WEIGHT
		Quantity<WeightUnit> weight1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> weight2 = new Quantity<>(1000.0, WeightUnit.GRAM);
		System.out.println("\nWeight Equality: " + demonstrateEquality(weight1, weight2));
		System.out.println("Weight conversion: " + demonstrateConversion(weight1, WeightUnit.GRAM));
		System.out.println("Weight Addition: " + demonstrateAddition(weight1, weight2, WeightUnit.KILOGRAM));
		System.out.println("Weight Subtraction: " + demonstrateSubtraction(weight1, weight2, WeightUnit.KILOGRAM));
		System.out.println("Weight Division: " + demonstrateDivision(weight1, weight2));

		// VOLUME
		Quantity<VolumeUnit> volume1 = new Quantity<>(1.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> volume2 = new Quantity<>(25.0, VolumeUnit.GALLON);
		System.out.println("\nVolume Equality: " + demonstrateEquality(volume1, volume2));
		System.out.println("Volume conversion: " + demonstrateConversion(volume1, VolumeUnit.MILLILITRE));
		System.out.println("Volume Subtraction: " + demonstrateSubtraction(volume1, volume2, VolumeUnit.LITRE));
		System.out.println("Volume Division: " + demonstrateDivision(volume1, volume2));

		// TEMPERATURE (UC14)
		// =========================
		System.out.println("\n===== TEMPERATURE =====");

		Quantity<TemperatureUnit> temp1 = new Quantity<>(0.0, TemperatureUnit.CELSIUS);
		Quantity<TemperatureUnit> temp2 = new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT);

		System.out.println("Temperature Equality (0C == 32F): " + demonstrateEquality(temp1, temp2));

		System.out.println("Convert 100C to F: "
				+ demonstrateConversion(new Quantity<>(100.0, TemperatureUnit.CELSIUS), TemperatureUnit.FAHRENHEIT));

		System.out.println("Convert 273.15K to C: "
				+ demonstrateConversion(new Quantity<>(273.15, TemperatureUnit.KELVIN), TemperatureUnit.CELSIUS));

		System.out.println("Convert -40C to F: "
				+ demonstrateConversion(new Quantity<>(-40.0, TemperatureUnit.CELSIUS), TemperatureUnit.FAHRENHEIT));

		// Unsupported Operations Demo
		try {
			demonstrateAddition(temp1, temp2, TemperatureUnit.CELSIUS);
		} catch (UnsupportedOperationException e) {
			System.out.println("Addition Error: " + e.getMessage());
		}

		try {
			demonstrateSubtraction(temp1, temp2, TemperatureUnit.CELSIUS);
		} catch (UnsupportedOperationException e) {
			System.out.println("Subtraction Error: " + e.getMessage());
		}

		try {
			demonstrateDivision(temp1, temp2);
		} catch (UnsupportedOperationException e) {
			System.out.println("Division Error: " + e.getMessage());
		}

	}
}*/
public class QuantityMeasurementApp {

    public static void main(String[] args) {

        // Repository
       /* QuantityMeasurementDatabaseRepository repository =
                new QuantityMeasurementDatabaseRepository();

        // Service
        QuantityMeasurementServiceImpl service =
                new QuantityMeasurementServiceImpl(repository);

        // Controller
        QuantityMeasurementController controller =
                new QuantityMeasurementController(service);

        // Example 1: Comparison
        QuantityDTO q1 =
                new QuantityDTO(1, QuantityDTO.LengthUnit.FEET);

        QuantityDTO q2 =
                new QuantityDTO(12, QuantityDTO.LengthUnit.INCH);

        System.out.println("1 FEET equals 12 INCHES: "
                + controller.compare(q1, q2));

        // Example 2: Conversion
        QuantityDTO converted =
                controller.convert(q1, QuantityDTO.LengthUnit.INCH);

        System.out.println("1 FEET in INCHES: "
                + converted.value);

        // Example 3: Addition
        QuantityDTO added =
                controller.add(q1, q2);

        System.out.println("Addition result: "
                + added.value + " " + added.unit.getUnitName());

        // Example 4: Subtraction
        QuantityDTO subtracted =
                controller.subtract(q1, q2);

        System.out.println("Subtraction result: "
                + subtracted.value + " " + subtracted.unit.getUnitName());

        // Example 5: Division
        double result =
                controller.divide(q1, new QuantityDTO(2,
                        QuantityDTO.LengthUnit.FEET));

        System.out.println("Division result: " + result);
        */
    	initDatabase();
    	IQuantityMeasurementRepository repository =
    	        new QuantityMeasurementDatabaseRepository();  // Step 8

        IQuantityMeasurementService service =
                new QuantityMeasurementServiceImpl(repository);

        QuantityMeasurementController controller =
                new QuantityMeasurementController(service);
        
        QuantityDTO q1 =
                new QuantityDTO(1, QuantityDTO.LengthUnit.FEET);

        QuantityDTO q2 =
                new QuantityDTO(12, QuantityDTO.LengthUnit.INCH);

        System.out.println("1 FEET equals 12 INCHES: "
                + controller.compare(q1, q2));

        // Example 2: Conversion
        QuantityDTO converted =
                controller.convert(q1, QuantityDTO.LengthUnit.INCH);

        System.out.println("1 FEET in INCHES: "
                + converted.value);

        // Example 3: Addition
        QuantityDTO added =
                controller.add(q1, q2);

        System.out.println("Addition result: "
                + added.value + " " + added.unit.getUnitName());

        // Example 4: Subtraction
        QuantityDTO subtracted =
                controller.subtract(q1, q2);

        System.out.println("Subtraction result: "
                + subtracted.value + " " + subtracted.unit.getUnitName());

        // Example 5: Division
        double result =
                controller.divide(q1, new QuantityDTO(2,
                        QuantityDTO.LengthUnit.FEET));

        System.out.println("Division result: " + result);
        
        
        controller.add(
        	    new QuantityDTO(5, QuantityDTO.LengthUnit.CENTIMETERS),
        	    new QuantityDTO(2, QuantityDTO.LengthUnit.CENTIMETERS)
        	);

        	controller.add(
        	    new QuantityDTO(10, QuantityDTO.LengthUnit.FEET),
        	    new QuantityDTO(5, QuantityDTO.LengthUnit.FEET)
        	);

        	System.out.println("Total records: " + repository.getTotalCount());
    }
    private static void initDatabase() {
        try (Connection conn = ConnectionPool.getConnection();
             Statement stmt = conn.createStatement()) {

        	Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
        	
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS quantity_measurement (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    quantity_value DOUBLE,
                    unit VARCHAR(50),
                    measurement_type VARCHAR(50),
                    operation VARCHAR(50),
                    result DOUBLE,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

        } catch (Exception e) {
            throw new RuntimeException("DB Init failed", e);
        }
    }
    
}
