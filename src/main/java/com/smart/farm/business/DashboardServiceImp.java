package com.smart.farm.business;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.smart.farm.models.FarmStatus;
import com.smart.farm.models.WateringData;
import com.smart.farm.repositories.FarmStatusRepository;

@Service
public class DashboardServiceImp implements DashboardService {
	
	@Autowired
	private FarmStatusRepository statusRepository;
	
	// Dates
	private static Locale loc = new Locale("en", "US");
	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM",loc); 
	private static SimpleDateFormat DATE_FORMAT_WEEK = new SimpleDateFormat("EEE, dd MMM",loc); 
	private static SimpleDateFormat DATE_FORMAT_HOURS = new SimpleDateFormat("HH",loc);
	
	private final static int DAY_SEARCH_MODE = 1;
	private final static int WEEK_SEARCH_MODE = 2;
	private final static int MONTH_SEARCH_MODE = 3;
	
	
	@SuppressWarnings("deprecation")
	@Override
	public List<FarmStatus> getDashboardStatus(String idSystem,int modeType,int selectedHours){
		
		int indexDay = 0;
		int indexMonth = 1;
		int considHourBegin = 0;
		int considHourEnd = 0;
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		
		switch(modeType) {
		case DAY_SEARCH_MODE :
			calendar.add(Calendar.HOUR_OF_DAY, -24);
			considHourBegin = -1; 
			considHourEnd = 24;
			break;
		case WEEK_SEARCH_MODE :
			calendar.add(Calendar.DAY_OF_MONTH, -7);
			considHourBegin = selectedHours-1; 
			considHourEnd = selectedHours+2;
			break;
		case MONTH_SEARCH_MODE :
			calendar.add(Calendar.MONTH, -1);
			 considHourBegin = selectedHours-1; 
			 considHourEnd = selectedHours+2;
			break;
		}
		
		List<FarmStatus> resultList = new ArrayList<FarmStatus>();
		List<int[]> testArray = new ArrayList<int[]>();
		List<FarmStatus> statusList = statusRepository.findBySystemIdAndTypeAndDateInsertionAfter(idSystem,FarmStatus.InsertionType.ENUM_SCHEDULED.value,calendar.getTime(),new Sort(Sort.Direction.ASC,"dateInsertion"));
		for (int i=0;i<statusList.size();i++) {
			int stateHour = statusList.get(i).getDateInsertion().getHours();
			int[] arrayExist = new int[2];
			if(modeType == MONTH_SEARCH_MODE ) {
				arrayExist[indexDay]= statusList.get(i).getDateInsertion().getDate();
				arrayExist[indexMonth]= statusList.get(i).getDateInsertion().getMonth(); 
			}
			else if (modeType == DAY_SEARCH_MODE ) {
				arrayExist[indexDay]= statusList.get(i).getDateInsertion().getDate();
				arrayExist[indexMonth]= statusList.get(i).getDateInsertion().getHours();
			}
			else if (modeType == WEEK_SEARCH_MODE ) {
				arrayExist[indexDay]= statusList.get(i).getDateInsertion().getDate();
				arrayExist[indexMonth]= statusList.get(i).getDateInsertion().getMonth();
			}
			if(stateHour>considHourBegin && stateHour<considHourEnd
					&& !containsArray(arrayExist, testArray)) {
				testArray.add(arrayExist);
				if(modeType == DAY_SEARCH_MODE) {statusList.get(i).setDateInsertionStr(DATE_FORMAT_HOURS.format(statusList.get(i).getDateInsertion())+"h"); }
				else if(modeType == MONTH_SEARCH_MODE) {statusList.get(i).setDateInsertionStr(DATE_FORMAT.format(statusList.get(i).getDateInsertion()));}
				else if(modeType == WEEK_SEARCH_MODE) {statusList.get(i).setDateInsertionStr(DATE_FORMAT_WEEK.format(statusList.get(i).getDateInsertion()));}
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
	
	
	// Watering Data 
	@Override
	public Object[] getWateringData(List<FarmStatus> statusList,int timeMode,String systemId,int pumpDebit) {
		double waterVolume = 0;
		double totalWateringTime = 0;
		int wateringTimeInt = 0;
		for(FarmStatus status : statusList) {
			totalWateringTime = totalWateringTime + status.getWateringDuration();
			wateringTimeInt = wateringTimeInt + status.getWateringDuration();
		}
		// Water Volume -> looking for volume water used during this period
		// pumpDebit -> Default 120 L/H == 120L/3600 Seconds
		waterVolume = (pumpDebit*totalWateringTime)/3600;
		String wateringTimeStr = wateringTimeInt/60+"m "+wateringTimeInt%60+"s";
		Object[] arrayRet = new Object[2];
		arrayRet[0]= waterVolume;
		arrayRet[1]= wateringTimeStr;
		return(arrayRet) ;
	}
	
	// WATER CHART BUSINESS
	@Override
	public List<WateringData> getWaterChart(List<FarmStatus> inputList,int pumpDebit, int modeType) {
	List<int[]> dateListArr = new ArrayList<int[]>();
	List<WateringData> outputList = new ArrayList<WateringData>();
	  for(int i=0;i<inputList.size();i++) {
	    int day = inputList.get(i).getDateInsertion().getDate();
	    @SuppressWarnings("deprecation")
		int month = inputList.get(i).getDateInsertion().getMonth();
	    if(modeType == DAY_SEARCH_MODE) {
	    	month = inputList.get(i).getDateInsertion().getHours();
	    }
	    int[] arrayExist = new int[2];
	    arrayExist[0] = day;
	    arrayExist[1] = month;
	    if(!containsArray(arrayExist,dateListArr)){
	    	dateListArr.add(arrayExist);
	    	double waterVol = 0.0;
	    	double waterTime = 0.0;
	    	double moisitureAvg =0.0;
	    	int j = 0;
	    	int size=0;
	    	int x = 0;    // Last token index 
	    	for(j=0; j<inputList.size(); j++) {
		    	if(inputList.get(j).getDateInsertion().getDate() == day && (inputList.get(j).getDateInsertion().getMonth() == month || inputList.get(j).getDateInsertion().getHours() == month )) {
		    		waterTime = waterTime + inputList.get(j).getWateringDuration();
		    		moisitureAvg = moisitureAvg + inputList.get(j).getValHumiditySoil();
		    		size ++;
		    		x=j;
		    	}
		    }
	    	if(size > 0) {
	    		if(modeType == DAY_SEARCH_MODE) {
	    			inputList.get(x).setDateInsertionStr(DATE_FORMAT_HOURS.format(inputList.get(x).getDateInsertion())+"h"); 
	    			} else if(modeType == MONTH_SEARCH_MODE) {
	    				inputList.get(x).setDateInsertionStr(DATE_FORMAT.format(inputList.get(x).getDateInsertion()));
	    			} else if(modeType == WEEK_SEARCH_MODE) {
	    				inputList.get(x).setDateInsertionStr(DATE_FORMAT_WEEK.format(inputList.get(x).getDateInsertion()));
	    			}
	    		waterVol = (pumpDebit * waterTime) / 3600;
	    		moisitureAvg = moisitureAvg / size;
	    		WateringData data = new WateringData(waterVol, waterTime, inputList.get(x).getDateInsertionStr(),moisitureAvg);
	    		outputList.add(data);
	    	}
	    }		
	  }
	  return outputList;
	}
}
