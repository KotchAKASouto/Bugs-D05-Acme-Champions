
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Hiring;

@Repository
public interface HiringRepository extends JpaRepository<Hiring, Integer> {
	
	@Query("select h from Hiring h where h.president.id=?1 and h.manager.id=?2")
	Hiring findHiringOfPresidentAndManager(int presidentId, int managerId);

}
