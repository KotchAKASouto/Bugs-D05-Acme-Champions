
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.PersonalDataRepository;
import security.Authority;
import domain.Actor;
import domain.PersonalData;
import forms.CreateHistoryForm;

@Service
@Transactional
public class PersonalDataService {

	// Managed Repository ------------------------
	@Autowired
	private PersonalDataRepository	personalDataRepository;

	// Suporting services ------------------------

	private ActorService			actorService;

	@Autowired
	private Validator				validator;


	// Simple CRUD methods -----------------------

	public PersonalData create() {

		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		final Authority authority = new Authority();
		authority.setAuthority(Authority.PLAYER);
		Assert.isTrue((actor.getUserAccount().getAuthorities().contains(authority)));

		PersonalData result;
		result = new PersonalData();

		return result;
	}

	public Collection<PersonalData> findAll() {

		Collection<PersonalData> result;

		result = this.personalDataRepository.findAll();

		return result;
	}

	public PersonalData findOne(final int personalId) {

		Assert.notNull(personalId);
		PersonalData result;
		result = this.personalDataRepository.findOne(personalId);
		return result;
	}

	public PersonalData save(final PersonalData personal) {

		Assert.notNull(personal);

		PersonalData result;

		result = this.personalDataRepository.save(personal);

		return result;

	}

	public PersonalData reconstruct(final CreateHistoryForm form, final BindingResult binding) {

		final PersonalData result = this.create();

		this.validator.validate(form, binding);

		result.setPhotos(form.getPhotos());
		result.setSocialNetworkProfilelink(form.getSocialNetworkProfilelink());

		return result;

	}

	//	public PersonalData reconstruct(final PersonalDataForm form, final BindingResult binding) {
	//
	//		final Actor actor = this.actorService.findByPrincipal();
	//
	//		final String fullName = actor.getName() + " " + actor.getSurnames();
	//
	//		final PersonalData result = this.create();
	//
	//		this.validator.validate(form, binding);
	//
	//		result.setId(form.getId());
	//		result.setVersion(form.getVersion());
	//		result.setPhotos(form.getPhotos());
	//		result.setSocialNetworkProfilelink(form.getSocialNetworkProfilelink());
	//
	//		return result;
	//
	//	}
}
