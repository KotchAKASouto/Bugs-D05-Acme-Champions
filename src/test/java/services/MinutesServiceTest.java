
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Game;
import domain.Minutes;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class MinutesServiceTest extends AbstractTest {

	@Autowired
	private MinutesService	minutesService;

	@Autowired
	private GameService		gameService;


	/*
	 * a)Req31.1: Actors that are authenticated as a referee must be able to:
	 * Write minute to a match only after the match date has passed.
	 * b) Negative cases:
	 * 2. the referee is not the owner
	 * 3. the game has already had a minutes
	 * c) Sentence coverage
	 * 
	 * d) Data coverage
	 */
	@Test
	public void driverMinutes() {
		final Object testingData[][] = {
			{
				"referee1", "game5", "player1", "player2", "player3", null
			},//1. All fine filter
			{
				"referee2", "game5", "player1", "player2", "player3", IllegalArgumentException.class
			},//2. the referee is not the owner
			{
				"referee1", "game1", "player1", "player2", "player3", IllegalArgumentException.class
			},//3. the game has already had a minutes

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateMinutes((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	protected void templateMinutes(final String username, final String game, final String player1, final String player2, final String player3, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(username);

			final Game gameFinded = this.gameService.findOne(super.getEntityId(game));

			final Minutes minutes = this.minutesService.create(gameFinded);

			final Minutes saved = this.minutesService.save(minutes);

			this.minutesService.addPlayerScored(super.getEntityId(player1), saved.getId());

			this.minutesService.addPlayerRedCard(super.getEntityId(player2), saved.getId());

			this.minutesService.addPlayerYellowCard(super.getEntityId(player2), saved.getId());

			this.minutesService.closeMinutes(saved.getId());

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		super.checkExceptions(expected, caught);
	}
}
