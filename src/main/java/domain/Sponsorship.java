
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Sponsorship extends DomainEntity {

	private CreditCard	creditCard;
	private String		banner;
	private String		target;
	//relationships
	private Sponsor		sponsor;
	private Match		match;
	private Player		player;
	private Team		team;


	@NotNull
	@Valid
	public CreditCard getCreditCard() {
		return this.creditCard;
	}
	public void setCreditCard(final CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	@NotNull
	@URL
	@SafeHtml
	public String getBanner() {
		return this.banner;
	}
	public void setBanner(final String banner) {
		this.banner = banner;
	}

	@NotNull
	@URL
	@SafeHtml
	public String getTarget() {
		return this.target;
	}
	public void setTarget(final String target) {
		this.target = target;
	}

	@Valid
	@ManyToOne(optional = false)
	public Sponsor getSponsor() {
		return this.sponsor;
	}

	public void setSponsor(final Sponsor sponsor) {
		this.sponsor = sponsor;
	}

	@Valid
	@ManyToOne(optional = true)
	public Match getMatch() {
		return this.match;
	}

	public void setMatch(final Match match) {
		this.match = match;
	}

	@Valid
	@ManyToOne(optional = true)
	public Player getPlayer() {
		return this.player;
	}

	public void setPlayer(final Player player) {
		this.player = player;
	}

	@Valid
	@ManyToOne(optional = true)
	public Team getTeam() {
		return this.team;
	}

	public void setTeam(final Team team) {
		this.team = team;
	}

}
