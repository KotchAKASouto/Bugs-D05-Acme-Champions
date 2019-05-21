package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import domain.Game;
import repositories.GameRepository;

@Service
@Transactional
public class GameService {
	
	// Managed repository

	@Autowired
	private GameRepository		gameRepository;
		
	public Collection<Game> findGamesOfTeam(final int teamId) {
		return this.gameRepository.findGamesOfTeam(teamId);
	}

}
