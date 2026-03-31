package com.apps.quantitymeasurement;

import com.apps.quantitymeasurement.model.QuantityDTO;
import com.apps.quantitymeasurement.model.QuantityMeasurementDTO;
import com.apps.quantitymeasurement.repository.QuantityMeasurementRepository;
import com.apps.quantitymeasurement.service.QuantityMeasurementServiceImpl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class QuantityMeasurementAppTest {

    private final QuantityMeasurementRepository repository =
            mock(QuantityMeasurementRepository.class);

    private final QuantityMeasurementServiceImpl service =
            new QuantityMeasurementServiceImpl(repository);

    private QuantityDTO createDTO(double value, String unit, String type) {
        QuantityDTO dto = new QuantityDTO();
        dto.setValue(value);
        dto.setUnit(unit);
        dto.setMeasurementType(type);
        return dto;
    }

    @Test
    void testCompare() {
        QuantityDTO q1 = createDTO(1, "FEET", "LENGTH");
        QuantityDTO q2 = createDTO(12, "INCH", "LENGTH");

        QuantityMeasurementDTO result = service.compare(q1, q2);

        assertNotNull(result);
        assertEquals("COMPARE", result.getOperation());
        verify(repository, times(1)).save(any());
    }

    @Test
    void testAdd() {
        QuantityDTO q1 = createDTO(1, "FEET", "LENGTH");
        QuantityDTO q2 = createDTO(12, "INCH", "LENGTH");

        QuantityMeasurementDTO result = service.add(q1, q2);

        assertNotNull(result);
        assertEquals("ADD", result.getOperation());
        assertEquals(2.0, result.getResultValue(), 0.01);
        verify(repository, times(1)).save(any());
    }

    @Test
    void testSubtract() {
        QuantityDTO q1 = createDTO(10, "FEET", "LENGTH");
        QuantityDTO q2 = createDTO(5, "FEET", "LENGTH");

        QuantityMeasurementDTO result = service.subtract(q1, q2);

        assertNotNull(result);
        assertEquals("SUBTRACT", result.getOperation());
        assertEquals(5.0, result.getResultValue(), 0.01);
        verify(repository, times(1)).save(any());
    }

    @Test
    void testDivide() {
        QuantityDTO q1 = createDTO(10, "FEET", "LENGTH");
        QuantityDTO q2 = createDTO(2, "FEET", "LENGTH");

        QuantityMeasurementDTO result = service.divide(q1, q2);

        assertNotNull(result);
        assertEquals("DIVIDE", result.getOperation());
        assertEquals(5.0, result.getResultValue(), 0.01);
        verify(repository, times(1)).save(any());
    }
}