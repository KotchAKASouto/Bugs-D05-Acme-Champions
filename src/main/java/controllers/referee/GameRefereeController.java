
package controllers.referee;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ConfigurationService;
import services.GameService;
import services.RefereeService;
import controllers.AbstractController;
import domain.Actor;
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

	@Autowired
	private ActorService			actorService;


	@RequestMapping(value = "/listMyGames", method = RequestMethod.GET)
	public ModelAndView listMyGames() {
		final ModelAndView result;
		final String banner = this.configurationService.findConfiguration().getBanner();
		final Actor actor = this.actorService.findByPrincipal();

		final Collection<Game> myGames = this.gameService.findGameByRefereeId(actor.getId());
		result = new ModelAndView("game/list");
		result.addObject("games", myGames);
		result.addObject("requestURI", "game/referee/listMyGames.do");
		result.addObject("banner", banner);
		result.addObject("language", LocaleContextHolder.getLocale().getLanguage());

		return result;
	}
}
