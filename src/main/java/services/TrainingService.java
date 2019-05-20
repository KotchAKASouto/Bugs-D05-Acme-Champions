
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.TrainingRepository;
import security.Authority;
import domain.Actor;
import domain.Manager;
import domain.Player;
import domain.Training;

@Service
@Transactional
public class TrainingService {

	// Managed repository

	@Autowired
	private TrainingRepository	trainingRepository;

	// Suporting services

	@Autowired
	private ActorService		actorService;

	@Autowired
	private ManagerService		managerService;

	@Autowired
	private Validator			validator;


	// Simple CRUD methods
	public Training create() {

		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		final Authority authority = new Authority();
		authority.setAuthority(Authority.MANAGER);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));

		final Training result = new Training();

		final Collection<Player> players = new HashSet<>();
		result.setPlayers(players);
		result.setManager(this.managerService.findByPrincipal());

		return result;

	}

	public Collection<Training> findAll() {

		final Collection<Training> training = this.trainingRepository.findAll();

		Assert.notNull(training);

		return training;
	}

	public Training findOne(final int trainingId) {

		final Training training = this.trainingRepository.findOne(trainingId);

		return training;

	}
	public Training save(final Training training) {
		Assert.notNull(training);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		final Authority manag = new Authority();
		manag.setAuthority(Authority.MANAGER);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(manag));
		Assert.isTrue(actor.getId() == training.getManager().getId());

		Training result;
		result = this.trainingRepository.save(training);

		return result;
	}

	public void delete(final Training training) {
		Assert.notNull(training);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);

		final Authority manag = new Authority();
		manag.setAuthority(Authority.MANAGER);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(manag));
		Assert.isTrue(actor.getId() == training.getManager().getId());

		this.trainingRepository.delete(training);

	}

	//Other Business methods----------------------------------------------------

	public Collection<Training> findTrainingsByManagerId(final int managerId) {
		final Collection<Training> trainings = this.trainingRepository.findTrainingsByManagerId(managerId);

		return trainings;
	}

	public Collection<Training> findTrainingsByPlayerId(final int playerId) {
		final Collection<Training> trainings = this.trainingRepository.findTrainingsByPlayerId(playerId);

		return trainings;
	}

	public Training reconstruct(final Training training, final BindingResult binding) {

		Training result = training;
		final Training trainingNew = this.create();

		if (training.getId() == 0 || training == null) {

			training.setManager(trainingNew.getManager());

			this.validator.validate(training, binding);

			result = training;
		} else {

			final Training trainingBBDD = this.findOne(training.getId());

			training.setManager(trainingBBDD.getManager());

			this.validator.validate(training, binding);

		}

		return result;

	}
	public Boolean trainingManagerSecurity(final int trainingId) {
		Boolean res = false;
		final Training training = this.findOne(trainingId);

		final Manager owner = training.getManager();

		final Manager login = this.managerService.findByPrincipal();

		if (login.equals(owner))
			res = true;

		return res;
	}

	public void flush() {
		this.trainingRepository.flush();
	}

}
