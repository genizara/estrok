package com.estrok.web.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.estrok.web.mapper.MainMapper;
import com.google.gson.Gson;

@Service
public class MainService {
	
	@Autowired
	MainMapper mapper;
	
	public void getHistoricPriceVolume(Map<String, Object> params, ModelAndView mv) {
		try {
			
			String stockCode = String.valueOf(params.get("stockCode"));
			
			params.put("stockCode", params.get("stockCode"));
			List<Map<String, Object>> list = mapper.getHistoricPriceVolume(params);
			
			mv.addObject("list", list );
			mv.addObject("listJson", new Gson().toJson(list).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
