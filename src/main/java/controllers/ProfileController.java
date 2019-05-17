/*
 * ProfileController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

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
import services.ActorService;
import services.AdministratorService;
import services.ConfigurationService;
import domain.Actor;
import domain.Administrator;

@Controller
@RequestMapping("/profile")
public class ProfileController extends AbstractController {

	@Autowired
	private ActorService			actorService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private ConfigurationService	configurationService;


	@RequestMapping(value = "/displayPrincipal", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView result;
		Actor actor;

		actor = this.actorService.findOne(this.actorService.findByPrincipal().getId());
		Assert.notNull(actor);

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("actor/display");
		result.addObject("actor", actor);
		result.addObject("banner", banner);
		result.addObject("laguageURI", "profile/displayPrincipal.do");
		result.addObject("admin", false);

		return result;

	}

	//	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	//	public ModelAndView edit() {
	//		final ModelAndView result;
	//		final Actor actor;
	//
	//		final Actor principal = this.actorService.findByPrincipal();
	//		actor = this.actorService.findOne(principal.getId());
	//		Assert.isTrue(actor.equals(principal));
	//
	//		final Authority authority1 = new Authority();
	//		authority1.setAuthority(Authority.COMPANY);
	//
	//		final Authority authority2 = new Authority();
	//		authority2.setAuthority(Authority.ROOKIE);
	//
	//		final Authority authority3 = new Authority();
	//		authority3.setAuthority(Authority.ADMIN);
	//
	//		final Authority authority4 = new Authority();
	//		authority4.setAuthority(Authority.AUDITOR);
	//
	//		final Authority authority5 = new Authority();
	//		authority5.setAuthority(Authority.PROVIDER);
	//
	//		String auth = null;
	//		String action = null;
	//		if (actor.getUserAccount().getAuthorities().contains(authority1)) {
	//			auth = "company";
	//			action = "editCompany.do";
	//
	//		} else if (actor.getUserAccount().getAuthorities().contains(authority2)) {
	//			auth = "rookie";
	//			action = "editRookie.do";
	//		} else if (actor.getUserAccount().getAuthorities().contains(authority3)) {
	//			auth = "administrator";
	//			action = "editAdministrator.do";
	//		} else if (actor.getUserAccount().getAuthorities().contains(authority4)) {
	//			auth = "auditor";
	//			action = "editAuditor.do";
	//		} else if (actor.getUserAccount().getAuthorities().contains(authority5)) {
	//			auth = "provider";
	//			action = "editProvider.do";
	//		}
	//
	//		final String banner = this.configurationService.findConfiguration().getBanner();
	//		final String defaultCountry = this.configurationService.findConfiguration().getCountryCode();
	//		result = new ModelAndView("actor/edit");
	//		result.addObject("actionURI", action);
	//		result.addObject(auth, actor);
	//		result.addObject("authority", auth);
	//		result.addObject("banner", banner);
	//		result.addObject("laguageURI", "profile/edit.do");
	//		result.addObject("defaultCountry", defaultCountry);
	//
	//		return result;
	//	}

	//--------------------------ADMINISTRATOR------------------------------

	@RequestMapping(value = "/editAdministrator", method = RequestMethod.POST, params = "save")
	public ModelAndView saveAdministrator(@ModelAttribute("administrator") final Administrator admin, final BindingResult binding) {
		ModelAndView result;

		final Administrator adminReconstruct = this.administratorService.reconstruct(admin, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndViewAdmin(adminReconstruct);
		else
			try {
				this.administratorService.save(adminReconstruct);
				final Credentials credentials = new Credentials();
				credentials.setJ_username(adminReconstruct.getUserAccount().getUsername());
				credentials.setPassword(adminReconstruct.getUserAccount().getPassword());
				result = new ModelAndView("redirect:/profile/displayPrincipal.do");
				result.addObject("credentials", credentials);
			} catch (final Throwable oops) {
				result = this.createEditModelAndViewAdmin(adminReconstruct, "actor.commit.error");
			}
		return result;
	}

	protected ModelAndView createEditModelAndViewAdmin(final Administrator admin) {
		ModelAndView result;

		result = this.createEditModelAndViewAdmin(admin, null);

		return result;
	}

	protected ModelAndView createEditModelAndViewAdmin(final Administrator admin, final String messageCode) {
		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("actor/edit");

		result.addObject("administrator", admin);
		result.addObject("authority", "administrator");
		result.addObject("actionURI", "editAdministrator.do");
		result.addObject("banner", banner);
		result.addObject("laguageURI", "profile/edit.do");
		result.addObject("messageError", messageCode);
		final String countryCode = this.configurationService.findConfiguration().getCountryCode();
		result.addObject("defaultCountry", countryCode);

		return result;
	}

}
