
package controllers.referee;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ConfigurationService;
import services.GameService;
import services.MinutesService;
import services.PlayerService;
import controllers.AbstractController;
import domain.Actor;
import domain.Game;
import domain.Minutes;
import domain.Player;

@Controller
@RequestMapping("/minutes/referee")
public class MinutesRefereeController extends AbstractController {

	@Autowired
	private MinutesService			minutesService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private GameService				gameService;

	@Autowired
	private PlayerService			playerService;

	@Autowired
	private ActorService			actorService;


	//create 1 step
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int gameId) {
		ModelAndView result;

		final Actor actor = this.actorService.findByPrincipal();

		final String banner = this.configurationService.findConfiguration().getBanner();

		final Minutes m = this.minutesService.findMinuteByGameId(gameId);

		final Game game = this.gameService.findOne(gameId);

		if (m == null && game != null && game.getReferee().getId() == actor.getId()) {

			final Collection<Player> playersHome = this.playerService.findPlayersOfTeam(game.getHomeTeam().getId());
			final Collection<Player> playersVisitor = this.playerService.findPlayersOfTeam(game.getVisitorTeam().getId());

			try {
				final Minutes minutes = this.minutesService.create(game);
				final Minutes saved = this.minutesService.save(minutes);

				result = new ModelAndView("player/listAdd");
				result.addObject("players", playersHome);
				result.addObject("playersVisitor", playersVisitor);
				result.addObject("minutesId", saved.getId());
				result.addObject("requestURI", "minutes/referee/create.do?gameId=" + gameId);
				result.addObject("banner", banner);
				result.addObject("language", LocaleContextHolder.getLocale().getLanguage());
				result.addObject("AmInMinutesFirstStep", true);

			} catch (final Throwable oops) {
				result = new ModelAndView("misc/error");
			}
		} else
			result = new ModelAndView("misc/notExist");

		return result;
	}
	@RequestMapping(value = "/addPlayerScored", method = RequestMethod.GET)
	public ModelAndView addPlayerScored(@RequestParam final int playerId, @RequestParam final int minutesId) {
		final ModelAndView result;
		final String banner = this.configurationService.findConfiguration().getBanner();
		final Minutes minutes = this.minutesService.findOne(minutesId);
		final Collection<Player> playersHome = this.playerService.findPlayersOfTeam(minutes.getGame().getHomeTeam().getId());
		final Collection<Player> playersVisitor = this.playerService.findPlayersOfTeam(minutes.getGame().getVisitorTeam().getId());

		if (this.minutesService.security(playerId, minutesId)) {

			result = new ModelAndView("player/listAdd");
			result.addObject("minutes", minutes);
			result.addObject("players", playersHome);
			result.addObject("playersVisitor", playersVisitor);
			result.addObject("requestURI", "minutes/referee/addPlayerScored.do?playerId=" + playerId + "&minutesId=" + minutesId);
			result.addObject("banner", banner);
			result.addObject("language", LocaleContextHolder.getLocale().getLanguage());
			result.addObject("AmInMinutesFirstStep", true);

		} else
			result = new ModelAndView("misc/notExist");
		return result;
	}
}
