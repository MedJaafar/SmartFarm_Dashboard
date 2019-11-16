package com.smart.farm.business;

import com.smart.farm.models.SystemURL;

public interface IConnectionService {
	public SystemURL getCurrentSystemUrl (String systemID);
}
