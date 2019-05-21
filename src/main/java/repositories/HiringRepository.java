
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Hiring;

@Repository
public interface HiringRepository extends JpaRepository<Hiring, Integer> {

	@Query("Select h from Hiring h where h.manager.id = ?1 and h.status = 'PENDING'")
	Collection<Hiring> findByManagerId(int id);

}
