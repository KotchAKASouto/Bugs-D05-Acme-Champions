
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.CreditCard;
import domain.Sponsor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class SponsorServiceTest extends AbstractTest {

	//The SUT----------------------------------------------------
	@Autowired
	private SponsorService	sponsorService;


	/*
	 * ----CALCULATE COVERAGE----
	 * The previous delivery, we calculate it manually. In this one instead we are using the plugin called EclEmma,
	 * with which we can automatically calculate the percentage.
	 * 
	 * Each of the test have their result just before them, and the coverage of the complete test is shown at the end of the document.
	 */

	@Test
	public void driverRegisterSponsor() {
		final Object testingData[][] = {
			{
				"name1", "surnames", "https://google.com", "email1@gmail.com", "672195205", "address1", "sponsorTest", "sponsorTest", "123", "12", "2020", "Iñigo Montoya", "VISA", "1111222233334444", null
			},//1. All fine
			{
				"name1", "surnames", "notUrl", "email1@gmail.com", "672195205", "address1", "sponsorTest", "sponsorTest", "123", "12", "2020", "Iñigo Montoya", "VISA", "1111222233334444", ConstraintViolationException.class
			},//2. Photo = not url
			{
				"name1", "surnames", "<script>Hola mundo<script/>", "email1@gmail.com", "672195205", "address1", "sponsorTest", "sponsorTest", "123", "12", "2020", "Iñigo Montoya", "VISA", "1111222233334444", ConstraintViolationException.class
			},//2. Photo = not url

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateRegisterSponsor((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], Integer.valueOf((String) testingData[i][8]), Integer.valueOf((String) testingData[i][9]), Integer.valueOf((String) testingData[i][10]), (String) testingData[i][11], (String) testingData[i][12],
				(String) testingData[i][13], (Class<?>) testingData[i][14]);
	}

	protected void templateRegisterSponsor(final String name, final String surnames, final String photo, final String email, final String phone, final String address, final String username, final String password, final Integer cvv, final Integer expMonth,
		final Integer expYear, final String holderName, final String make, final String number, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.startTransaction();

			final Sponsor sponsor = this.sponsorService.create();

			sponsor.setName(name);
			sponsor.setSurnames(surnames);
			sponsor.setPhoto(photo);
			sponsor.setEmail(email);
			sponsor.setPhone(phone);
			sponsor.setAddress(address);

			final CreditCard creditCard = new CreditCard();
			creditCard.setCvv(cvv);
			creditCard.setExpMonth(expMonth);
			creditCard.setExpYear(expYear);
			creditCard.setHolderName(holderName);
			creditCard.setMake(make);
			creditCard.setNumber(number);
			sponsor.setCreditCard(creditCard);

			sponsor.getUserAccount().setUsername(username);
			sponsor.getUserAccount().setPassword(password);

			this.sponsorService.save(sponsor);
			this.sponsorService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();
		super.checkExceptions(expected, caught);

	}

	@Test
	public void driverEditSponsor() {
		final Object testingData[][] = {
			{
				"name1", "surnames", "https://google.com", "email1@gmail.com", "672195205", "address1", "sponsor1", "sponsor1", "123", "12", "2020", "Iñigo Montoya", "VISA", "1111222233334444", null
			},//1. All fine
			{
				"name1", "surnames", "https://google.com", "		", "672195205", "address1", "sponsor1", "sponsor1", "123", "12", "2020", "Iñigo Montoya", "VISA", "1111222233334444", ConstraintViolationException.class
			},//2. Email = blank
			{
				"name1", "surnames", "https://google.com", null, "672195205", "address1", "sponsor1", "sponsor1", "123", "12", "2020", "Iñigo Montoya", "VISA", "1111222233334444", ConstraintViolationException.class
			},//3. Email = null
			{
				"name1", "surnames", "https://google.com", "email1", "672195205", "address1", "sponsor1", "sponsor1", "123", "12", "2020", "Iñigo Montoya", "VISA", "1111222233334444", ConstraintViolationException.class
			},//4. Invalid email 

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditSponsor((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], Integer.valueOf((String) testingData[i][8]), Integer.valueOf((String) testingData[i][9]), Integer.valueOf((String) testingData[i][10]), (String) testingData[i][11], (String) testingData[i][12],
				(String) testingData[i][13], (Class<?>) testingData[i][14]);
	}

	protected void templateEditSponsor(final String name, final String surnames, final String photo, final String email, final String phone, final String address, final String username, final String bean, final Integer cvv, final Integer expMonth,
		final Integer expYear, final String holderName, final String make, final String number, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.startTransaction();

			this.authenticate(username);

			final Sponsor sponsor = this.sponsorService.findOne(super.getEntityId(bean));

			sponsor.setName(name);
			sponsor.setSurnames(surnames);
			sponsor.setPhoto(photo);
			sponsor.setEmail(email);
			sponsor.setPhone(phone);
			sponsor.setAddress(address);

			final CreditCard creditCard = new CreditCard();
			creditCard.setCvv(cvv);
			creditCard.setExpMonth(expMonth);
			creditCard.setExpYear(expYear);
			creditCard.setHolderName(holderName);
			creditCard.setMake(make);
			creditCard.setNumber(number);
			sponsor.setCreditCard(creditCard);

			this.sponsorService.save(sponsor);
			this.sponsorService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);

	}

}
