
package controllers.referee;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationService;
import services.GameService;
import services.RefereeService;
import controllers.AbstractController;
import domain.Game;

@Controller
@RequestMapping("/game/referee")
public class GameRefereeController extends AbstractController {

	@Autowired
	private RefereeService			refereeService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private GameService				gameService;


	@RequestMapping(value = "/listGamesEnded", method = RequestMethod.GET)
	public ModelAndView listGamesEnded() {
		final ModelAndView result;
		final String banner = this.configurationService.findConfiguration().getBanner();

		final Collection<Game> gamesEndedWithoutMinutes = this.gameService.findAllEndedGamesWithoutMinutes();
		result = new ModelAndView("game/list");
		result.addObject("games", gamesEndedWithoutMinutes);
		result.addObject("requestURI", "game/referee/listGamesEnded.do");
		result.addObject("banner", banner);
		result.addObject("language", LocaleContextHolder.getLocale().getLanguage());

		return result;
	}

}
