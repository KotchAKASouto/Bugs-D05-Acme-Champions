
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Manager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ManagerServiceTest extends AbstractTest {

	//The SUT----------------------------------------------------
	@Autowired
	private ManagerService			managerService;

	@Autowired
	private ConfigurationService	configurationService;


	/*
	 * ----CALCULATE COVERAGE----
	 * The previous delivery, we calculate it manually. In this one instead we are using the plugin called EclEmma,
	 * with which we can automatically calculate the percentage.
	 * 
	 * Each of the test have their result just before them, and the coverage of the complete test is shown at the end of the document.
	 */

	@Test
	public void driverRegisterManager() {
		final Object testingData[][] = {
			{
				"name1", "surnames", "https://google.com", "email1@gmail.com", "672195205", "address1", "managerTest", "managerTest", null
			},//1. All fine
			{
				"		", "surnames", "https://google.com", "email1@gmail.com", "672195205", "address1", "managerTest", "managerTest", ConstraintViolationException.class
			},//2. Name = blank
			{
				null, "surnames", "https://google.com", "email1@gmail.com", "672195205", "address1", "managerTest", "managerTest", ConstraintViolationException.class
			},//3. Name = null

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateRegisterManager((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (Class<?>) testingData[i][8]);
	}

	protected void templateRegisterManager(final String name, final String surnames, final String photo, final String email, final String phone, final String address, final String username, final String password, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.startTransaction();

			final Manager manager = this.managerService.create();

			manager.setName(name);
			manager.setSurnames(surnames);
			manager.setPhoto(photo);
			manager.setEmail(email);
			manager.setPhone(phone);
			manager.setAddress(address);

			manager.getUserAccount().setUsername(username);
			manager.getUserAccount().setPassword(password);

			this.managerService.save(manager);
			this.managerService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();
		super.checkExceptions(expected, caught);

	}

	@Test
	public void driverEditManager() {
		final Object testingData[][] = {
			{
				"name1", "surnames", "https://google.com", "email1@gmail.com", "672195205", "address1", "manager1", "manager1", null
			},//1. All fine
			{
				"name1", "surnames", "https://google.com", "		", "672195205", "address1", "manager1", "manager1", ConstraintViolationException.class
			},//2. Email = blank
			{
				"name1", "surnames", "https://google.com", null, "672195205", "address1", "manager1", "manager1", ConstraintViolationException.class
			},//3. Email = null
			{
				"name1", "surnames", "https://google.com", "noEmail", "672195205", "address1", "manager1", "manager1", ConstraintViolationException.class
			},//4. Invalid email

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditManager((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (Class<?>) testingData[i][8]);
	}

	protected void templateEditManager(final String name, final String surnames, final String photo, final String email, final String phone, final String address, final String username, final String bean, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.startTransaction();

			this.authenticate(username);

			final Manager manager = this.managerService.findOne(super.getEntityId(bean));

			manager.setName(name);
			manager.setSurnames(surnames);
			manager.setPhoto(photo);
			manager.setEmail(email);
			manager.setPhone(phone);
			manager.setAddress(address);

			this.managerService.save(manager);
			this.managerService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();
		super.checkExceptions(expected, caught);

	}

	@Test
	public void driverGoalPrediction() {
		final Object testingData[][] = {

			{
				"manager1", 2.5, null
			},//1. All fine 
			{
				null, 999.999, IllegalArgumentException.class
			},//2. Incorrect results

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateGoalPrediction((String) testingData[i][0], (Double) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	protected void templateGoalPrediction(final String username, final Double expectedValue, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			super.authenticate(username);

			final int id = this.managerService.findByPrincipal().getId();

			final Manager manager = this.managerService.findOne(id);

			final Double result = this.configurationService.goalPrediction(manager.getTeam().getId());
			Assert.isTrue(expectedValue == result);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}
}
