
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {

	@Query("select p from Player p where p.userAccount.id = ?1")
	Player findByUserAccountId(int userAccountId);

	@Query("select p from Player p where (p.name like ?1 or p.surnames like ?1) and (p.positionEnglish like ?2 or p.positionSpanish like ?2)")
	Collection<Player> findPlayersByFinder(String keyword, String position);
}
