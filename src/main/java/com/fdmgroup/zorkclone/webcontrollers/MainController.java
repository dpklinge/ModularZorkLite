package com.fdmgroup.zorkclone.webcontrollers;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.fdmgroup.zorkclone.ZorkController;

@Controller
@ComponentScan(basePackages={"com.fdmgroup"})
public class MainController {
	@Autowired
	private ServletContext servletContext;
	@Autowired
	private ZorkController controller;

	@GetMapping(value = "/")
	public String landingPage(HttpSession session) {
		return "index";
	}

	@PostConstruct
	public void loadFiles() {
		Logger logger = LoggerFactory.getLogger("FileLogger");
		logger.trace("Beginning postconstruct");
		System.out.println("==================================");
		System.out.println("Loading initial files");
		try {
			String ip = InetAddress.getLocalHost().getHostAddress();
			System.out.println("Local ip address:" + ip);
			logger.trace("Local ip address:" + ip);

			servletContext.setAttribute("ipAddress", ip);
			logger.trace("Finishing IP");

		} catch (UnknownHostException e) {
			System.out.println("=======================================================");
			System.out.println("=======================================================");
			System.out.println("=======================================================");
			System.out.println("=======================================================");
			System.out.println("=======================================================");
			System.out.println("Main controller post-construct error");
			e.printStackTrace();
		}

		System.out.println("Getting controller");
		controller = new ZorkController();
		System.out.println("Attempting to load stuff");
		controller.loadRooms();
		controller.loadItems();
		controller.loadActors();
	}

}
