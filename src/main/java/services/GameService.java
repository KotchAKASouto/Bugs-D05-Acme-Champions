
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.GameRepository;
import security.Authority;
import domain.Actor;
import domain.Competition;
import domain.Game;
import domain.Sponsorship;
import domain.Team;

@Service
@Transactional
public class GameService {

	// Managed repository

	@Autowired
	private GameRepository		gameRepository;

	// Supporting Services
	@Autowired
	private ActorService		actorService;

	@Autowired
	private SponsorshipService	sponsorshipService;

	@Autowired
	private CompetitionService	competitionService;


	//Simple CRUD methods

	public Game create() {

		final Game result = new Game();

		return result;

	}

	public Collection<Game> findAll() {

		final Collection<Game> result = this.gameRepository.findAll();
		Assert.notNull(result);
		return result;

	}

	public Game findOne(final int gameId) {

		final Game result = this.gameRepository.findOne(gameId);
		return result;

	}

	public Game save(final Game game) {
		Assert.notNull(game);

		final Authority authReferee = new Authority();
		authReferee.setAuthority(Authority.REFEREE);
		final Authority authFederation = new Authority();
		authFederation.setAuthority(Authority.FEDERATION);

		//Hay que estar logeado
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);

		//solo puede guardar partidos referee's y federation's
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authReferee) || actor.getUserAccount().getAuthorities().contains(authFederation));

		//posthacking referee y federation
		if (actor.getUserAccount().getAuthorities().contains(authReferee))
			Assert.isTrue(game.getReferee().getId() == actor.getId());
		else {
			final Competition competition = this.competitionService.findCompetitionByGameId(game.getId());
			Assert.isTrue(competition.getFederation().getId() == actor.getId());
		}

		//si el que guarda partido es referee el partido es amistoso, si es federation es competitivo
		if (actor.getUserAccount().getAuthorities().contains(authReferee))
			Assert.isTrue(game.getFriendly());
		else if (actor.getUserAccount().getAuthorities().contains(authFederation))
			Assert.isTrue(!game.getFriendly());

		//Ya sea al crearse o editarse, la fecha ha de ser futura
		final Date currentDate = new Date(System.currentTimeMillis() - 1000);
		Assert.isTrue(game.getGameDate().after(currentDate));

		//el lugar del partido debe coincidir con el estadio del equipo local
		final Team homeTeam = game.getHomeTeam();
		Assert.isTrue(game.getPlace() == homeTeam.getStadiumName());

		final Game result = this.gameRepository.save(game);

		return result;

	}
	public void delete(final Game game) {
		Assert.notNull(game);

		final Authority authReferee = new Authority();
		authReferee.setAuthority(Authority.REFEREE);

		//Hay que estar logeado
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);

		//solo pueden borrar partidos referee's
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authReferee));

		//posthacking referee y federation
		Assert.isTrue(game.getReferee().getId() == actor.getId());

		//la fecha ha de ser futura
		final Date currentDate = new Date(System.currentTimeMillis() - 1000);
		Assert.isTrue(game.getGameDate().after(currentDate));

		//eliminacion relacion con sponsorship
		final Collection<Sponsorship> sponsorships = this.sponsorshipService.findSponsorshipsByGameId(game.getId());
		for (final Sponsorship s : sponsorships)
			this.sponsorshipService.deleteForDeleteGame(s);

		//Solo se pueden borrar partidos amistosos
		Assert.isTrue(game.getFriendly());

		this.gameRepository.delete(game);

	}
	//Other business methods
	public Collection<Game> findGamesOfTeam(final int teamId) {
		return this.gameRepository.findNextGamesOfTeam(teamId);
	}

	public Collection<Game> findAllGamesOrdered() {
		return this.gameRepository.findAllGamesOrdered();
	}

	public Collection<Game> findAllEndedGamesWithoutMinutes() {

		final Collection<Game> res = this.gameRepository.findAllEndedGamesWithoutMinutes();

		return res;
	}

}
