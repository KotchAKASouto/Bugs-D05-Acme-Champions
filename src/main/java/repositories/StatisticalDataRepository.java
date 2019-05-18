
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.StatisticalData;

@Repository
public interface StatisticalDataRepository extends JpaRepository<StatisticalData, Integer> {

}
