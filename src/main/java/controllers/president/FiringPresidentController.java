package controllers.president;

import java.util.Collection;
import java.util.Date;

import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Actor;
import domain.Finder;
import domain.Game;
import domain.Player;
import domain.President;
import domain.Signing;
import domain.Team;
import services.ActorService;
import services.ConfigurationService;
import services.FinderService;
import services.GameService;
import services.PlayerService;
import services.PresidentService;
import services.SigningService;
import services.TeamService;

@Controller
@RequestMapping("/president")
public class FiringPresidentController {

	// Services ---------------------------------------------------

		@Autowired
		PresidentService				presidentService;
		
		@Autowired
		PlayerService				    playerService;

		@Autowired
		private FinderService			finderService;

		@Autowired
		private ConfigurationService	configurationService;
		
		@Autowired
		private TeamService				teamService;
		
		@Autowired
		private GameService			    gameService;
		
		@Autowired
		private SigningService			signingService;


		@RequestMapping(value = "/firePlayer", method = RequestMethod.GET)
		public ModelAndView showFinder(@RequestParam final int playerId) {
			ModelAndView result = null;
			
			final President president = this.presidentService.findByPrincipal();
			
			final Team team = this.teamService.getTeamByPresidentId(president.getId());
			
			final Collection<Game> games = this.gameService.findGamesOfTeam(team.getId());
			
			final Collection<Player> players = this.playerService.findPlayersOfTeam(team.getId());
			
			final Player player = this.playerService.findOne(playerId);
			
			Signing signing;
			if (player != null ) {
				if (games.isEmpty()) {
					if(players.contains(player)) {
						signing = this.signingService.findSigningOfPresidentAndPlayer(president.getId(),player.getId());
						
						this.signingService.delete(signing);
						result = new ModelAndView("redirect:/welcome/index.do");
						
					} else {
						// NO AUTORIZADO!
					}
				} else {
					// TIENE PARTIDOS!
				}
			} else {
				// NO EXISTE EL JUGADOR!
				result = new ModelAndView("misc/notExist");
			}
			
			// Configuracion
			final String banner = this.configurationService.findConfiguration().getBanner();
			final String language = LocaleContextHolder.getLocale().getLanguage();
			result.addObject("requestURI", "finder/president/find.do");
			result.addObject("requestAction", "finder/president/find.do");
			result.addObject("banner", banner);
			result.addObject("AmILogged", true);
			result.addObject("AmInFinder", false);
			result.addObject("language", language);

			return result;

		}
	
}
