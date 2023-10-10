package com.bartosztanski.BlogApp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@CrossOrigin(origins = {"https://bartosztanski.azurewebsites.net/","http://localhost:3000"})
@RequestMapping("/")
public class HomeController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
	
	@GetMapping("/")
	public ModelAndView home() {
		LOGGER.info("Inside HomeController.htmlTest");
		
	    ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("test.html");
	    return modelAndView;
	}
	
}
