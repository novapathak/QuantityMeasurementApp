package com.apps.quantitymeasurement.service;

import com.apps.quantitymeasurement.model.QuantityDTO;

public interface IQuantityMeasurementService {

   public boolean compare(QuantityDTO q1, QuantityDTO q2);
   public QuantityDTO convert(QuantityDTO quantity, QuantityDTO.IMeasurableUnit targetUnit);
   public  QuantityDTO add(QuantityDTO q1, QuantityDTO q2);
   public  QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2);
   public double divide(QuantityDTO q1, QuantityDTO q2);

}