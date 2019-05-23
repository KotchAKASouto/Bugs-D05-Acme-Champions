
package controllers.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationService;
import services.PlayerService;
import services.StatisticalDataService;
import controllers.AbstractController;
import domain.Player;
import domain.StatisticalData;

@Controller
@RequestMapping("/player")
public class PlayerController extends AbstractController {

	@Autowired
	private PlayerService			playerService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private StatisticalDataService	statisticalDataService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int playerId) {
		ModelAndView result;

		final Player player = this.playerService.findOne(playerId);
		final String banner = this.configurationService.findConfiguration().getBanner();
		final String language = LocaleContextHolder.getLocale().getLanguage();

		if (player == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {
			final StatisticalData statisticalData = this.statisticalDataService.findStatisticalDataByPlayerId(playerId);
			result = new ModelAndView("player/display");
			result.addObject("player", player);
			result.addObject("statisticalData", statisticalData);
			result.addObject("banner", banner);
			result.addObject("language", language);

			//			try {
			//				final Sponsorship s = this.sponsorshipService.ramdomSponsorship(positionId);
			//
			//				if (s != null) {
			//					result.addObject("find", true);
			//					result.addObject("bannerSponsorship", s.getBanner());
			//					result.addObject("targetSponsorship", s.getTarget());
			//				}
			//
			//				else
			//					result.addObject("find", false);
			//			} catch (final Throwable oops) {
			//				result.addObject("find", false);
			//			}

		}
		return result;
	}
}