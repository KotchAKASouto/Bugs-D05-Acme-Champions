
package controllers.referee;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ConfigurationService;
import services.GameService;
import services.RefereeService;
import services.TeamService;
import controllers.AbstractController;
import domain.Actor;
import domain.Game;
import domain.Team;

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

	@Autowired
	private TeamService				teamService;


	@RequestMapping(value = "/listMyGames", method = RequestMethod.GET)
	public ModelAndView listMyGames() {
		final ModelAndView result;
		final String banner = this.configurationService.findConfiguration().getBanner();
		final Actor actor = this.actorService.findByPrincipal();

		final Collection<Game> myFutureGames = this.gameService.findFutureGamesByRefereeId(actor.getId());
		final Collection<Game> myEndedGamesWithMinutes = this.gameService.findAllEndedGamesWithMinutes(actor.getId());
		final Collection<Game> myEndedGamesWithoutMinutes = this.gameService.findAllEndedGamesWithoutMinutes(actor.getId());

		result = new ModelAndView("game/list");
		result.addObject("games", myFutureGames);
		result.addObject("myEndedGamesWithMinutes", myEndedGamesWithMinutes);
		result.addObject("myEndedGamesWithoutMinutes", myEndedGamesWithoutMinutes);
		result.addObject("requestURI", "game/referee/listMyGames.do");
		result.addObject("banner", banner);
		result.addObject("language", LocaleContextHolder.getLocale().getLanguage());

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;

		final Game game = this.gameService.create();

		result = this.createEditModelAndView(game, null);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int gameId) {
		ModelAndView result;

		final Game game = this.gameService.findOne(gameId);

		if (game.getId() != 0 && this.gameService.findOne(game.getId()) == null)
			result = this.createEditModelAndView(game, null);
		else
			result = new ModelAndView("redirect:/welcome/index.do");

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute(value = "game") Game game, final BindingResult binding) {
		ModelAndView result;
		final String banner = this.configurationService.findConfiguration().getBanner();

		game = this.gameService.reconstruct(game, binding);

		if (game.getId() != 0 && this.gameService.findOne(game.getId()) == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {
			game = this.gameService.reconstruct(game, binding);

			if (binding.hasErrors())
				result = this.createEditModelAndView(game, null);
			else
				try {
					this.gameService.save(game);
					result = new ModelAndView("redirect:list.do");
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(game, "game.commit.error");

				}
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@ModelAttribute(value = "game") Game game, final BindingResult binding) {
		ModelAndView result;
		final String banner = this.configurationService.findConfiguration().getBanner();

		if (game.getId() != 0 && this.gameService.findOne(game.getId()) == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {
			game = this.gameService.reconstruct(game, binding);

			if (binding.hasErrors())
				result = this.createEditModelAndView(game, null);
			else
				try {
					this.gameService.delete(game);
					result = new ModelAndView("redirect:list.do");
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(game, "game.commit.error");

				}
		}
		return result;
	}

	protected ModelAndView createEditModelAndView(final Game game, final String messageCode) {
		final ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		final Collection<Team> teams = this.teamService.findAll();

		result = new ModelAndView("game/edit");
		result.addObject("teams", teams);
		result.addObject("game", game);
		result.addObject("banner", banner);
		result.addObject("messageError", messageCode);

		return result;
	}
}
