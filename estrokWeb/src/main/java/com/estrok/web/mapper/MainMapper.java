package com.estrok.web.mapper;

import java.util.List;
import java.util.Map;

public interface MainMapper {

	public String selectNow() throws Exception;

	public List<Map<String, Object>> getHistoricPriceVolume(Map<String, Object> params) throws Exception;
	
}
