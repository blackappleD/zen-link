package com.mkc.api.supplier.dto.jhsj;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/3/19 15:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JhsjPersonalVehicleResDTO extends JhsjBusinessResDTO {

	private int vehicleCount;
	private List<Vehicle> list;

	@Data
	public static class Vehicle {
		private String plateNum;
		private int vehicleType;
		private int plateColor;
	}
}
