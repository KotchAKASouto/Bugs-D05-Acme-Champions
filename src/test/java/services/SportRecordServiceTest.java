
package services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.SportRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class SportRecordServiceTest extends AbstractTest {

	//The SUT----------------------------------------------------
	@Autowired
	private SportRecordService	sportRecordService;


	/*
	 * ----CALCULATE COVERAGE----
	 * The previous delivery, we calculate it manually. In this one instead we are using the plugin called EclEmma,
	 * with which we can automatically calculate the percentage.
	 * 
	 * Each of the test have their result just before them, and the coverage of the complete test is shown at the end of the document.
	 */

	@Test
	public void driverCreateSportRecord() {
		final Object testingData[][] = {
			{
				"player1", "1998/06/29", "2000/06/29", "Fútbol", null
			},//1. All fine
			{
				"player1", "1998/06/29", "2000/06/29", null, ConstraintViolationException.class
			},//2. sportName = null
			{
				"player1", "1998/06/29", "2000/06/29", "		", ConstraintViolationException.class
			},//3. sportName = blank
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSportRecord((String) testingData[i][0], this.convertStringToDate((String) testingData[i][1]), this.convertStringToDate((String) testingData[i][2]), (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}
	protected void templateCreateSportRecord(final String username, final Date startDate, final Date endDate, final String sportName, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.startTransaction();

			super.authenticate(username);

			final SportRecord record = this.sportRecordService.create();

			record.setStartDate(startDate);
			record.setEndDate(endDate);
			record.setSportName(sportName);
			record.setTeamSport(true);

			this.sportRecordService.save(record);
			this.sportRecordService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);

	}

	//Methods

	protected Date convertStringToDate(final String dateString) {
		Date date = null;

		if (dateString != null) {
			final DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
			try {
				date = df.parse(dateString);
			} catch (final Exception ex) {
				System.out.println(ex);
			}
		}

		return date;
	}

	protected Double convertStringToDouble(final String doubleString) {
		Double result = null;

		if (doubleString != null)
			result = Double.valueOf(doubleString);

		return result;
	}
}
