package com.smart.farm.RestControllers;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smart.farm.models.FarmStatus;
import com.smart.farm.repositories.FarmStatusRepository;

@RestController
public class DataStatusController {
	
	private final static int HOUR_SEARCH_MODE = 0;
	private final static int DAY_SEARCH_MODE = 1;
	private final static int WEEK_SEARCH_MODE = 2;
	private final static int MONTH_SEARCH_MODE = 3;
	private final static String GET_SEARCH_STATUS_PATH = "/getstatuspage";
	
	@Autowired
	private FarmStatusRepository statusRepository;
	
	@SuppressWarnings({"deprecation"})
	@GetMapping(GET_SEARCH_STATUS_PATH)
	public Page <FarmStatus> getStatusTableByPage(
		@RequestParam(name="timeMode",defaultValue="0")String timeMode, 
		@RequestParam(name="type",defaultValue="0") int type, 
		@RequestParam(name="page",defaultValue="0") int page,
		@RequestParam(name="size",defaultValue="5") int size){
		// Use Calendar to Add and subsruct Time -> Easy way to do it
		int intMode = Integer.parseInt(timeMode);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		switch(intMode) {
		case HOUR_SEARCH_MODE : 
			calendar.add(Calendar.HOUR_OF_DAY, -1);
			break;
		case DAY_SEARCH_MODE :
			calendar.add(Calendar.HOUR_OF_DAY, -24);
			break;
		case WEEK_SEARCH_MODE :
			calendar.add(Calendar.DAY_OF_MONTH, -7);
			break;
		case MONTH_SEARCH_MODE :
			calendar.add(Calendar.MONTH, -1);
			size = 20;
			break;
		}
		if(type == 0) {
			return statusRepository.findBySystemIdAndDateInsertionAfter("1",calendar.getTime(), new PageRequest(page, size,Sort.by("dateInsertion").descending()));
		} else {
			return statusRepository.findBySystemIdAndTypeAndDateInsertionAfter("1", type, calendar.getTime(), new PageRequest(page, size,Sort.by("dateInsertion").descending()));
		}
	}
 }
