
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.MinutesRepository;
import security.Authority;
import domain.Actor;
import domain.Game;
import domain.Minutes;
import domain.Player;
import domain.StatisticalData;

@Service
@Transactional
public class MinutesService {

	// Managed repository

	@Autowired
	private MinutesRepository		minutesRepository;

	// Supporting Services
	@Autowired
	private ActorService			actorService;

	@Autowired
	private StatisticalDataService	statisticalDataService;

	@Autowired
	private PlayerService			playerService;


	//simple CRUD methods

	public Minutes create() {

		final Minutes result = new Minutes();

		return result;

	}

	public Collection<Minutes> findAll() {

		final Collection<Minutes> result = this.minutesRepository.findAll();
		Assert.notNull(result);
		return result;

	}

	public Minutes findOne(final int minutesId) {

		final Minutes result = this.minutesRepository.findOne(minutesId);
		return result;

	}

	public Minutes save(final Minutes minutes) {
		Assert.notNull(minutes);

		final Authority authReferee = new Authority();
		authReferee.setAuthority(Authority.REFEREE);

		//Hay que estar logeado
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);

		//solo puede guardar partidos referee's
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authReferee));

		//el partido debe haber acabado
		final Date currentDate = new Date(System.currentTimeMillis() - 1000);
		Assert.isTrue(minutes.getGame().getGameDate().after(currentDate));

		//tratamiento de goles, calculo del score y ganador
		final Collection<Player> playersScore = minutes.getPlayersScore();
		int goalsHome = 0;
		int goalsVisitor = 0;
		for (final Player p : playersScore) {
			final StatisticalData statisticalData = this.statisticalDataService.findStatisticalDataByPlayerId(p.getId());
			statisticalData.setGoals(statisticalData.getGoals() + 1);
			if (p.getTeam().getStadiumName().equals(minutes.getGame().getPlace()))
				goalsHome = goalsHome + 1;
			else
				goalsVisitor = goalsVisitor + 1;
		}
		minutes.setHomeScore(goalsHome);
		minutes.setVisitorScore(goalsVisitor);
		if (goalsHome > goalsVisitor)
			minutes.setWinner(minutes.getGame().getHomeTeam());
		else if (goalsHome < goalsVisitor)
			minutes.setWinner(minutes.getGame().getVisitorTeam());
		else
			minutes.setWinner(null);

		//actualización partidos jugados
		final Game gameOfMinutes = minutes.getGame();
		final Collection<Player> playersHome = this.playerService.findPlayersOfTeam(gameOfMinutes.getHomeTeam().getId());
		final Collection<Player> playersVisitor = this.playerService.findPlayersOfTeam(gameOfMinutes.getVisitorTeam().getId());
		final Collection<Player> playersOfTheGame = new HashSet<Player>();
		playersOfTheGame.addAll(playersVisitor);
		playersOfTheGame.addAll(playersHome);

		for (final Player p : playersOfTheGame) {
			final StatisticalData statisticalData = this.statisticalDataService.findStatisticalDataByPlayerId(p.getId());
			statisticalData.setMatchsPlayed(statisticalData.getMatchsPlayed() + 1);
			//antes de ver amarillas y rojas reseteamos los que estuvieran castigados
			//de partido anterior
			p.setPunished(false);
			this.playerService.save(p);
		}

		//tratamiento de amarillas
		for (final Player p : minutes.getPlayersYellow()) {
			final StatisticalData statisticalData = this.statisticalDataService.findStatisticalDataByPlayerId(p.getId());
			final int yellowCardsAccumulated = statisticalData.getAccumulatedYellowCard() + 1;
			if (yellowCardsAccumulated == 5) {
				p.setPunished(true);
				statisticalData.setAccumulatedYellowCard(0);
				this.playerService.save(p);
			} else
				statisticalData.setAccumulatedYellowCard(yellowCardsAccumulated);
			statisticalData.setYellowCards(yellowCardsAccumulated);
			this.statisticalDataService.save(statisticalData);
		}

		//tratamiento de rojas
		for (final Player p : minutes.getPlayersRed()) {
			final StatisticalData statisticalData = this.statisticalDataService.findStatisticalDataByPlayerId(p.getId());
			final int redCardsAccumulated = statisticalData.getRedCards();
			statisticalData.setRedCards(redCardsAccumulated + 1);
			this.statisticalDataService.save(statisticalData);
			p.setPunished(true);
			this.playerService.save(p);
		}

		final Minutes result = this.minutesRepository.save(minutes);

		return result;

	}

	//Other business methods

}
