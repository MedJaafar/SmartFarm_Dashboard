package com.smart.farm.RestControllers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart.farm.business.DashboardService;
import com.smart.farm.models.FarmStatus;

@RestController
public class DashboardController {
	
	@Autowired
	DashboardService dashboardService;
	
	@GetMapping("/getStatusLastMonth")
	public List<FarmStatus> getDashboardStatusMonth(){
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(new Date());
	calendar.add(Calendar.MONTH, -1);
	return dashboardService.getDashboardStatus("1", calendar.getTime());
	}
}
