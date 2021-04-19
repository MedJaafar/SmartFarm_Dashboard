package com.smart.farm.business;

import java.util.List;

import com.smart.farm.models.FarmStatus;
import com.smart.farm.models.WateringData;

public interface DashboardService {
	public List<FarmStatus> getDashboardStatus(String idSystem,int modeType,int selectedHours);
	public Object[] getWateringData(List<FarmStatus> inputList,int timeMode,String systemId,int pumpDebit);
	public List<WateringData> getWaterChart(List<FarmStatus> inputList,int pumpDebit, int modeType);
}
