
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.FormatRepository;
import security.Authority;
import domain.Actor;
import domain.Format;

@Service
@Transactional
public class FormatService {

	// Managed Repository --------------------
	@Autowired
	private FormatRepository	formatRepository;

	// Supporting Services -------------------
	@Autowired
	private ActorService		actorService;


	//Simple CRUD methods

	public Format create() {

		final Format result = new Format();

		return result;

	}

	public Collection<Format> findAll() {

		final Collection<Format> result = this.formatRepository.findAll();
		Assert.notNull(result);
		return result;

	}

	public Format findOne(final int formatId) {

		final Format result = this.formatRepository.findOne(formatId);
		return result;

	}

	public Format save(final Format format) {
		Assert.notNull(format);

		final Authority authFederation = new Authority();
		authFederation.setAuthority(Authority.FEDERATION);

		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);

		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authFederation));

		final Format result = this.formatRepository.save(format);

		return result;

	}

	public void delete(final Format format) {
		Assert.notNull(format);

		final Authority authFederation = new Authority();
		authFederation.setAuthority(Authority.FEDERATION);

		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);

		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authFederation));

		Assert.isTrue(format.getFederation().getId() == actor.getId());

		this.formatRepository.delete(format);

	}

	// Methods -------------------------------

	public Collection<Format> findFormatByFederationId(final int federationId) {
		return this.formatRepository.findFormatByFederationId(federationId);
	}

	public Boolean exist(final int formatId) {

		Boolean res = false;

		final Format format = this.formatRepository.findOne(formatId);

		if (format != null)
			res = true;

		return res;
	}

}
