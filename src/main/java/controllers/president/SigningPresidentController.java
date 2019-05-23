
package controllers.president;

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
import services.PlayerService;
import services.PresidentService;
import services.SigningService;
import services.TeamService;
import controllers.AbstractController;
import domain.Actor;
import domain.Player;
import domain.President;
import domain.Signing;
import domain.Team;
import forms.SigningForm;

@Controller
@RequestMapping("/signing/president")
public class SigningPresidentController extends AbstractController {

	// Services ---------------------------------------------------

	@Autowired
	private SigningService			signingService;

	@Autowired
	private PresidentService		presidentService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private PlayerService			playerService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private TeamService				teamService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		final ModelAndView result;
		final Collection<Signing> signings;
		final President president;

		president = this.presidentService.findByPrincipal();

		signings = this.signingService.findByPresident(president.getId());

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("signing/list");
		result.addObject("signings", signings);
		result.addObject("requestURI", "signing/president/list.do");
		result.addObject("pagesize", 5);
		result.addObject("banner", banner);
		result.addObject("language", LocaleContextHolder.getLocale().getLanguage());
		result.addObject("autoridad", "president");

		return result;

	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int playerId) {
		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		final Boolean exist = this.playerService.exist(playerId);

		if (exist) {

			final Player player = this.playerService.findOne(playerId);

			final SigningForm signingForm = this.signingService.create(playerId);
			result = this.createEditModelAndView(signingForm);
			result.addObject("enlace", "signing/president/edit.do");
			result.addObject("buyoutClause", player.getBuyoutClause());

		} else {

			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute(value = "signing") final SigningForm signingForm, final BindingResult binding) {
		ModelAndView result;

		final Boolean exist = this.playerService.exist(signingForm.getPlayerId());

		if (exist) {

			final Signing signing = this.signingService.reconstruct(signingForm, binding);

			if (binding.hasErrors())
				result = this.createEditModelAndView(signingForm, null);
			else
				try {

					this.signingService.save(signing);
					result = new ModelAndView("redirect:/signing/president/list.do");

				} catch (final Throwable oops) {

					result = this.createEditModelAndView(signingForm, "signing.commit.error");

				}

		} else
			result = new ModelAndView("redirect:/welcome/index.do");

		return result;
	}

	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public ModelAndView accept(@RequestParam final int signingId) {
		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		final Boolean exist = this.signingService.exist(signingId);

		if (exist) {

			final Signing signing = this.signingService.findOne(signingId);

			final Actor loged = this.actorService.findByPrincipal();

			final Team team = this.teamService.findByPresidentId(loged.getId());

			if (signing.getPlayer().getTeam() != null && signing.getPlayer().getTeam().equals(team) && signing.getStatus().equals("PENDING")) {

				final Player player = this.playerService.findOne(signing.getPlayer().getId());

				this.playerService.editTeam(player, this.teamService.findByPresidentId(signing.getPresident().getId()), signing.getOfferedClause());

				final Collection<Signing> oldOnes = this.signingService.findAllByPlayer(player.getId());

				for (final Signing oldOne : oldOnes)
					this.signingService.delete(oldOne);

				signing.setStatus("ACCEPTED");

				this.signingService.save(signing);

				result = new ModelAndView("redirect:/signing/president/list.do");
				result.addObject("banner", banner);

			} else {

				result = new ModelAndView("redirect:/welcome/index.do");
				result.addObject("banner", banner);
			}

		} else {

			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		}

		return result;
	}

	@RequestMapping(value = "/reject", method = RequestMethod.GET)
	public ModelAndView reject(@RequestParam final int signingId) {
		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		final Boolean exist = this.signingService.exist(signingId);

		if (exist) {

			final Signing signing = this.signingService.findOne(signingId);

			final Actor loged = this.actorService.findByPrincipal();

			final Team team = this.teamService.findByPresidentId(loged.getId());

			if (signing.getPlayer().getTeam() != null && signing.getPlayer().getTeam().equals(team) && signing.getStatus().equals("PENDING")) {

				final SigningForm signingForm = this.signingService.editForm(signing);

				result = this.createEditModelAndView(signingForm);
				result.addObject("enlace", "signing/president/reject.do");

			} else {

				result = new ModelAndView("redirect:/welcome/index.do");
				result.addObject("banner", banner);
			}

		} else {

			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		}

		return result;
	}
	@RequestMapping(value = "/reject", method = RequestMethod.POST, params = "save")
	public ModelAndView reject(@ModelAttribute(value = "signing") final SigningForm signingForm, final BindingResult binding) {
		ModelAndView result;

		final Boolean exist = this.signingService.exist(signingForm.getId());

		if (exist) {

			final Signing signing = this.signingService.reconstruct(signingForm, binding);

			final Actor loged = this.actorService.findByPrincipal();

			final Team team = this.teamService.findByPresidentId(loged.getId());

			if (signing.getPlayer().getTeam() != null && signing.getPlayer().getTeam().equals(team) && signing.getStatus().equals("PENDING")) {

				if (binding.hasErrors())
					result = this.createEditModelAndView(signingForm, null);
				else
					try {
						signing.setStatus("REJECTED");
						this.signingService.save(signing);
						result = new ModelAndView("redirect:/signing/president/list.do");

					} catch (final Throwable oops) {

						result = this.createEditModelAndView(signingForm, "signing.commit.error");

					}

			} else
				result = new ModelAndView("redirect:/welcome/index.do");

		} else
			result = new ModelAndView("redirect:/welcome/index.do");

		return result;
	}

	protected ModelAndView createEditModelAndView(final SigningForm signing) {
		final ModelAndView result;
		result = this.createEditModelAndView(signing, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final SigningForm signing, final String errorText) {
		final ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("signing/edit");
		result.addObject("messageError", errorText);
		result.addObject("signing", signing);
		result.addObject("banner", banner);
		result.addObject("autoridad", "president");

		return result;
	}

}
