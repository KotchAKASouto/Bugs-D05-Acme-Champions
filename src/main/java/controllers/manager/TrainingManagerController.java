
package controllers.manager;

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

import security.Authority;
import services.ActorService;
import services.ConfigurationService;
import services.ManagerService;
import services.TrainingService;
import controllers.AbstractController;
import domain.Actor;
import domain.Manager;
import domain.Training;

@Controller
@RequestMapping("/training/manager")
public class TrainingManagerController extends AbstractController {

	@Autowired
	private TrainingService			trainingService;

	@Autowired
	private ManagerService			managerService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ConfigurationService	configurationService;


	//List---------------------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		final ModelAndView result;
		final Collection<Training> trainings;
		final Manager manag;

		manag = this.managerService.findByPrincipal();

		trainings = this.trainingService.findTrainingsByManagerId(manag.getId());

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("training/list");
		result.addObject("trainings", trainings);
		result.addObject("requestURI", "training/manager/list.do");
		result.addObject("pagesize", 5);
		result.addObject("banner", banner);
		result.addObject("language", LocaleContextHolder.getLocale().getLanguage());
		result.addObject("autoridad", "manager");

		return result;

	}
	//Create-----------------------------------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		final String banner = this.configurationService.findConfiguration().getBanner();

		final Actor actor = this.actorService.findByPrincipal();
		final Authority authority = new Authority();
		authority.setAuthority("MANAGER");
		if (actor.getUserAccount().getAuthorities().contains(authority)) {
			final Training training = this.trainingService.create();
			result = this.createEditModelAndView(training, null);
		} else {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("banner", banner);
		}
		return result;

	}

	//Edit-------------------------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int trainingId) {
		ModelAndView result;
		Boolean security;

		final Training trainingFind = this.trainingService.findOne(trainingId);
		final String banner = this.configurationService.findConfiguration().getBanner();

		if (trainingFind == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {
			security = this.trainingService.trainingManagerSecurity(trainingId);

			if (security)
				result = this.createEditModelAndView(trainingFind, null);
			else
				result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute(value = "training") Training training, final BindingResult binding) {
		ModelAndView result;
		final String banner = this.configurationService.findConfiguration().getBanner();

		if (training.getId() != 0 && this.trainingService.findOne(training.getId()) == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else if (training.getId() != 0 && this.trainingService.findOne(training.getId()).getManager() != this.managerService.findByPrincipal())
			result = new ModelAndView("redirect:/welcome/index.do");
		else {

			training = this.trainingService.reconstruct(training, binding);

			if (binding.hasErrors())
				result = this.createEditModelAndView(training, null);
			else
				try {
					this.trainingService.save(training);
					result = new ModelAndView("redirect:list.do");
				} catch (final Throwable oops) {
					if (oops.getMessage() == "Invalid Dates")
						result = this.createEditModelAndView(training, "url.error");
					else
						result = this.createEditModelAndView(training, "training.commit.error");
				}
		}
		return result;
	}
	//Delete--------------------------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Training training, final BindingResult binding) {
		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();
		if (this.trainingService.findOne(training.getId()) == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {
			training = this.trainingService.findOne(training.getId());
			final Manager manager = this.managerService.findByPrincipal();

			if (training.getManager().getId() == manager.getId())
				try {
					this.trainingService.delete(training);
					result = new ModelAndView("redirect:list.do");
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(training, "training.commit.error");
				}
			else
				result = new ModelAndView("redirect:/welcome/index.do");

		}
		return result;
	}
	//Display------------------------------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int trainingId) {
		ModelAndView result;
		Boolean security;

		final Training trainingFind = this.trainingService.findOne(trainingId);
		final String banner = this.configurationService.findConfiguration().getBanner();

		if (trainingFind == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {
			security = this.trainingService.trainingManagerSecurity(trainingId);

			if (security) {
				result = new ModelAndView("training/display");
				result.addObject("training", trainingFind);
				result.addObject("banner", banner);
			} else
				result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;
	}

	//Ancillary methods---------------------------------------------------------------------------------
	protected ModelAndView createEditModelAndView(final Training training, final String messageCode) {
		final ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("training/edit");
		result.addObject("training", training);
		result.addObject("messageError", messageCode);
		result.addObject("banner", banner);
		result.addObject("language", LocaleContextHolder.getLocale().getLanguage());
		return result;
	}
}
