
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Finder extends DomainEntity {

	//Atributos-----------------------------------------------------------------------
	private String	keyWord;
	private Double	minimumSalary;
	private Double	maximumSalary;
	private String	maximumDeadline;
	private Date	lastUpdate;


	//Getters y Setters----------------------------------------------------------------

	@SafeHtml
	public String getKeyWord() {
		return this.keyWord;
	}

	public void setKeyWord(final String keyWord) {
		this.keyWord = keyWord;
	}

	public Double getMinimumSalary() {
		return this.minimumSalary;
	}

	public void setMinimumSalary(final Double minimumSalary) {
		this.minimumSalary = minimumSalary;
	}

	@Pattern(regexp = "((?:19|20)\\d\\d)/(0?[1-9]|1[012])/([12][0-9]|3[01]|0?[1-9])|| ")
	@SafeHtml
	public String getMaximumDeadline() {
		return this.maximumDeadline;
	}

	public void setMaximumDeadline(final String maximumDeadline) {
		this.maximumDeadline = maximumDeadline;
	}

	public Double getMaximumSalary() {
		return this.maximumSalary;
	}

	public void setMaximumSalary(final Double maximumSalary) {
		this.maximumSalary = maximumSalary;
	}

	@Past
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(final Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}


	// Relationships ----------------------------------------------------------

	private Collection<Manager>	managers;
	private Collection<Player>	players;
	private President			president;


	@ManyToMany
	@Valid
	public Collection<Manager> getManagers() {
		return this.managers;
	}

	public void setManagers(final Collection<Manager> managers) {
		this.managers = managers;
	}

	@ManyToMany
	@Valid
	public Collection<Player> getPlayers() {
		return this.players;
	}

	public void setPlayers(final Collection<Player> players) {
		this.players = players;
	}

	@OneToOne(optional = false)
	@Valid
	public President getPresident() {
		return this.president;
	}

	public void setPresident(final President president) {
		this.president = president;
	}

}
