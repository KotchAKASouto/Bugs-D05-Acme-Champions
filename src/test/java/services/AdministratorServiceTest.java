
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Administrator;
import domain.CreditCard;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AdministratorServiceTest extends AbstractTest {

	//The SUT--------------------------------------------------
	@Autowired
	private AdministratorService	adminService;


	/*
	 * ----CALCULATE COVERAGE----
	 * The previous delivery, we calculate it manually. In this one instead we are using the plugin called EclEmma,
	 * with which we can automatically calculate the percentage.
	 * 
	 * Each of the test have their result just before them, and the coverage of the complete test is shown at the end of the document.
	 */

	/*
	 * ACME.HACKERRANK
	 * a)(Level C) Requirement 11.1: An actor who is authenticated as an administrator must be able to: Create user accounts for new administrators.
	 * 
	 * b) Negative cases:
	 * 2. Name = blank
	 * 3. Name = null
	 * 
	 * c) Sentence coverage
	 * -create(): 100%
	 * -save():53,3%
	 * d) Data coverage
	 * -Admin: 12,5% (name)
	 */

	@Test
	public void driverRegisterAdmin() {
		final Object testingData[][] = {
			{
				"admin", "name1", "surname1", "12100", "https://google.com", "email1@gmail.com", "cvycjwbi", "visa", "1111222233334444", "12", "2020", "900", "672195205", "address1", "admin55", "admin55", null
			},//1. All fine
			{
				"admin", "		", "surname1", "12100", "https://google.com", "email1@gmail.com", "cvycjwbi", "visa", "1111222233334444", "12", "2020", "900", "672195205", "address1", "admin55", "admin55", ConstraintViolationException.class
			},//2. Name = blank
			{
				"admin", null, "surname1", "12100", "https://google.com", "email1@gmail.com", "cvycjwbi", "visa", "1111222233334444", "12", "2020", "900", "672195205", "address1", "admin55", "admin55", ConstraintViolationException.class
			},//3. Name = null

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateRegisterAdmin((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], Integer.valueOf((String) testingData[i][3]), (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (String) testingData[i][8], Integer.valueOf((String) testingData[i][9]), Integer.valueOf((String) testingData[i][10]), Integer.valueOf((String) testingData[i][11]), (String) testingData[i][12],
				(String) testingData[i][13], (String) testingData[i][14], (String) testingData[i][15], (Class<?>) testingData[i][16]);
	}
	protected void templateRegisterAdmin(final String usernameLogin, final String name, final String surnames, final Integer vat, final String photo, final String email, final String holderName, final String make, final String number,
		final Integer expMonth, final Integer expYear, final Integer cvv, final String phone, final String address, final String username, final String password, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			this.startTransaction();
			super.authenticate(usernameLogin);

			final CreditCard cc = new CreditCard();
			cc.setHolderName(holderName);
			cc.setMake(make);
			cc.setNumber(number);
			cc.setExpMonth(expMonth);
			cc.setExpYear(expYear);
			cc.setCvv(cvv);

			final Administrator admin = this.adminService.create();

			admin.setName(name);
			admin.setSurnames(surnames);
			admin.setPhoto(photo);
			admin.setEmail(email);
			admin.setPhone(phone);
			admin.setAddress(address);

			admin.getUserAccount().setUsername(username);
			admin.getUserAccount().setPassword(password);

			this.adminService.save(admin);
			this.adminService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.unauthenticate();

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);

	}

	/*
	 * ACME.HACKERRANK
	 * a)(Level C) Requirement 8.2: An actor who is authenticated must be able to: Edit his/her personal data.
	 * 
	 * b) Negative cases:
	 * 2. The expiration year of the credit card is past
	 * 
	 * c) Sentence coverage
	 * -findOne(): 100%
	 * -save():53,3%
	 * d) Data coverage
	 * -Admin: 0%
	 */
	@Test
	public void driverEditAdmin() {
		final Object testingData[][] = {
			{
				"endesa", "name1", "surnames", 12000, "https://google.com", "email1@gmail.com", "672195205", "address1", "admin", "functionalTest", "VISA", "377964663288126", "12", "2020", "123", "administrator1", null
			},//1. All fine
			{
				"endesa", "name1", "surnames", 12000, "https://google.com", "email1gmail.com", "672195205", "address1", "admin", "functionalTest", "VISA", "377964663288126", "12", "2018", "123", "administrator1", ConstraintViolationException.class
			},//2. The expiration year of the credit card is past

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditAdmin((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (String) testingData[i][8], (String) testingData[i][9], (String) testingData[i][10], (String) testingData[i][11], (String) testingData[i][12], (String) testingData[i][13], (String) testingData[i][14],
				(String) testingData[i][15], (Class<?>) testingData[i][16]);
	}

	protected void templateEditAdmin(final String commercialName, final String name, final String surnames, final Integer vat, final String photo, final String email, final String phone, final String address, final String username,
		final String holderName, final String make, final String number, final String expMonth, final String expYear, final String cvv, final String adminToEdit, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			this.authenticate(username);
			final Administrator administrator = this.adminService.findOne(super.getEntityId(adminToEdit));

			administrator.setName(name);
			administrator.setSurnames(surnames);

			administrator.setPhoto(photo);
			administrator.setEmail(email);
			administrator.setPhone(phone);
			administrator.setAddress(address);

			this.adminService.save(administrator);
			this.adminService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();

		}
		this.unauthenticate();
		super.checkExceptions(expected, caught);

	}

	/*
	 * -------Coverage AdministratorService-------
	 * 
	 * ----TOTAL SENTENCE COVERAGE:
	 * AdministratorService = 58%
	 * 
	 * ----TOTAL DATA COVERAGE:
	 * Administrator = 12,5%%
	 */

}
