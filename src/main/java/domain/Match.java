
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Match extends DomainEntity {

	private Date	matchDate;
	private String	place;

	private Team	homeTeam;
	private Team	visitorTeam;
	private Referee	referee;


	@NotNull
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
	public Date getMatchDate() {
		return this.matchDate;
	}

	public void setMatchDate(final Date matchDate) {
		this.matchDate = matchDate;
	}

	@SafeHtml
	@NotBlank
	public String getPlace() {
		return this.place;
	}

	public void setPlace(final String place) {
		this.place = place;
	}

	@Valid
	@ManyToOne(optional = false)
	public Team getHomeTeam() {
		return this.homeTeam;
	}

	public void setHomeTeam(final Team homeTeam) {
		this.homeTeam = homeTeam;
	}

	@Valid
	@ManyToOne(optional = false)
	public Team getVisitorTeam() {
		return this.visitorTeam;
	}

	public void setVisitorTeam(final Team visitorTeam) {
		this.visitorTeam = visitorTeam;
	}

	@Valid
	@ManyToOne(optional = false)
	public Referee getReferee() {
		return this.referee;
	}

	public void setReferee(final Referee referee) {
		this.referee = referee;
	}

}
