
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.President;

@Repository
public interface PresidentRepository extends JpaRepository<President, Integer> {

}
