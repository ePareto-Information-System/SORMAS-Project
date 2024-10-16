package de.symeda.sormas.api.user;

import de.symeda.sormas.api.ReferenceDto;
import de.symeda.sormas.api.statistics.StatisticsGroupingKey;

import java.util.Objects;

public class UserRoleReferenceDto extends ReferenceDto implements StatisticsGroupingKey {

	public UserRoleReferenceDto() {
		super();
	}

	public UserRoleReferenceDto(String uuid, String caption) {
		super(uuid, caption);
	}

	@Override
	public int keyCompareTo(StatisticsGroupingKey o) {

		if (o == null) {
			throw new NullPointerException("Can't compare to null.");
		}

		if (this.equals(o)) {
			return 0;
		}

		int captionComparison = this.getCaption().compareTo(((UserRoleReferenceDto) o).getCaption());
		if (captionComparison != 0) {
			return captionComparison;
		} else {
			return this.getUuid().compareTo(((UserRoleReferenceDto) o).getUuid());
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserRoleReferenceDto that = (UserRoleReferenceDto) o;
		return Objects.equals(getCaption(), that.getCaption());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getCaption());
	}
}
