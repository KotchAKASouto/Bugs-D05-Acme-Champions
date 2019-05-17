
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Hiring;

@Repository
public interface HiringRepository extends JpaRepository<Hiring, Integer> {

}
