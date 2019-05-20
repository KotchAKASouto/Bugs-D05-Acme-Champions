
package services;

import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.PlayerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Actor;
import domain.Player;
import forms.RegisterPlayerForm;

@Service
@Transactional
public class PlayerService {

	// Managed Repository ------------------------
	@Autowired
	private PlayerRepository	playerRepository;

	// Suporting services ------------------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private Validator			validator;


	// Methods -----------------------------------

	public Player create() {

		Player result;
		result = new Player();

		final UserAccount userAccount = this.userAccountService.createPlayer();
		result.setUserAccount(userAccount);

		return result;

	}

	public Collection<Player> findAll() {

		Collection<Player> result;
		result = this.playerRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Player findOne(final int companyId) {

		Assert.notNull(companyId);
		Player result;
		result = this.playerRepository.findOne(companyId);
		return result;
	}

	public Player save(final Player player) {
		Assert.notNull(player);
		Player result;

		if (player.getId() != 0) {
			final Authority admin = new Authority();
			admin.setAuthority(Authority.ADMIN);

			final Actor actor = this.actorService.findByPrincipal();
			Assert.notNull(actor);

			Assert.isTrue(actor.getId() == player.getId() || actor.getUserAccount().getAuthorities().contains(admin));

			this.actorService.checkEmail(player.getEmail(), false);
			this.actorService.checkPhone(player.getPhone());

			final String phone = this.actorService.checkPhone(player.getPhone());
			player.setPhone(phone);

			result = this.playerRepository.save(player);

		} else {

			String pass = player.getUserAccount().getPassword();

			final Md5PasswordEncoder code = new Md5PasswordEncoder();

			pass = code.encodePassword(pass, null);

			final UserAccount userAccount = player.getUserAccount();
			userAccount.setPassword(pass);

			player.setUserAccount(userAccount);

			this.actorService.checkEmail(player.getEmail(), false);
			this.actorService.checkPhone(player.getPhone());

			final String phone = this.actorService.checkPhone(player.getPhone());
			player.setPhone(phone);

			result = this.playerRepository.save(player);

		}
		return result;

	}

	public Player findByPrincipal() {
		Player player;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		player = this.findByUserAccount(userAccount);
		Assert.notNull(player);

		return player;
	}

	public Player findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);

		Player result;

		result = this.playerRepository.findByUserAccountId(userAccount.getId());

		return result;
	}

	public Player reconstruct(final RegisterPlayerForm form, final BindingResult binding) {

		final Player player = this.create();

		final Locale locale = LocaleContextHolder.getLocale();
		final String language = locale.getLanguage();

		if (language == "es") {
			player.setPositionSpanish(form.getPosition());

			if (form.getPosition() == "PORTERO")
				player.setPositionEnglish("GOALKEEPER");
			else if (form.getPosition() == "DEFENSA")
				player.setPositionEnglish("DEFENDER");
			else if (form.getPosition() == "CENTROCAMPISTA")
				player.setPositionEnglish("MIDFIELDER");
			else if (form.getPosition() == "DELANTERO")
				player.setPositionEnglish("STRIKER");
			else
				form.setPosition("error");

		} else if (language == "en") {

			player.setPositionSpanish(form.getPosition());

			if (form.getPosition() == "GOALKEEPER")
				player.setPositionSpanish("PORTERO");
			else if (form.getPosition() == "DEFENDER")
				player.setPositionSpanish("DEFENSA");
			else if (form.getPosition() == "MIDFIELDER")
				player.setPositionSpanish("CENTROCAMPISTA");
			else if (form.getPosition() == "STRIKER")
				player.setPositionSpanish("DELANTERO");
			else
				form.setPosition("error");
		}

		this.validator.validate(form, binding);

		player.setName(form.getName());
		player.setSurnames(form.getSurnames());
		player.setPhoto(form.getPhoto());
		player.setEmail(form.getEmail());
		player.setPhone(form.getPhone());
		player.setAddress(form.getAddress());
		player.getUserAccount().setUsername(form.getUsername());
		player.getUserAccount().setPassword(form.getPassword());

		player.setBuyoutClause(0.0);
		player.setInjured(false);
		player.setPunished(false);
		player.setSquadName(form.getSquadName());
		player.setSquadNumber(form.getSquadNumber());

		return player;

	}

	public Player reconstruct(final Player player, final BindingResult binding) {

		final Player result;

		final Player playerBBDD = this.findOne(player.getId());

		if (playerBBDD != null) {

			player.setUserAccount(playerBBDD.getUserAccount());

			this.validator.validate(player, binding);

		}
		result = player;
		return result;

	}
}
