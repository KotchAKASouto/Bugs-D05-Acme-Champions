
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

@Entity
@Access(AccessType.PROPERTY)
public class Match extends DomainEntity {

	private Date	matchDate;
	private String	place;


	public Date getMatchDate() {
		return this.matchDate;
	}

	public void setMatchDate(final Date matchDate) {
		this.matchDate = matchDate;
	}

	public String getPlace() {
		return this.place;
	}

	public void setPlace(final String place) {
		this.place = place;
	}

}
