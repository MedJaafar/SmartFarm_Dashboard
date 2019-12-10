package com.smart.farm.business;

import java.util.Date;
import java.util.List;

import com.smart.farm.models.FarmStatus;

public interface DashboardService {
	public List<FarmStatus> getDashboardStatus(String idSystem,Date date);
}
