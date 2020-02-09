package com.estrok.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.estrok.web.service.MainService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class MainController {
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	MainService service;
	
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/data1", method = RequestMethod.GET)
	public ModelAndView data1(@RequestParam Map<String, Object> params,HttpServletRequest request, HttpServletResponse response) {
		logger.info("data1");
		
		ModelAndView mv = new ModelAndView();
		

		try {
			if(params.get("stockCode")!=null) {
				service.getHistoricPriceVolume(params, mv);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mv.setViewName("stockList");
		
		return mv;
		
	}
	
}
