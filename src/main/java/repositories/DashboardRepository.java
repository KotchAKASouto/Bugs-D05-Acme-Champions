package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardRepository {
	
	@Query(nativeQuery= true, value= "select avg(count) from (select count(*) as Count "
			+ "from training t join manager m on (m.id = t.manager) "
			+ "group by manager) as counts")
	Double avgOfTrainingsPerManager();
	
	@Query(nativeQuery= true, value= "select min(count) from (select count(*) as Count "
			+ "from training t join manager m on (m.id = t.manager) "
			+ "group by manager) as counts")
	Integer minOfTrainingsPerManager();
	
	@Query(nativeQuery= true, value= "select max(count) from (select count(*) as Count "
			+ "from training t join manager m on (m.id = t.manager) "
			+ "group by manager) as counts")
	Integer maxOfTrainingsPerManager();
	
	@Query(nativeQuery= true, value= "select std(count) from (select count(*) as Count "
			+ "from training t join manager m on (m.id = t.manager) "
			+ "group by manager) as counts")
	Double stdOfTrainingsPerManager();
	
	@Query(nativeQuery= true, value= "select avg(duration) from (select TIMESTAMPDIFF(MINUTE,t.start_date,t.ending_date) as duration from training t) as duration")
	Double avgLengthOfTrainings();
	
	@Query(nativeQuery= true, value= "select min(duration) from (select TIMESTAMPDIFF(MINUTE,t.start_date,t.ending_date) as duration from training t) as duration")
	Integer minLengthOfTrainings();
	
	@Query(nativeQuery= true, value= "select max(duration) from (select TIMESTAMPDIFF(MINUTE,t.start_date,t.ending_date) as duration from training t) as duration")
	Integer maxLengthOfTrainings();
	
	@Query(nativeQuery= true, value= "select std(duration) from (select TIMESTAMPDIFF(MINUTE,t.start_date,t.ending_date) as duration from training t) as duration")
	Double stdLengthOfTrainings();
	
	@Query(nativeQuery=true, value= "select avg(count) from (select f.id, count(fp.finder) as Count from finder f left join finder_players fp on (f.id = fp.finder) left join finder_managers fm on (f.id = fm.finder) group by f.id ) as counts")
	Double avgResultsPerFinder();
	
	@Query(nativeQuery=true, value= "select min(count) from (select f.id, count(fp.finder) as Count from finder f left join finder_players fp on (f.id = fp.finder) left join finder_managers fm on (f.id = fm.finder) group by f.id ) as counts")
	Integer minResultsPerFinder();
	
	@Query(nativeQuery=true, value= "select max(count) from (select f.id, count(fp.finder) as Count from finder f left join finder_players fp on (f.id = fp.finder) left join finder_managers fm on (f.id = fm.finder) group by f.id ) as counts")
	Integer maxResultsPerFinder();
	
	@Query(nativeQuery=true, value= "select std(count) from (select f.id, count(fp.finder) as Count from finder f left join finder_players fp on (f.id = fp.finder) left join finder_managers fm on (f.id = fm.finder) group by f.id ) as counts")
	Double stdResultsPerFinder();
	
	@Query(nativeQuery=true, value= "select (select count(*) from player where player.position_english='GOALKEEPER')/(select count(*) from player)")
	Double ratioGoalkeepers();
	
	@Query(nativeQuery=true, value= "select (select count(*) from player where player.position_english='DEFENDER')/(select count(*) from player)")
	Double ratioDefenders();
	
	@Query(nativeQuery=true, value= "select (select count(*) from player where player.position_english='MIDFIELDER')/(select count(*) from player)")
	Double ratioMidfielders();

	@Query(nativeQuery=true, value= "select (select count(*) from player where player.position_english='STRIKER')/(select count(*) from player)")
	Double ratioStrikers();
	
	@Query(nativeQuery=true, value= "select (select count(*) from manager where manager.team is null)/(select count(*) from manager)")
	Double ratioOfManagersWithoutTeam();
	
	@Query(nativeQuery=true, value= "select name from (select t.name as name, count(p.id) as Count from player p right join team t on (t.id = p.team) group by t.id) as counts where Count > (select avg(count) * 0.1 + avg(count) from (select t.id, count(p.id) as Count from player p right join team t on (t.id = p.team) group by t.id) as counts)")
	List<String> superiorTeams();
}
