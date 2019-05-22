
package controllers.anonymous;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationService;
import services.GameService;
import controllers.AbstractController;
import domain.Game;

@Controller
@RequestMapping("/game")
public class GameController extends AbstractController {

	@Autowired
	private GameService				gameService;

	@Autowired
	private ConfigurationService	configurationService;


	//List para todo el mundo ------------------------------------------------------------------------------
	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ModelAndView list() {

		final ModelAndView result;
		final Collection<Game> games;

		games = this.gameService.findAllGamesOrdered();

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("game/list");
		result.addObject("games", games);
		result.addObject("requestURI", "game/listAll.do");
		result.addObject("pagesize", 5);
		result.addObject("banner", banner);
		result.addObject("language", LocaleContextHolder.getLocale().getLanguage());
		result.addObject("autoridad", "");

		return result;

	}
}
