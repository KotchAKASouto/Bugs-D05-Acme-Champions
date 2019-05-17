
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Format;

@Repository
public interface FormatRepository extends JpaRepository<Format, Integer> {

}
