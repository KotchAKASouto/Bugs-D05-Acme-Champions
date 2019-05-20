
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class Format extends DomainEntity {

	private String					type;
	private Integer					maximumTeams;
	private Integer					minimumTeams;

	private Collection<Competition>	competitions;


	@SafeHtml
	@NotBlank
	public String getType() {
		return this.type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	@Min(2)
	public Integer getMaximumTeams() {
		return this.maximumTeams;
	}

	public void setMaximumTeams(final Integer maximumTeams) {
		this.maximumTeams = maximumTeams;
	}

	@Min(2)
	public Integer getMinimumTeams() {
		return this.minimumTeams;
	}

	public void setMinimumTeams(final Integer minimumTeams) {
		this.minimumTeams = minimumTeams;
	}

	@ElementCollection
	public Collection<Competition> getCompetitions() {
		return this.competitions;
	}

	@Valid
	@OneToMany
	public void setCompetitions(final Collection<Competition> competitions) {
		this.competitions = competitions;
	}

}
