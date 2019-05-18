
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repositories.PlayerRecordRepository;

@Service
@Transactional
public class PlayerRecordService {

	// Managed Repository ------------------------
	@Autowired
	private PlayerRecordRepository	playerRecordRepository;

	// Suporting services ------------------------
}
