
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

@Entity
@Access(AccessType.PROPERTY)
public class Competition extends DomainEntity {

	private Date		startDate;
	private Date		endDate;
	private String		nameTrophy;

	private Federation	federation;
	private Collection<Team>


	@NotNull
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}

	@NotNull
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(final Date endDate) {
		this.endDate = endDate;
	}

	@SafeHtml
	@NotBlank
	public String getNameTrophy() {
		return this.nameTrophy;
	}

	public void setNameTrophy(final String nameTrophy) {
		this.nameTrophy = nameTrophy;
	}

	@Valid
	@ManyToOne(optional = false)
	public Federation getFederation() {
		return this.federation;
	}

	public void setFederation(final Federation federation) {
		this.federation = federation;
	}

}
