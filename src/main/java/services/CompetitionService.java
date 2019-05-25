
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CompetitionRepository;
import security.Authority;
import security.UserAccountService;
import domain.Actor;
import domain.Competition;
import domain.Game;
import domain.Minutes;
import domain.Referee;
import domain.Team;

@Service
@Transactional
public class CompetitionService {

	//Managed Repository ---------------------------------------------------
	@Autowired
	private CompetitionRepository	competitionRepository;

	//Supporting services --------------------------------------------------

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private UserAccountService		userAccountService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private SponsorshipService		sponsorshipService;

	@Autowired
	private FinderService			finderService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private RefereeService			refereeService;

	@Autowired
	private GameService			gameService;

	@Autowired
	private TeamService			teamService;

	@Autowired
	private MinutesService			minutesService;

	//Simple CRUD methods --------------------------------------------------

	public Collection<Competition> findAll() {

		final Collection<Competition> competition = this.competitionRepository.findAll();

		Assert.notNull(competition);

		return competition;
	}

	public Competition findOne(final int ActorId) {

		final Competition competition = this.competitionRepository.findOne(ActorId);

		Assert.notNull(competition);

		return competition;
	}

	public Competition save(final Competition competition) {
		
		final Competition res;

		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		final Authority authority = new Authority();
		authority.setAuthority(Authority.FEDERATION);
		Assert.isTrue((actor.getUserAccount().getAuthorities().contains(authority)));
		
		if (competition.getId() == 0) {
			
			res = this.competitionRepository.save(competition);
			
		} else {
			
			if (competition.getClosed()) {
				
				Date start = competition.getStartDate();
				
				List<Referee> referees = (List<Referee>) refereeService.findAll();
				
				Collection<Game> games = competition.getGames();
				
				Collection<Team> teams = competition.getTeams();
				
				if (competition.getFormat().getType() == "LEAGUE") {

					for (Team team : teams) {
						
						for (Team team2 : teams) {
							
							if (!team.equals(team2)) {
								
								Game game = new Game();
								
								start.setTime(start.getTime() + 86400000);

								Random rand = new Random();
								Referee random = referees.get(rand.nextInt(referees.size()));
								
								game.setFriendly(false);
								game.setGameDate(start);
								game.setHomeTeam(team);
								game.setPlace(team.getStadiumName());
								game.setVisitorTeam(team2);
								game.setReferee(random);
								
								Game saved = gameService.save(game);
								
								games.add(saved);
																
							}
						}
					}
					
					competition.setGames(games);
					
				} else {
					
					List<Team> teamsThisRound = (List<Team>) competition.getTeams();
					
					Integer gamesToPlay = competition.getTeams().size() / 2;
					
					for (int i = 0; i <= gamesToPlay; i++) {
						
						Random rand1 = new Random();
						Team team1 = teamsThisRound.get(rand1.nextInt(teamsThisRound.size()));
						
						Random rand2 = new Random();
						Team team2 = teamsThisRound.get(rand2.nextInt(teamsThisRound.size()));
						
						Game game = new Game();
						
						start.setTime(start.getTime() + 86400000);

						Random rand = new Random();
						Referee random = referees.get(rand.nextInt(referees.size()));
						
						game.setFriendly(false);
						game.setGameDate(start);
						game.setHomeTeam(team1);
						game.setPlace(team1.getStadiumName());
						game.setVisitorTeam(team2);
						game.setReferee(random);
						
						Game saved = gameService.save(game);
						
						games.add(saved);
						
					}
					
				}
				
			}
			
			res = this.competitionRepository.save(competition);
			
		}


		Assert.notNull(res);

		return res;
	}
	
	public void nextRounds(Competition competition, Minutes minutes) {
				
		Collection<Game> gamesAll = gameService.findByCompetitionId(competition.getId());
		Collection<Game> gamesNow = competition.getGames();
		
		Collection<Game> actualGames = new ArrayList<>();
		
		actualGames.addAll(gamesAll);
		actualGames.removeAll(gamesNow);
		
		if (competition.getFormat().getType() == "LEAGUE") {
			
			if (gamesAll.size() == competition.getTeams().size()*(competition.getTeams().size()-1)*2) {
				
//				for (Game game : games) {
//					
//					Minutes minutes = minutesService.findMinuteByGameId(game.getId());
//					
//					Map<Team, Integer> map = new HashMap<>();
//					
//				}
//
//				winner.setTrackRecord(winner.getTrackRecord() + 1);
//				
//				teamService.save(winner);
				
			}
			
		} else {
			
			List<Referee> referees = (List<Referee>) refereeService.findAll();
			
			if (actualGames.size() >= 2) {
				
				List<Team> teams = new ArrayList<>();
				
				for (Game game : actualGames) {

					Minutes minute = minutesService.findMinuteByGameId(game.getId());
					
					teams.add(minute.getWinner());
					
				}
				
				Integer gamesToPlay = teams.size() / 2;
				
				Date start = minutes.getGame().getGameDate();
				
				for (int i = 0; i <= gamesToPlay; i++) {
					
					Random rand1 = new Random();
					Team team1 = teams.get(rand1.nextInt(teams.size()));
					
					Random rand2 = new Random();
					Team team2 = teams.get(rand2.nextInt(teams.size()));
					
					Game game = new Game();
					
					start.setTime(start.getTime() + 86400000);

					Random rand = new Random();
					Referee random = referees.get(rand.nextInt(referees.size()));
					
					game.setFriendly(false);
					game.setGameDate(start);
					game.setHomeTeam(team1);
					game.setPlace(team1.getStadiumName());
					game.setVisitorTeam(team2);
					game.setReferee(random);
					
					Game saved = gameService.save(game);
					
					gamesNow.add(saved);
					
					competition.setGames(gamesNow);
				
			}
			
		}
	}
		
	}

	//Other business methods----------------------------

	public Boolean exist(final int competitiond) {
		Boolean res = false;

		final Competition competition = this.competitionRepository.findOne(competitiond);

		if (competition != null)
			res = true;

		return res;

	}

	public Competition findCompetitionByGameId(final int gameId) {

		final Competition res = this.competitionRepository.findCompetitionByGameId(gameId);

		return res;
	}

}
