
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CompetitionRepository;
import security.UserAccountService;
import domain.Competition;

@Service
@Transactional
public class CompetitionService {

	//Managed Repository ---------------------------------------------------
	@Autowired
	private CompetitionRepository	competitionRepository;

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

	public Collection<Competition> findAll() {

		final Collection<Competition> competition = this.competitionRepository.findAll();

		Assert.notNull(competition);

		return competition;
	}

	public Competition findOne(final int ActorId) {

		final Competition competition = this.competitionRepository.findOne(ActorId);

		Assert.notNull(competition);

		return competition;
	}

	public Competition save(final Competition competition) {

		final Competition res = this.competitionRepository.save(competition);

		Assert.notNull(res);

		return res;
	}

	//Other business methods----------------------------

	public Boolean exist(final int competitiond) {
		Boolean res = false;

		final Competition competition = this.competitionRepository.findOne(competitiond);

		if (competition != null)
			res = true;

		return res;

	}

}
