
package services;

import javax.transaction.Transactional;

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

	@Test
	public void driverRegisterPlayer() {
		final Object testingData[][] = {
			{
				"name1", "surnames", "https://google.com", "email1@gmail.com", "672195205", "address1", "GOALKEEPER", "PORTERO", "7", "Raúl", "playerTest", "playerTest", null
			},//1. All fine

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
}
