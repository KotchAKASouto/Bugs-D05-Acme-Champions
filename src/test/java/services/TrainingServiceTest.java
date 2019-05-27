
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class TrainingServiceTest extends AbstractTest {

	//The SUT----------------------------------------------------

	@Autowired
	private TrainingService	trainingService;

	@Autowired
	private PlayerService	playerService;


	/*
	 * ACME-ROOKIES
	 * a)(Level A) Requirement 13.1: Providers manage his/her sponsorships : List
	 * 
	 * b) Negative cases:
	 * 2. Incorrect results
	 * 
	 * c) Sentence coverage
	 * -findAllBySponsorId() = 100%
	 * 
	 * d) Data coverage
	 * -Sponsorship = 0%
	 */

	@Test
	public void driverListTrainingOfPlayer() {
		final Object testingData[][] = {

			{
				"player1", 1, null
			},//1. All fine 
			{
				null, 1000, IllegalArgumentException.class
			},//2. Incorrect results

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListTrainingOfPlayer((String) testingData[i][0], (Integer) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	protected void templateListTrainingOfPlayer(final String username, final Integer expectedInt, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			super.authenticate(username);

			final int id = this.playerService.findByPrincipal().getId();

			final Integer result = this.trainingService.findTrainingsByPlayerId(id).size();
			Assert.isTrue(expectedInt == result);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	/*
	 * -------Coverage TrainingService-------
	 * 
	 * ----TOTAL SENTENCE COVERAGE:
	 * TrainingService = 47,3%
	 * 
	 * ----TOTAL DATA COVERAGE:
	 */

}
