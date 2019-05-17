
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

@Entity
@Access(AccessType.PROPERTY)
public class Player extends Actor {

	private String	positionEnglish;
	private String	positisonSpanish;
	private Boolean	injured;
	private Boolean	punished;


	public String getPositionEnglish() {
		return this.positionEnglish;
	}

	public void setPositionEnglish(final String positionEnglish) {
		this.positionEnglish = positionEnglish;
	}

	public String getPositisonSpanish() {
		return this.positisonSpanish;
	}

	public void setPositisonSpanish(final String positisonSpanish) {
		this.positisonSpanish = positisonSpanish;
	}

	public Boolean getInjured() {
		return this.injured;
	}

	public void setInjured(final Boolean injured) {
		this.injured = injured;
	}

	public Boolean getPunished() {
		return this.punished;
	}

	public void setPunished(final Boolean punished) {
		this.punished = punished;
	}
}
