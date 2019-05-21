
package services;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Actor;

@Service
@Transactional
public class ActorService {

	//Managed Repository ---------------------------------------------------
	@Autowired
	private ActorRepository			actorRepository;

	//Supporting services --------------------------------------------------

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private UserAccountService		userAccountService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private SponsorshipService		sponsorshipService;

	@Autowired
	private FinderService			finderService;


	//Simple CRUD methods --------------------------------------------------

	public Collection<Actor> findAll() {

		final Collection<Actor> actors = this.actorRepository.findAll();

		Assert.notNull(actors);

		return actors;
	}

	public Actor findOne(final int ActorId) {

		final Actor actor = this.actorRepository.findOne(ActorId);

		Assert.notNull(actor);

		return actor;
	}

	public Actor save(final Actor a) {

		final Actor actor = this.actorRepository.save(a);

		Assert.notNull(actor);

		return actor;
	}

	public void delete(final Actor actor) {
		Assert.notNull(actor);
		Assert.isTrue(actor.getId() != 0);
		Assert.isTrue(this.actorRepository.exists(actor.getId()));
		this.actorRepository.delete(actor);
	}

	//Other business methods----------------------------

	public Actor findByPrincipal() {
		Actor a;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		a = this.findByUserAccount(userAccount);
		Assert.notNull(a);

		return a;
	}

	public Actor findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);
		Actor result;
		result = this.actorRepository.findByUserAccountId(userAccount.getId());
		return result;
	}

	public UserAccount findUserAccount(final Actor actor) {
		Assert.notNull(actor);
		UserAccount result;
		result = this.userAccountService.findByActor(actor);
		return result;
	}

	public Actor editPersonalData(final Actor actor) {
		Assert.notNull(actor);
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.isTrue(actor.getUserAccount().equals(userAccount));
		final Actor result = this.save(actor);

		return result;
	}

	public void checkEmail(final String email, final Boolean isAdmin) {

		if (!isAdmin) {
			final boolean checkEmailOthers = email.matches("^[\\w]+@(?:[a-zA-Z0-9]+\\.)+[a-zA-Z0-9]+|(([\\w]\\s)*[\\w])+<\\w+@(?:[a-zA-Z0-9]+\\.)+[a-zA-Z0-9]+>$");
			Assert.isTrue(checkEmailOthers);
		} else {
			final boolean checkEmailAdmin = email.matches("^[\\w]+@((?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]+){0,1}|(([\\w]\\s)*[\\w])+<\\w+@((?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]+){0,1}>$");
			Assert.isTrue(checkEmailAdmin);
		}
	}

	public String checkPhone(final String phone) {
		String res = phone;

		//Esto es para contar el número de dígitos que contiene 
		int count = 0;
		for (int i = 0, len = phone.length(); i < len; i++)
			if (Character.isDigit(phone.charAt(i)))
				count++;

		if (StringUtils.isNumericSpace(phone) && count == 4) {
			res.replaceAll("\\s+", ""); //quitar espacios
			res = this.configurationService.findConfiguration().getCountryCode() + " " + phone;
			Assert.isTrue(res.contains(this.configurationService.findConfiguration().getCountryCode() + " " + phone));

		}
		return res;

	}
	//	public String authorityAuthenticated() {
	//		String res = null;
	//
	//		final Authority authority1 = new Authority();
	//		authority1.setAuthority(Authority.COMPANY);
	//		final Authority authority2 = new Authority();
	//		authority2.setAuthority(Authority.ROOKIE);
	//		final Authority authority3 = new Authority();
	//		authority3.setAuthority(Authority.ADMIN);
	//
	//		if (LoginService.getPrincipal().getAuthorities().contains(authority1))
	//			res = "rookie";
	//		else if (LoginService.getPrincipal().getAuthorities().contains(authority2))
	//			res = "company";
	//		else if (LoginService.getPrincipal().getAuthorities().contains(authority3))
	//			res = "admin";
	//		return res;
	//
	//	}

	//	public void convertToSpammerActor() {
	//		final Actor actor = this.findByPrincipal();
	//		final Collection<Authority> authorities = actor.getUserAccount().getAuthorities();
	//
	//		final Authority authAdmin = new Authority();
	//		authAdmin.setAuthority(Authority.ADMIN);
	//
	//		final Authority authCompany = new Authority();
	//		authCompany.setAuthority(Authority.COMPANY);
	//
	//		final Authority authRookie = new Authority();
	//		authRookie.setAuthority(Authority.ROOKIE);
	//
	//		if (authorities.contains(authAdmin)) {
	//			final Administrator administrator = this.administratorService.findByPrincipal();
	//			administrator.setSpammer(true);
	//			this.administratorService.save(administrator);
	//
	//		} else if (authorities.contains(authRookie)) {
	//			final Rookie rookie = this.rookieService.findByPrincipal();
	//			rookie.setSpammer(true);
	//			this.rookieService.save(rookie);
	//
	//		} else if (authorities.contains(authCompany)) {
	//			final Company company = this.companyService.findByPrincipal();
	//			company.setSpammer(true);
	//			this.companyService.save(company);
	//
	//		}
	//
	//	}

	//	public void banOrUnBanActor(final Actor actor) {
	//		final Collection<Authority> authorities = actor.getUserAccount().getAuthorities();
	//
	//		final Actor principal = this.findByPrincipal();
	//
	//		Assert.isTrue(!actor.equals(principal));
	//
	//		final Authority authAdmin = new Authority();
	//		authAdmin.setAuthority(Authority.ADMIN);
	//
	//		final Authority authRookie = new Authority();
	//		authRookie.setAuthority(Authority.ROOKIE);
	//
	//		final Authority authCompany = new Authority();
	//		authCompany.setAuthority(Authority.COMPANY);
	//
	//		final Authority authProvider = new Authority();
	//		authProvider.setAuthority(Authority.PROVIDER);
	//
	//		final Authority authAuditor = new Authority();
	//		authProvider.setAuthority(Authority.AUDITOR);
	//
	//		if (authorities.contains(authAdmin)) {
	//			final Administrator administrator = this.administratorService.findOne(actor.getId());
	//			final UserAccount userAccount = administrator.getUserAccount();
	//			userAccount.setIsNotBanned(!userAccount.getIsNotBanned());
	//			this.userAccountService.save(userAccount);
	//
	//		} else if (authorities.contains(authRookie)) {
	//			final Rookie rookie = this.rookieService.findOne(actor.getId());
	//			final UserAccount userAccount = rookie.getUserAccount();
	//			userAccount.setIsNotBanned(!userAccount.getIsNotBanned());
	//			this.userAccountService.save(userAccount);
	//
	//		} else if (authorities.contains(authCompany)) {
	//			final Company company = this.companyService.findOne(actor.getId());
	//			final UserAccount userAccount = company.getUserAccount();
	//			userAccount.setIsNotBanned(!userAccount.getIsNotBanned());
	//			this.userAccountService.save(userAccount);
	//
	//		} else if (authorities.contains(authProvider)) {
	//			final Provider provider = this.providerService.findOne(actor.getId());
	//			final UserAccount userAccount = provider.getUserAccount();
	//			userAccount.setIsNotBanned(!userAccount.getIsNotBanned());
	//			this.userAccountService.save(userAccount);
	//		} else if (authorities.contains(authAuditor)) {
	//			final Auditor auditor = this.auditorService.findOne(actor.getId());
	//			final UserAccount userAccount = auditor.getUserAccount();
	//			userAccount.setIsNotBanned(!userAccount.getIsNotBanned());
	//			this.userAccountService.save(userAccount);
	//		}
	//
	//	}
	public Boolean existActor(final int actorId) {
		Boolean res = false;

		final Actor actor = this.actorRepository.findOne(actorId);

		if (actor != null)
			res = true;

		return res;

	}

	public void flush() {
		this.actorRepository.flush();
	}

}
