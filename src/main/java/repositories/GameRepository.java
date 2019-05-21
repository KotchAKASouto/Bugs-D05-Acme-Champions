
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {

	@Query("select g from Game g where g.team.id=?1")
	Collection<Game> findGamesOfTeam(int teamId);
	
}
