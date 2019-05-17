
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import services.ConfigurationService;

@Controller
@RequestMapping("/register")
public class RegisterController extends AbstractController {

	// Services

	@Autowired
	private ConfigurationService	configurationService;

}
