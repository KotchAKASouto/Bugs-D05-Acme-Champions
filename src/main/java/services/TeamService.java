
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.TeamRepository;
import security.Authority;
import domain.Actor;
import domain.Manager;
import domain.Player;
import domain.President;
import domain.Team;

@Service
@Transactional
public class TeamService {

	// Managed repository

	@Autowired
	private TeamRepository		teamRepository;

	// Suporting services

	@Autowired
	private ActorService		actorService;

	@Autowired
	private PresidentService	presidentService;

	@Autowired
	private Validator			validator;


	// Simple CRUD methods
	public Team create() {

		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		final Authority authority = new Authority();
		authority.setAuthority(Authority.PRESIDENT);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));

		final Team result = new Team();

		final Date establishmentDate = new Date(System.currentTimeMillis() - 1000);

		final Boolean f = false;

		result.setEstablishmentDate(establishmentDate);
		result.setFunctional(f);
		result.setPresident(this.presidentService.findByPrincipal());

		return result;

	}

	public Collection<Team> findAll() {

		final Collection<Team> team = this.teamRepository.findAll();

		Assert.notNull(team);

		return team;
	}

	public Team findOne(final int teamId) {

		final Team team = this.teamRepository.findOne(teamId);

		return team;

	}
	public Team save(final Team team) {
		Assert.notNull(team);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		final Authority pres = new Authority();
		pres.setAuthority(Authority.PRESIDENT);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(pres));
		Assert.isTrue(actor.getId() == team.getPresident().getId());

		Team result;

		final Collection<Player> players = this.findPlayersByTeamId(team.getId());

		final Manager m = this.findManagerByTeamId(team.getId());

		if (players.size() > 4 && m != null)
			team.setFunctional(true);
		else
			team.setFunctional(false);

		result = this.teamRepository.save(team);

		return result;
	}

	//Other Business methods----------------------------------------------------

	public Collection<Player> findPlayersByTeamId(final int teamId) {

		final Collection<Player> result = this.teamRepository.findPlayersByTeamId(teamId);

		return result;

	}

	public Manager findManagerByTeamId(final int teamId) {

		final Manager result = this.teamRepository.findManagerByTeamId(teamId);

		return result;

	}

	public Team findTeamByPresidentId(final int presidentId) {

		final Team result = this.teamRepository.findTeamByPresidentId(presidentId);

		return result;

	}

	public Team reconstruct(final Team team, final BindingResult binding) {

		Team result = team;
		final Team teamNew = this.create();

		if (team.getId() == 0 || team == null) {

			team.setPresident(teamNew.getPresident());

			this.validator.validate(team, binding);

			result = team;
		} else {

			final Team teamBBDD = this.findOne(team.getId());

			team.setPresident(teamBBDD.getPresident());

			this.validator.validate(team, binding);

		}

		return result;

	}

	public Team getTeamByPresidentId(final int presidentId) {
		Team result;
		Assert.notNull(presidentId);
		result = this.teamRepository.findTeamByPresidentId(presidentId);
		return result;
	}

	public Boolean teamPresidentSecurity(final int teamId) {
		Boolean res = false;
		final Team team = this.findOne(teamId);

		final President owner = team.getPresident();

		final President login = this.presidentService.findByPrincipal();

		if (login.equals(owner))
			res = true;

		return res;
	}

	public void flush() {
		this.teamRepository.flush();
	}

}
