package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import domain.Actor;
import domain.Message;
import domain.Signing;
import repositories.SigningRepository;

@Service
@Transactional
public class SigningService {
	
	// Managed repository

	@Autowired
	private SigningRepository		signingRepository;
	
	// ----------------
	
	public void delete(final Signing signing) {

		Assert.notNull(signing);
		Assert.isTrue(signing.getId() != 0);

		this.signingRepository.delete(signing);

	}
	
	public Signing findSigningOfPresidentAndPlayer(int presidentId, int playerId) {
		return this.signingRepository.findSigningOfPresidentAndPlayer(presidentId, playerId);
	}

}
