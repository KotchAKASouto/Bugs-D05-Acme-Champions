
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AdministratorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Actor;
import domain.Administrator;
import domain.Message;
import forms.RegisterAdministratorForm;

@Service
@Transactional
public class AdministratorService {

	// Managed Repository ------------------------
	@Autowired
	private AdministratorRepository	administratorRepository;

	// Suporting services ------------------------

	@Autowired
	private ActorService			actorService;

	@Autowired
	private UserAccountService		userAccountService;

	@Autowired
	private Validator				validator;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private MessageService			messageService;


	// Simple CRUD methods -----------------------

	public Administrator create() {

		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		Assert.isTrue((actor.getUserAccount().getAuthorities().contains(authority)));

		Administrator result;
		result = new Administrator();

		final UserAccount userAccount = this.userAccountService.createAdmin();
		result.setUserAccount(userAccount);

		return result;

	}

	public Collection<Administrator> findAll() {

		Collection<Administrator> result;
		result = this.administratorRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Administrator findOne(final int administratorId) {

		Assert.notNull(administratorId);
		Administrator result;
		result = this.administratorRepository.findOne(administratorId);
		return result;
	}

	public Administrator save(final Administrator administrator) {

		Assert.notNull(administrator);
		Administrator result;

		if (administrator.getId() != 0) {
			final Authority admin = new Authority();
			admin.setAuthority(Authority.ADMIN);

			final Actor actor = this.actorService.findByPrincipal();
			Assert.notNull(actor);

			Assert.isTrue(actor.getId() == administrator.getId());

			this.actorService.checkEmail(administrator.getEmail(), true);
			this.actorService.checkPhone(administrator.getPhone());

			result = this.administratorRepository.save(administrator);

		} else {

			this.actorService.checkEmail(administrator.getEmail(), true);
			this.actorService.checkPhone(administrator.getPhone());

			String pass = administrator.getUserAccount().getPassword();

			final Md5PasswordEncoder code = new Md5PasswordEncoder();

			pass = code.encodePassword(pass, null);

			final UserAccount userAccount = administrator.getUserAccount();
			userAccount.setPassword(pass);

			administrator.setUserAccount(userAccount);

			final String phone = this.actorService.checkPhone(administrator.getPhone());

			result = this.administratorRepository.save(administrator);

		}
		return result;
	}

	public Administrator reconstruct(final RegisterAdministratorForm form, final BindingResult binding) {

		final Administrator admin = this.create();

		this.validator.validate(form, binding);

		admin.setName(form.getName());
		admin.setSurnames(form.getSurnames());
		admin.setPhoto(form.getPhoto());
		admin.setEmail(form.getEmail());
		admin.setPhone(form.getPhone());
		admin.setAddress(form.getAddress());
		admin.setSpammer(null);
		admin.getUserAccount().setUsername(form.getUsername());
		admin.getUserAccount().setPassword(form.getPassword());

		return admin;

	}

	public Administrator reconstruct(final Administrator admin, final BindingResult binding) {

		final Administrator result;

		final Administrator adminBBDD = this.findOne(admin.getId());

		if (adminBBDD != null) {

			admin.setUserAccount(adminBBDD.getUserAccount());
			admin.setSpammer(adminBBDD.getSpammer());

			this.validator.validate(admin, binding);

		}
		result = admin;

		return result;

	}

	public void flush() {
		this.administratorRepository.flush();
	}

	// Other business methods -----------------------

	public Administrator findByPrincipal() {
		Administrator admin;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		admin = this.findByUserAccount(userAccount);
		Assert.notNull(admin);

		return admin;
	}

	public Administrator findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);

		Administrator result;

		result = this.administratorRepository.findByUserAccountId(userAccount.getId());

		return result;
	}

	public void spammer() {

		final Actor admin = this.actorService.findByPrincipal();

		final Authority authAdmin = new Authority();
		authAdmin.setAuthority(Authority.ADMIN);

		Assert.isTrue(admin.getUserAccount().getAuthorities().contains(authAdmin));

		final Collection<Actor> actors = this.actorService.findAll();

		for (final Actor actor : actors) {

			Collection<Message> messages = new HashSet<>();

			Boolean body = false;
			Boolean subject = false;
			Boolean tags = false;

			Integer intMesage = 0;
			Integer intSpam = 0;

			messages = this.messageService.messagePerActor(actor.getId());

			if (!messages.isEmpty())
				for (final Message message : messages) {

					if (message.getBody() != null)
						body = this.configurationService.spamContent(message.getBody());
					if (message.getSubject() != null)
						subject = this.configurationService.spamContent(message.getSubject());
					if (message.getTags() != null)
						tags = this.configurationService.spamContent(message.getTags());

					intMesage++;
					if (body || subject || tags)
						intSpam++;

				}

			if (!messages.isEmpty()) {
				if (intSpam >= 0.1 * intMesage)
					actor.setSpammer(true);
				else
					actor.setSpammer(false);

				this.actorService.save(actor);
			}

		}
	}

}
