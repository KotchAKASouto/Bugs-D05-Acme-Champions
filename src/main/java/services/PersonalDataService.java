
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repositories.PersonalDataRepository;

@Service
@Transactional
public class PersonalDataService {

	// Managed Repository ------------------------
	@Autowired
	private PersonalDataRepository	personalDataRepository;

	// Suporting services ------------------------
}
