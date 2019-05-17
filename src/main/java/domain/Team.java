
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

@Entity
@Access(AccessType.PROPERTY)
public class Team extends DomainEntity {

	private String		name;
	private String		place;
	private String		badgeUrl;
	private Integer		trackRecord;
	private Date		establishmentDate;

	private President	president;
}
