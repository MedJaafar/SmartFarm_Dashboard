package com.smart.farm.RestControllers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.smart.farm.business.DashboardService;
import com.smart.farm.models.FarmStatus;
import com.smart.farm.models.FarmSystem;
import com.smart.farm.repositories.FarmStatusRepository;
import com.smart.farm.repositories.FarmSystemRepository;

@RestController
public class DashboardController {
	
	private static final String GET_STATUS_CHART_DATA_PATH ="/getStatusChart";
	private static final int INDEX_WATERING_LIST =0;
	private static final int INDEX_DASHBOARD_LIST =1;
	private static final int INDEX_SYSTEM_INFO_LIST =2;
	private static final int INDEX_WATERING_DATA_LIST = 3;
	private final static int DAY_SEARCH_MODE = 1;
	private final static int WEEK_SEARCH_MODE = 2;
	private final static int MONTH_SEARCH_MODE = 3;
	
	@Autowired
	DashboardService dashboardService;
	@Autowired
	FarmSystemRepository systemRepository;
	@Autowired 
	FarmStatusRepository statusRepository;
	
	
	@GetMapping(GET_STATUS_CHART_DATA_PATH)
	public Object[] getDashboardStatusMonth(
	@RequestParam(name="timeMode",defaultValue="1")int timeMode,
	@RequestParam(name="selectedHours",defaultValue="16")String selectedHours,
	@RequestParam(name="idSystem",defaultValue="1")String idSystem){
	 FarmSystem system = systemRepository.findBySystemID(idSystem);
	 Object[] arrayRet = new Object[4] ; 
	 Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		switch(timeMode) {
		case DAY_SEARCH_MODE :
			calendar.add(Calendar.HOUR_OF_DAY, -24);
			break;
		case WEEK_SEARCH_MODE :
			calendar.add(Calendar.DAY_OF_MONTH, -7);
			break;
		case MONTH_SEARCH_MODE :
			calendar.add(Calendar.MONTH, -1);
			break;
		}	
	List<FarmStatus>inputList = statusRepository.findBySystemIdAndDateInsertionAfter(idSystem, calendar.getTime(), new Sort(Sort.Direction.ASC,"dateInsertion"));
	 arrayRet[INDEX_DASHBOARD_LIST] = dashboardService.getDashboardStatus(idSystem, timeMode,Integer.parseInt(selectedHours));
	 arrayRet[INDEX_WATERING_LIST] = dashboardService.getWateringData(inputList,timeMode, idSystem, system.getPumpDebit());
	 arrayRet[INDEX_SYSTEM_INFO_LIST] = system;
	 arrayRet[INDEX_WATERING_DATA_LIST] = dashboardService.getWaterChart(inputList, system.getPumpDebit(), timeMode);
	 
	 return arrayRet;
	}
}
