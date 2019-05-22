
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.GameRepository;
import domain.Game;

@Service
@Transactional
public class GameService {

	// Managed repository

	@Autowired
	private GameRepository	gameRepository;


	//Other business methods
	public Collection<Game> findGamesOfTeam(final int teamId) {
		return this.gameRepository.findNextGamesOfTeam(teamId);
	}

	public Collection<Game> findAllGamesOrdered() {
		return this.gameRepository.findAllGamesOrdered();
	}

}
