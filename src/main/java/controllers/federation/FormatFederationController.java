package controllers.federation;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import domain.Federation;
import domain.Format;
import services.ConfigurationService;
import services.FederationService;
import services.FormatService;

@Controller
@RequestMapping("/format/federation")
public class FormatFederationController {
	
	// Services ---------------------------------------------------
	
	@Autowired
	private FormatService			formatService;
	
	@Autowired
	private FederationService		federationService;
	
	@Autowired
	private ConfigurationService	configurationService;
	
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		final ModelAndView result;
		final Collection<Format> formats;
		final Federation federation;

		federation = this.federationService.findByPrincipal();

		formats = this.formatService.findFormatByFederationId(federation.getId());

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("format/list");
		result.addObject("formats", formats);
		result.addObject("requestURI", "format/fedearation/list.do");
		result.addObject("pagesize", 5);
		result.addObject("banner", banner);
		result.addObject("language", LocaleContextHolder.getLocale().getLanguage());
		result.addObject("autoridad", "federation");

		return result;

	}

}
