package com.smart.farm.business;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.smart.farm.models.FarmStatus;
import com.smart.farm.repositories.FarmStatusRepository;

@Service
public class DashboardServiceImp implements DashboardService {
	
	@Autowired
	private FarmStatusRepository statusRepository;
	// Dates
	Locale loc = new Locale("en", "US");
	SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy",loc);
	
	@SuppressWarnings("deprecation")
	@Override
	public List<FarmStatus> getDashboardStatus(String idSystem,Date date){
		int indexDay = 0;
		int indexMonth = 1;
		List<FarmStatus> resultList = new ArrayList<FarmStatus>();
		List<int[]> testArray = new ArrayList<int[]>();
		int consideredHour = 17;
		List<FarmStatus> statusList = statusRepository.findBySystemIdAndTypeAndDateInsertionAfter(idSystem,FarmStatus.InsertionType.ENUM_SCHEDULED.value,date);
		for (int i=0;i<statusList.size();i++) {
			int stateHour = statusList.get(i).getDateInsertion().getHours();
			int[] arrayExist = new int[2];
			arrayExist[indexDay]= statusList.get(i).getDateInsertion().getDate();
			arrayExist[indexMonth]= statusList.get(i).getDateInsertion().getMonth();
			if(stateHour>consideredHour && stateHour<consideredHour+5
					&& !containsArray(arrayExist, testArray)) {
				testArray.add(arrayExist);
				statusList.get(i).setDateInsertionStr(DATE_FORMAT.format(statusList.get(i).getDateInsertion()));
				resultList.add(statusList.get(i));
				
			}
		}		
		return resultList;
	}
	
	// Checks If date is Already in the list
	public boolean containsArray(int[] arrayDate,List<int[]> testArrayList ) {
		for(int i=0;i<testArrayList.size();i++) {
			if(testArrayList.get(i)[0] == arrayDate[0] && testArrayList.get(i)[1] == arrayDate[1]) {
				return true;
			}
		}
		return false;
	}
	

}
