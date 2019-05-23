
package controllers.referee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import services.RefereeService;
import controllers.AbstractController;

@Controller
@RequestMapping("/game/referee")
public class GameRefereeController extends AbstractController {

	@Autowired
	private RefereeService	refereeService;

}
