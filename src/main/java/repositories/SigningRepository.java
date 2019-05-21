
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Signing;

@Repository
public interface SigningRepository extends JpaRepository<Signing, Integer> {
	
	@Query("select s from Signing s where s.president=?1 and s.player=?2")
	Signing findSigningOfPresidentAndPlayer(int presidentId, int playerId);

}
