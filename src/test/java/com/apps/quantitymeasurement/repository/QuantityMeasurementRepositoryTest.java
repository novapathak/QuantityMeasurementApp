package com.apps.quantitymeasurement.repository;

import com.apps.quantitymeasurement.model.QuantityMeasurementEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class QuantityMeasurementRepositoryTest {

    @Autowired
    private QuantityMeasurementRepository repository;

    @Test
    void testJPARepositoryFindByOperationAndCount() {
        repository.save(createEntity("ADD", "LengthUnit", false, LocalDateTime.now().minusDays(1)));
        repository.save(createEntity("COMPARE", "LengthUnit", false, LocalDateTime.now()));
        repository.save(createEntity("COMPARE", "LengthUnit", true, LocalDateTime.now()));

        assertEquals(2, repository.findByOperationIgnoreCase("COMPARE").size());
        assertEquals(1, repository.countByOperationIgnoreCaseAndIsErrorFalse("COMPARE"));
    }

    @Test
    void testJPARepositoryCustomQueryAndFilters() {
        LocalDateTime now = LocalDateTime.now();
        repository.save(createEntity("ADD", "LengthUnit", false, now.minusHours(3)));
        repository.save(createEntity("ADD", "WeightUnit", true, now.minusHours(2)));
        repository.save(createEntity("COMPARE", "LengthUnit", false, now.minusHours(1)));

        List<QuantityMeasurementEntity> successfulAdds = repository.findSuccessfulByOperation("ADD");
        List<QuantityMeasurementEntity> errored = repository.findByIsErrorTrue();
        List<QuantityMeasurementEntity> byType = repository.findByThisMeasurementTypeIgnoreCase("LengthUnit");
        List<QuantityMeasurementEntity> recent = repository.findByCreatedAtAfter(now.minusHours(2).minusMinutes(1));

        assertEquals(1, successfulAdds.size());
        assertEquals(1, errored.size());
        assertEquals(2, byType.size());
        assertEquals(2, recent.size());
    }

    private QuantityMeasurementEntity createEntity(
            String operation,
            String measurementType,
            boolean isError,
            LocalDateTime createdAt
    ) {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setThisValue(1.0);
        entity.setThisUnit("FEET");
        entity.setThisMeasurementType(measurementType);
        entity.setThatValue(12.0);
        entity.setThatUnit("INCH");
        entity.setThatMeasurementType(measurementType);
        entity.setOperation(operation);
        entity.setResultValue(2.0);
        entity.setResultUnit("FEET");
        entity.setResultMeasurementType(measurementType);
        entity.setResultString("true");
        entity.setError(isError);
        entity.setErrorMessage(isError ? "Saved error" : null);
        entity.setCreatedAt(createdAt);
        entity.setUpdatedAt(createdAt);
        return entity;
    }
}
