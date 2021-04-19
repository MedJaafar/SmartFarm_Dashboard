package com.smart.farm.RestControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.smart.farm.business.IConnectionService;
import com.smart.farm.mdbServices.NextSequenceService;
import com.smart.farm.models.FarmSystem;
import com.smart.farm.models.SystemURL;
import com.smart.farm.repositories.FarmSystemRepository;

@RestController
public class SystemController {

	private static final String ADD_NEW_SYSTEM_PATH = "/addsystem";
	private static final String GET_CURRENT_CONNECTION_URL_PATH = "/getcurrenturl";
	private static final String GET_CURRENT_SYSTEM_MONITR = "/getcurrentsystem";
	private static final String TOGGLE_AUTO_WATERING_PATH = "/toggleSystemWatering";
	
	@Autowired 
	private FarmSystemRepository farmRepository;
	@Autowired 
	private NextSequenceService nextsequenceService;
	@Autowired
	private IConnectionService connectionService;
	
	@GetMapping(ADD_NEW_SYSTEM_PATH)
	public String addNewSystem() {
		FarmSystem farmSystem = new FarmSystem();
		farmSystem.setSystemID(Integer.toString(nextsequenceService.getNextSequence(NextSequenceService.FARM_SYSTEM_SEQ)));
		farmSystem.setBuildingNumber(14);
		farmSystem.setCity("Metline");
		farmSystem.setCountry("Tunisia");
		farmSystem.setPostalCode(7034);
		farmSystem.setSystemName("Metline_Farm");
		farmSystem.setVoieName("Demna");
		farmSystem.setSystemCode("farmMetline1Code");
		// Set Longitude & Latitude
			double [] localisation = new double [2];
			localisation[0]= 36.833142;
			localisation[1]= 10.319130;
		farmSystem.setLocalisation(localisation);
		farmRepository.save(farmSystem);
	return "ok"; 
	}
	
	// Load current active url by choosen systemID
	@GetMapping(GET_CURRENT_CONNECTION_URL_PATH+"/{systemID}")
	public SystemURL getCurrentSystemUrl(@PathVariable String systemID) {
		SystemURL url= connectionService.getCurrentSystemUrl(systemID);
		return url;
	}
	
	@GetMapping(GET_CURRENT_SYSTEM_MONITR+"/{systemID}")
	public FarmSystem getCurrentSystem(@PathVariable String systemID) {
		FarmSystem system = farmRepository.findBySystemID(systemID);
		return system;	
	}
	
	
	// TOGGLE ENABLE AUTOWATERING
	@GetMapping(TOGGLE_AUTO_WATERING_PATH+"/{systemID}")
	public FarmSystem toggleSystemWatering(@PathVariable String systemID) {
		FarmSystem system = farmRepository.findBySystemID(systemID);
		if(system != null) {
			system.setbEnableWatering(!system.isbEnableWatering());
		return farmRepository.save(system);
		}
		return null;	
	}
}
