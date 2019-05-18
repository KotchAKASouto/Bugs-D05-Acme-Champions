
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.Credentials;
import services.ConfigurationService;
import services.ManagerService;
import services.PlayerService;
import domain.Manager;
import domain.Player;
import forms.RegisterManagerForm;
import forms.RegisterPlayerForm;

@Controller
@RequestMapping("/register")
public class RegisterController extends AbstractController {

	// Services

	@Autowired
	private ManagerService			managerService;

	@Autowired
	private PlayerService			playerService;

	@Autowired
	private ConfigurationService	configurationService;


	//Manager
	@RequestMapping(value = "/createManager", method = RequestMethod.GET)
	public ModelAndView createManager() {
		final ModelAndView result;
		final RegisterManagerForm manager = new RegisterManagerForm();

		result = this.createEditModelAndViewManager(manager);

		return result;
	}

	@RequestMapping(value = "/saveManager", method = RequestMethod.POST, params = "save")
	public ModelAndView saveManager(@ModelAttribute("manager") final RegisterManagerForm form, final BindingResult binding) {
		ModelAndView result;
		final Manager manager;

		manager = this.managerService.reconstruct(form, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndViewManager(form);
		else
			try {
				Assert.isTrue(form.getCheckbox());
				Assert.isTrue(form.checkPassword());
				this.managerService.save(manager);
				final Credentials credentials = new Credentials();
				credentials.setJ_username(manager.getUserAccount().getUsername());
				credentials.setPassword(manager.getUserAccount().getPassword());
				result = new ModelAndView("redirect:/security/login.do");
				result.addObject("credentials", credentials);
			} catch (final Throwable oops) {
				result = this.createEditModelAndViewManager(form, "manager.commit.error");
			}
		return result;
	}
	protected ModelAndView createEditModelAndViewManager(final RegisterManagerForm manager) {
		ModelAndView result;

		result = this.createEditModelAndViewManager(manager, null);

		return result;
	}

	protected ModelAndView createEditModelAndViewManager(final RegisterManagerForm manager, final String messageCode) {
		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("security/signUpCompany");
		result.addObject("manager", manager);
		result.addObject("banner", banner);
		result.addObject("messageError", messageCode);
		final String countryCode = this.configurationService.findConfiguration().getCountryCode();
		result.addObject("defaultCountry", countryCode);

		return result;
	}

	//Player
	@RequestMapping(value = "/createPlayer", method = RequestMethod.GET)
	public ModelAndView createPlayer() {
		final ModelAndView result;
		final RegisterPlayerForm player = new RegisterPlayerForm();

		result = this.createEditModelAndViewPlayer(player);

		return result;
	}

	@RequestMapping(value = "/savePlayer", method = RequestMethod.POST, params = "save")
	public ModelAndView savePlayer(@ModelAttribute("manager") final RegisterPlayerForm form, final BindingResult binding) {
		ModelAndView result;
		final Player player;

		player = this.playerService.reconstruct(form, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndViewPlayer(form);
		else
			try {
				Assert.isTrue(form.getCheckbox());
				Assert.isTrue(form.checkPassword());
				this.playerService.save(player);
				final Credentials credentials = new Credentials();
				credentials.setJ_username(player.getUserAccount().getUsername());
				credentials.setPassword(player.getUserAccount().getPassword());
				result = new ModelAndView("redirect:/security/login.do");
				result.addObject("credentials", credentials);
			} catch (final Throwable oops) {
				result = this.createEditModelAndViewPlayer(form, "player.commit.error");
			}
		return result;
	}
	protected ModelAndView createEditModelAndViewPlayer(final RegisterPlayerForm player) {
		ModelAndView result;

		result = this.createEditModelAndViewPlayer(player, null);

		return result;
	}

	protected ModelAndView createEditModelAndViewPlayer(final RegisterPlayerForm player, final String messageCode) {
		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("security/signUpCompany");
		result.addObject("player", player);
		result.addObject("banner", banner);
		result.addObject("messageError", messageCode);
		final String countryCode = this.configurationService.findConfiguration().getCountryCode();
		result.addObject("defaultCountry", countryCode);

		return result;
	}

}
