
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
import domain.StatisticalData;
import forms.RegisterPlayerForm;

@Service
@Transactional
public class PlayerService {

	// Managed Repository ------------------------
	@Autowired
	private PlayerRepository		playerRepository;

	// Suporting services ------------------------

	@Autowired
	private ActorService			actorService;

	@Autowired
	private UserAccountService		userAccountService;

	@Autowired
	private StatisticalDataService	statisticalDataService;

	@Autowired
	private Validator				validator;


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

			final StatisticalData data = this.statisticalDataService.create();
			data.setPlayer(player);
			this.statisticalDataService.save(data);

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

		if (language.equals("es")) {
			player.setPositionSpanish(form.getPosition());

			if (form.getPosition().equals("PORTERO"))
				player.setPositionEnglish("GOALKEEPER");
			else if (form.getPosition().equals("DEFENSA"))
				player.setPositionEnglish("DEFENDER");
			else if (form.getPosition().equals("CENTROCAMPISTA"))
				player.setPositionEnglish("MIDFIELDER");
			else if (form.getPosition().equals("DELANTERO"))
				player.setPositionEnglish("STRIKER");
			else
				form.setPosition("error");

		} else if (language.equals("en")) {

			player.setPositionEnglish(form.getPosition());

			if (form.getPosition().equals("GOALKEEPER"))
				player.setPositionSpanish("PORTERO");
			else if (form.getPosition().equals("DEFENDER"))
				player.setPositionSpanish("DEFENSA");
			else if (form.getPosition().equals("MIDFIELDER"))
				player.setPositionSpanish("CENTROCAMPISTA");
			else if (form.getPosition().equals("STRIKER"))
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

		player.setBuyoutClause(0.0);
		player.setInjured(false);
		player.setPunished(false);
		player.setSquadName(form.getSquadName());
		player.setSquadNumber(form.getSquadNumber());

		player.getUserAccount().setUsername(form.getUsername());
		player.getUserAccount().setPassword(form.getPassword());

		return player;

	}
	public Player reconstruct(final Player player, final BindingResult binding) {

		final Player result;

		final Player playerBBDD = this.findOne(player.getId());

		if (playerBBDD != null) {

			player.setUserAccount(playerBBDD.getUserAccount());
			player.setBuyoutClause(playerBBDD.getBuyoutClause());
			player.setInjured(playerBBDD.getInjured());
			player.setPunished(playerBBDD.getPunished());
			player.setPositionEnglish(playerBBDD.getPositionEnglish());
			player.setPositionSpanish(playerBBDD.getPositionSpanish());

			this.validator.validate(player, binding);

		}
		result = player;
		return result;

	}
}
