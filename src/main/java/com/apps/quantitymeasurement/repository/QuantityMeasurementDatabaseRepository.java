package com.apps.quantitymeasurement.repository;

import com.apps.quantitymeasurement.model.QuantityMeasurementEntity;
import com.apps.quantitymeasurement.exception.DatabaseException;
import com.apps.quantitymeasurement.util.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuantityMeasurementDatabaseRepository implements IQuantityMeasurementRepository {

	@Override
	public void save(QuantityMeasurementEntity entity) {
	    try (Connection conn = ConnectionPool.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(
	             "INSERT INTO quantity_measurement " +
	             "(quantity_value, unit, measurement_type, operation, result) VALUES (?, ?, ?, ?, ?)"
	         )) {

	        stmt.setDouble(1, entity.getValue());
	        stmt.setString(2, entity.getUnit());
	        stmt.setString(3, entity.getType());
	        stmt.setString(4, entity.getOperation());
	        stmt.setDouble(5, entity.getResult());

	        stmt.executeUpdate();

	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        List<QuantityMeasurementEntity> list = new ArrayList<>();
        String sql = "SELECT * FROM quantity_measurement";

        try (Connection conn = ConnectionPool.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
                entity.setValue(rs.getDouble("value"));
                entity.setUnit(rs.getString("unit"));
                entity.setType(rs.getString("type"));
                entity.setOperation(rs.getString("operation"));
                entity.setResult(rs.getDouble("result"));

                list.add(entity);
            }

        } catch (Exception e) {
            throw new DatabaseException("Error fetching data", e);
        }

        return list;
    }

    @Override
    public int getTotalCount() {
        String sql = "SELECT COUNT(*) FROM quantity_measurement";

        try (Connection conn = ConnectionPool.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            rs.next();
            return rs.getInt(1);

        } catch (Exception e) {
            throw new DatabaseException("Count failed", e);
        }
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM quantity_measurement";

        try (Connection conn = ConnectionPool.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);

        } catch (Exception e) {
            throw new DatabaseException("Delete failed", e);
        }
    }

	@Override
	public List<QuantityMeasurementEntity> getMeasurementsByOperation(String operation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<QuantityMeasurementEntity> getMeasurementsByType(String type) {
		// TODO Auto-generated method stub
		return null;
	}
}