
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Player;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class PlayerServiceTest extends AbstractTest {

	//The SUT----------------------------------------------------
	@Autowired
	private PlayerService	playerService;


	/*
	 * ----CALCULATE COVERAGE----
	 * The previous delivery, we calculate it manually. In this one instead we are using the plugin called EclEmma,
	 * with which we can automatically calculate the percentage.
	 * 
	 * Each of the test have their result just before them, and the coverage of the complete test is shown at the end of the document.
	 */

	/*
	 * ACME.CHAMPIONS
	 * a)(Level C) Requirement 9.1: An actor who is not authenticated must be able to register to the system as a player.
	 * 
	 * b) Negative cases:
	 * 2. SquadName = blank
	 * 3. SquadName = null
	 * 
	 * c) Sentence coverage
	 * -create(): 100%
	 * -save(): 64,2%
	 * 
	 * d) Data coverage
	 * -Player: 7,14286%
	 */

	@Test
	public void driverRegisterPlayer() {
		final Object testingData[][] = {
			{
				"name1", "surnames", "https://google.com", "email1@gmail.com", "672195205", "address1", "GOALKEEPER", "PORTERO", "7", "Raúl", "playerTest", "playerTest", null
			},//1. All fine
			{
				"name1", "surnames", "https://google.com", "email1@gmail.com", "672195205", "address1", "GOALKEEPER", "PORTERO", "7", "		", "playerTest", "playerTest", ConstraintViolationException.class
			},//2. SquadName = blank
			{
				"name1", "surnames", "https://google.com", "email1@gmail.com", "672195205", "address1", "GOALKEEPER", "PORTERO", "7", null, "playerTest", "playerTest", ConstraintViolationException.class
			},//3. SquadName = null

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateRegisterPlayer((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], Integer.valueOf((String) testingData[i][8]), (String) testingData[i][9], (String) testingData[i][10], (String) testingData[i][11], (Class<?>) testingData[i][12]);
	}

	protected void templateRegisterPlayer(final String name, final String surnames, final String photo, final String email, final String phone, final String address, final String positionEnglish, final String positionSpanish, final Integer squadNumber,
		final String squadName, final String username, final String password, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.startTransaction();

			final Player player = this.playerService.create();

			player.setName(name);
			player.setSurnames(surnames);
			player.setPhoto(photo);
			player.setEmail(email);
			player.setPhone(phone);
			player.setAddress(address);

			player.setBuyoutClause(0.0);
			player.setInjured(false);
			player.setPositionEnglish(positionEnglish);
			player.setPositionSpanish(positionSpanish);
			player.setSquadName(squadName);
			player.setSquadNumber(squadNumber);
			player.setPunished(false);

			player.getUserAccount().setUsername(username);
			player.getUserAccount().setPassword(password);

			this.playerService.save(player);
			this.playerService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();
		super.checkExceptions(expected, caught);

	}

	/*
	 * ACME.CHAMPIONS
	 * a)(Level C) Requirement 10.2: An actor who is authenticated must be able to edit their personal data.
	 * 
	 * b) Negative cases:
	 * 2. SquadNumber < 1
	 * 3. SquadNumber > 99
	 * 
	 * c) Sentence coverage
	 * -findOne(): 100%
	 * -save(): 30,2%
	 * 
	 * d) Data coverage
	 * -Player: 7,14286%
	 */

	@Test
	public void driverEditPlayer() {
		final Object testingData[][] = {
			{
				"name1", "surnames", "https://google.com", "email1@gmail.com", "672195205", "address1", "GOALKEEPER", "PORTERO", "7", "Raúl", "player1", "player1", null
			},//1. All fine
			{
				"name1", "surnames", "https://google.com", "email1@gmail.com", "672195205", "address1", "GOALKEEPER", "PORTERO", "-7", "Raúl", "player1", "player1", ConstraintViolationException.class
			},//2. SquadNumber < 1
			{
				"name1", "surnames", "https://google.com", "email1@gmail.com", "672195205", "address1", "GOALKEEPER", "PORTERO", "200", "Raúl", "player1", "player1", ConstraintViolationException.class
			},//3. SquadNumber > 99

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditPlayer((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], Integer.valueOf((String) testingData[i][8]), (String) testingData[i][9], (String) testingData[i][10], (String) testingData[i][11], (Class<?>) testingData[i][12]);
	}

	protected void templateEditPlayer(final String name, final String surnames, final String photo, final String email, final String phone, final String address, final String positionEnglish, final String positionSpanish, final Integer squadNumber,
		final String squadName, final String username, final String bean, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.startTransaction();

			this.authenticate(username);

			final Player player = this.playerService.findOne(super.getEntityId(bean));

			player.setName(name);
			player.setSurnames(surnames);
			player.setPhoto(photo);
			player.setEmail(email);
			player.setPhone(phone);
			player.setAddress(address);

			player.setBuyoutClause(0.0);
			player.setInjured(false);
			player.setPositionEnglish(positionEnglish);
			player.setPositionSpanish(positionSpanish);
			player.setSquadName(squadName);
			player.setSquadNumber(squadNumber);
			player.setPunished(false);

			this.playerService.save(player);
			this.playerService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();
		super.checkExceptions(expected, caught);

	}

	/*
	 * -------Coverage PlayerService-------
	 * 
	 * ----TOTAL SENTENCE COVERAGE:
	 * PlayerService = 37,3%
	 * 
	 * ----TOTAL DATA COVERAGE:
	 * Player = 14,28571%
	 */
}
