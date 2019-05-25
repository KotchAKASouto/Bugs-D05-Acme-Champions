
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

		final Minutes m = this.minutesService.findMinuteByGameId(gameId);

		final Game game = this.gameService.findOne(gameId);

		if (m == null && game != null && game.getReferee().getId() == actor.getId())
			try {
				final Minutes minutes = this.minutesService.create(game);
				final Minutes saved = this.minutesService.save(minutes);

				result = new ModelAndView("redirect:listAddInterface.do?minutesId=" + saved.getId());

			} catch (final Throwable oops) {
				result = new ModelAndView("misc/error");
			}
		else
			result = new ModelAndView("misc/notExist");

		return result;
	}

	@RequestMapping(value = "/listAddInterface", method = RequestMethod.GET)
	public ModelAndView listAddInterface(@RequestParam final int minutesId) {
		ModelAndView result;
		final String banner = this.configurationService.findConfiguration().getBanner();
		final Minutes minutes = this.minutesService.findOne(minutesId);
		final Collection<Player> playersHome = this.playerService.findPlayersOfTeam(minutes.getGame().getHomeTeam().getId());
		final Collection<Player> playersVisitor = this.playerService.findPlayersOfTeam(minutes.getGame().getVisitorTeam().getId());
		final Actor actor = this.actorService.findByPrincipal();

		if (minutes != null && actor.getId() == minutes.getGame().getReferee().getId())
			try {
				final Integer countHome = this.playerService.countHomeGoalsByMinutesId(minutes.getId());
				final Integer countYellowHome = this.playerService.countHomeYellowsByMinutesId(minutesId);
				final Integer countRedHome = this.playerService.countHomeRedsByMinutesId(minutes.getId());
				final Integer countVisitor = this.playerService.countVisitorGoalsByMinutesId(minutes.getId());
				final Integer countYellowVisitor = this.playerService.countVisitorYellowsByMinutesId(minutesId);
				final Integer counRedVisitor = this.playerService.countVisitorRedsByMinutesId(minutesId);

				result = new ModelAndView("player/listAdd");
				result.addObject("players", playersHome);
				result.addObject("minutesId", minutes.getId());
				result.addObject("playersVisitor", playersVisitor);
				result.addObject("requestURI", "minutes/referee/listAddInterface.do");
				result.addObject("banner", banner);
				result.addObject("language", LocaleContextHolder.getLocale().getLanguage());
				result.addObject("countHome", countHome);
				result.addObject("countYellowHome", countYellowHome);
				result.addObject("countRedHome", countRedHome);
				result.addObject("countVisitor", countVisitor);
				result.addObject("countYellowVisitor", countYellowVisitor);
				result.addObject("counRedVisitor", counRedVisitor);
			} catch (final Throwable oops) {
				result = new ModelAndView("misc/error");
			}
		else
			result = new ModelAndView("misc/notExist");
		return result;
	}

	@RequestMapping(value = "/addPlayerScored", method = RequestMethod.GET)
	public ModelAndView addPlayerScored(@RequestParam final int playerId, @RequestParam final int minutesId) {
		ModelAndView result;

		if (this.minutesService.security(playerId, minutesId))
			try {
				this.minutesService.addPlayerScored(playerId, minutesId);

				result = new ModelAndView("redirect:listAddInterface.do?minutesId=" + minutesId);
			} catch (final Throwable oops) {
				result = new ModelAndView("misc/error");
			}
		else
			result = new ModelAndView("misc/notExist");
		return result;
	}

	@RequestMapping(value = "/addPlayerYellowCard", method = RequestMethod.GET)
	public ModelAndView addPlayerYellowCard(@RequestParam final int playerId, @RequestParam final int minutesId) {
		ModelAndView result;

		if (this.minutesService.security(playerId, minutesId))
			try {
				this.minutesService.addPlayerYellowCard(playerId, minutesId);

				result = new ModelAndView("redirect:listAddInterface.do?minutesId=" + minutesId);
			} catch (final Throwable oops) {
				result = new ModelAndView("misc/error");
			}
		else
			result = new ModelAndView("misc/notExist");
		return result;
	}

	@RequestMapping(value = "/addPlayerRedCard", method = RequestMethod.GET)
	public ModelAndView addPlayerRedCard(@RequestParam final int playerId, @RequestParam final int minutesId) {
		ModelAndView result;

		if (this.minutesService.security(playerId, minutesId))
			try {
				this.minutesService.addPlayerRedCard(playerId, minutesId);

				result = new ModelAndView("redirect:listAddInterface.do?minutesId=" + minutesId);
			} catch (final Throwable oops) {
				result = new ModelAndView("misc/error");
			}
		else
			result = new ModelAndView("misc/notExist");
		return result;
	}
}
