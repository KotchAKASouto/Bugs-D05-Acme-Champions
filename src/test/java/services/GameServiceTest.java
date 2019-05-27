
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Game;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class GameServiceTest extends AbstractTest {

	//The SUT----------------------------------------------------
	@Autowired
	private GameService	gameService;


	/*
	 * ----CALCULATE COVERAGE----
	 * The previous delivery, we calculate it manually. In this one instead we are using the plugin called EclEmma,
	 * with which we can automatically calculate the percentage.
	 * 
	 * Each of the test have their result just before them, and the coverage of the complete test is shown at the end of the document.
	 */

	/*
	 * ACME.CHAMPIONS
	 * a)(Level B) Requirement 29.2: An actor who is not authenticated must be able to: View next matches as well as the already finished ones.
	 * 
	 * b) Negative cases:
	 * 2. The number of games is incorrect
	 * 3. The number of games is null
	 * 
	 * c) Sentence coverage
	 * -findAllGamesOrdered():100%
	 * 
	 * d) Data coverage
	 * -Game: 0%
	 */

	@Test
	public void driverListGamesOrdered() {
		final Object testingData[][] = {
			{
				2, null
			},//1. All fine
			{
				7, IllegalArgumentException.class
			},//2. The number of games is incorrect
			{
				null, NullPointerException.class
			},//3. The number of games is null

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListGamesOrdered((Integer) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void templateListGamesOrdered(final Integer number, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.startTransaction();

			final Collection<Game> games = this.gameService.findAllGamesOrdered();

			Assert.isTrue(games.size() == number);
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}
		this.rollbackTransaction();
		super.checkExceptions(expected, caught);

	}

}
