package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import domain.Signing;
import repositories.SigningRepository;

@Service
@Transactional
public class SigningService {
	
	// Managed repository

	@Autowired
	private SigningRepository		signingRepository;
	
	// ----------------
	
	public Signing findSigningOfPresidentAndPlayer(int presidentId, int playerId) {
		return this.signingRepository.findSigningOfPresidentAndPlayer(presidentId, playerId);
	}

}
