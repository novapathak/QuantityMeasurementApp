package com.apps.quantitymeasurement;

import com.apps.quantitymeasurement.model.QuantityDTO;
import com.apps.quantitymeasurement.model.QuantityMeasurementDTO;
import com.apps.quantitymeasurement.model.QuantityMeasurementEntity;
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

    public QuantityMeasurementAppTest() {
        when(repository.save(any(QuantityMeasurementEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void testCompare() {
        QuantityDTO q1 = createDTO(1, "FEET", "LengthUnit");
        QuantityDTO q2 = createDTO(12, "INCHES", "LengthUnit");

        QuantityMeasurementDTO result = service.compare(q1, q2);

        assertNotNull(result);
        assertEquals("COMPARE", result.getOperation());
        verify(repository, times(1)).save(any());
    }

    @Test
    void testAdd() {
        QuantityDTO q1 = createDTO(1, "FEET", "LengthUnit");
        QuantityDTO q2 = createDTO(12, "INCHES", "LengthUnit");

        QuantityMeasurementDTO result = service.add(q1, q2);

        assertNotNull(result);
        assertEquals("ADD", result.getOperation());
        assertEquals(2.0, result.getResultValue(), 0.01);
        verify(repository, times(1)).save(any());
    }

    @Test
    void testSubtract() {
        QuantityDTO q1 = createDTO(10, "FEET", "LengthUnit");
        QuantityDTO q2 = createDTO(5, "FEET", "LengthUnit");

        QuantityMeasurementDTO result = service.subtract(q1, q2);

        assertNotNull(result);
        assertEquals("SUBTRACT", result.getOperation());
        assertEquals(5.0, result.getResultValue(), 0.01);
        verify(repository, times(1)).save(any());
    }

    @Test
    void testDivide() {
        QuantityDTO q1 = createDTO(10, "FEET", "LengthUnit");
        QuantityDTO q2 = createDTO(2, "FEET", "LengthUnit");

        QuantityMeasurementDTO result = service.divide(q1, q2);

        assertNotNull(result);
        assertEquals("DIVIDE", result.getOperation());
        assertEquals(5.0, result.getResultValue(), 0.01);
        verify(repository, times(1)).save(any());
    }

    @Test
    void testMultiply() {
        QuantityDTO q1 = createDTO(2, "FEET", "LengthUnit");
        QuantityDTO q2 = createDTO(3, "FEET", "LengthUnit");

        QuantityMeasurementDTO result = service.multiply(q1, q2);

        assertNotNull(result);
        assertEquals("MULTIPLY", result.getOperation());
        assertEquals(6.0, result.getResultValue(), 0.01);
        verify(repository, times(1)).save(any());
    }

    @Test
    void testConvert() {
        QuantityDTO source = createDTO(1, "FEET", "LengthUnit");
        QuantityDTO target = createDTO(0, "INCHES", "LengthUnit");

        QuantityMeasurementDTO result = service.convert(source, target);

        assertNotNull(result);
        assertEquals("CONVERT", result.getOperation());
        assertEquals(12.0, result.getResultValue(), 0.01);
        verify(repository, times(1)).save(any());
    }
}
