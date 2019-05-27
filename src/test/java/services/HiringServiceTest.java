
package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Hiring;
import forms.HiringForm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
public class HiringServiceTest extends AbstractTest {

	//The SUT----------------------------------------------------
	@Autowired
	private HiringService	hiringService;


	/*
	 * ----CALCULATE COVERAGE----
	 * The previous delivery, we calculate it manually. In this one instead we are using the plugin called EclEmma,
	 * with which we can automatically calculate the percentage.
	 * 
	 * Each of the test have their result just before them, and the coverage of the complete test is shown at the end of the document.
	 */

	/*
	 * ACME.HACKERRANK
	 * a)(Level A) Requirement 24.3: An actor who is authenticated as an administrator must be able to: Ban an actor
	 * 
	 * b) Negative cases:
	 * 2. Ban yourself
	 * 
	 * c) Sentence coverage
	 * -banOrUnBanActor(): 58.5%
	 * 
	 * d) Data coverage
	 * -Actor: 0%
	 */

	@Test
	public void driverCreate() {

		final Object testingData[][] = {

			{
				"president1", "manager2", null
			}, //1. All fine
			{
				"administrator1", "manager2", IllegalArgumentException.class
			}, //2. Wrong authority
			{
				null, "manager2", IllegalArgumentException.class
			}, //3. No loged actor

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	protected void templateCreate(final String actorBean, final String manager, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {

			this.startTransaction();

			super.authenticate(actorBean);

			final HiringForm hiring = this.hiringService.create(super.getEntityId(manager));

			Assert.notNull(hiring);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);

	}

	@Test
	public void driverList() {

		final Object testingData[][] = {

			{
				"president1", null
			}, //1. All fine
			{
				"administrator1", IllegalArgumentException.class
			}, //2. Wrong authority
			{
				"referee1", IllegalArgumentException.class
			}, //3. No loged actor

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateList((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	protected void templateList(final String actorBean, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {

			this.startTransaction();

			super.authenticate(actorBean);

			final Collection<Hiring> hiring = this.hiringService.findByPresident(super.getEntityId(actorBean));

			Assert.notNull(hiring);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);

	}

	@Test
	public void driverSave() {

		final Object testingData[][] = {

			{
				"president1", "manager2", null
			}, //1. All fine
			{
				"administrator1", "manager2", IllegalArgumentException.class
			}, //2. Wrong authority
			{
				null, "manager2", IllegalArgumentException.class
			}, //3. No loged actor

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateSave((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	protected void templateSave(final String actorBean, final String manager, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {

			this.startTransaction();

			super.authenticate(actorBean);

			final HiringForm hiringForm = this.hiringService.create(super.getEntityId(manager));

			hiringForm.setPrice(150000.50);

			final Hiring hiring = this.hiringService.reconstruct(hiringForm, null);

			final Hiring saved = this.hiringService.save(hiring);

			Assert.notNull(saved);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);

	}

	/*
	 * ACME.HACKERRANK
	 * a)(Level A) Requirement 24.4: An actor who is authenticated as an administrator must be able to: Unban an actor
	 * 
	 * b) Negative cases:
	 * 2. Unban yourself
	 * 
	 * c) Sentence coverage
	 * -banOrUnBanActor(): 56.1%
	 * 
	 * d) Data coverage
	 * -Actor: 0%
	 */

	/*
	 * -------Coverage ActorService-------
	 * 
	 * ----TOTAL SENTENCE COVERAGE:
	 * ActorService = 25,8%
	 * 
	 * ----TOTAL DATA COVERAGE:
	 * Actor = 0%
	 */
}
