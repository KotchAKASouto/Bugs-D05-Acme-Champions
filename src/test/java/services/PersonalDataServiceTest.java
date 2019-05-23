
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.PersonalData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class PersonalDataServiceTest extends AbstractTest {

	//The SUT----------------------------------------------------
	@Autowired
	private PersonalDataService	personalDataService;


	/*
	 * ----CALCULATE COVERAGE----
	 * The previous delivery, we calculate it manually. In this one instead we are using the plugin called EclEmma,
	 * with which we can automatically calculate the percentage.
	 * 
	 * Each of the test have their result just before them, and the coverage of the complete test is shown at the end of the document.
	 */

	@Test
	public void driverCreatePersonalData() {
		final Object testingData[][] = {
			{
				"player1", "http://test.com/", "http://test.com/", null
			},//1. All fine
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreatePersonalData((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	protected void templateCreatePersonalData(final String username, final String photo, final String socialNetworkProfilelink, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.startTransaction();

			super.authenticate(username);

			final PersonalData data = this.personalDataService.create();

			final Collection<String> photos = new HashSet<>();
			photos.add(photo);
			data.setPhotos(photos);
			data.setSocialNetworkProfilelink(socialNetworkProfilelink);

			this.personalDataService.save(data);
			this.personalDataService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);

	}
}
