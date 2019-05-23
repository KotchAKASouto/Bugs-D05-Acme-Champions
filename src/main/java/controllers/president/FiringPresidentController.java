
package controllers.president;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationService;
import services.FinderService;
import services.GameService;
import services.HiringService;
import services.ManagerService;
import services.PlayerService;
import services.PresidentService;
import services.SigningService;
import services.TeamService;
import controllers.AbstractController;
import domain.Game;
import domain.Hiring;
import domain.Manager;
import domain.Player;
import domain.President;
import domain.Signing;
import domain.Team;

@Controller
@RequestMapping("/president")
public class FiringPresidentController extends AbstractController {

	// Services ---------------------------------------------------

	@Autowired
	PresidentService				presidentService;

	@Autowired
	PlayerService					playerService;

	@Autowired
	ManagerService					managerService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private TeamService				teamService;

	@Autowired
	private GameService				gameService;

	@Autowired
	private SigningService			signingService;

	@Autowired
	private HiringService			hiringService;
	
	@RequestMapping(value = "/canFire", method = RequestMethod.GET)
	public boolean canFire() {
		boolean result = false;
		final President president = this.presidentService.findByPrincipal();

		final Team team = this.teamService.findTeamByPresidentId(president.getId());

		final Collection<Game> games = this.gameService.findGamesOfTeam(team.getId());

		if (games.isEmpty()) {
			result = true;
		}
		
		return result;
	}
	

	@RequestMapping(value = "/firePlayer", method = RequestMethod.GET)
	public ModelAndView firePlayer(@RequestParam final int playerId) {
		ModelAndView result = null;

		final President president = this.presidentService.findByPrincipal();

		final Team team = this.teamService.findTeamByPresidentId(president.getId());

		final Collection<Game> games = this.gameService.findGamesOfTeam(team.getId());

		final Collection<Player> players = this.playerService.findPlayersOfTeam(team.getId());

		final Player player = this.playerService.findOne(playerId);

		Signing signing;
		if (player != null) {
			if (games.isEmpty()) {
				if (players.contains(player)) {
					signing = this.signingService.findSigningOfPresidentAndPlayer(president.getId(), player.getId());

					this.signingService.delete(signing);
					player.setTeam(null);
					this.playerService.save(player);
					
					result = new ModelAndView("redirect:/actor/listPlayerManager");
					System.out.println("Borrado!");

				} else
					// NO AUTORIZADO!
					System.out.println("No autorizado!");
				result = new ModelAndView("redirect:/welcome/index.do");
			} else
				// TIENE PARTIDOS!
				System.out.println("Tiene partidos!");
				result = new ModelAndView("redirect:/welcome/index.do");
			
		} else {
			// NO EXISTE EL JUGADOR!
			System.out.println("No existe el jugador!");
			result = new ModelAndView("misc/notExist");
		}

		// Configuracion
		/*final String banner = this.configurationService.findConfiguration().getBanner();
		final String language = LocaleContextHolder.getLocale().getLanguage();
		result.addObject("requestURI", "finder/president/find.do");
		result.addObject("requestAction", "finder/president/find.do");
		result.addObject("banner", banner);
		result.addObject("AmILogged", true);
		result.addObject("AmInFinder", false);
		result.addObject("language", language);*/

		return result;

	}

	@RequestMapping(value = "/fireManager", method = RequestMethod.GET)
	public ModelAndView fireManager(@RequestParam final int managerId) {
		ModelAndView result = null;

		final President president = this.presidentService.findByPrincipal();

		final Team team = this.teamService.findTeamByPresidentId(president.getId());

		final Collection<Game> games = this.gameService.findGamesOfTeam(team.getId());

		final Manager manager = this.managerService.findOne(managerId);

		final Hiring hiring;

		if (manager != null) {
			hiring = this.hiringService.findHiringOfPresidentAndManager(president.getId(), manager.getId());
			if (games.isEmpty()) {

				if (hiring != null) {
					this.hiringService.delete(hiring);
					
					manager.setTeam(null);
					this.managerService.save(manager);
					
					result = new ModelAndView("redirect:/actor/listPlayerManager");
					System.out.println("Borrado!");

				} else
					// NO AUTORIZADO!
					System.out.println("No autorizado!");
					result = new ModelAndView("redirect:/welcome/index.do");
			} else
				// TIENE PARTIDOS!
				System.out.println("Tiene partidos!");
				result = new ModelAndView("redirect:/welcome/index.do");
		} else {
			// NO EXISTE EL MANAGER!
			System.out.println("No existe el manager!");
			result = new ModelAndView("misc/notExist");
		}

		// Configuracion
		/*final String banner = this.configurationService.findConfiguration().getBanner();
		final String language = LocaleContextHolder.getLocale().getLanguage();
		result.addObject("requestURI", "finder/president/find.do");
		result.addObject("requestAction", "finder/president/find.do");
		result.addObject("banner", banner);
		result.addObject("AmILogged", true);
		result.addObject("AmInFinder", false);
		result.addObject("language", language);*/

		return result;

	}

}
