
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {

	@Query("select g from Game g where (g.homeTeam.id=?1 or g.visitorTeam=?1) and g.gameDate > current_date")
	Collection<Game> findNextGamesOfTeam(int teamId);

	@Query("select g from Game g order by g.gameDate desc")
	Collection<Game> findAllGamesOrdered();

}
