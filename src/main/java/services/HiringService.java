package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import domain.Hiring;
import domain.Signing;
import repositories.HiringRepository;

@Service
@Transactional
public class HiringService {
	
	// Managed repositories
	
	@Autowired
	private HiringRepository hiringRepository;
	
	// --------
	
	public void delete(final Hiring hiring) {

		Assert.notNull(hiring);
		Assert.isTrue(hiring.getId() != 0);

		this.hiringRepository.delete(hiring);

	}
	
	public Hiring findHiringOfPresidentAndManager(int presidentId, int managerId) {
		return this.hiringRepository.findHiringOfPresidentAndManager(presidentId, managerId);
	}

}
