package de.symeda.sormas.api.statistics;

import java.io.Serializable;
import java.math.BigDecimal;

import de.symeda.sormas.api.infrastructure.InfrastructureHelper;
import de.symeda.sormas.api.utils.DataHelper;

public class StatisticsContactCountDto implements Serializable {

	private static final long serialVersionUID = 3090861241209018655L;

	private Integer contactCount;
	private Integer population;
	private StatisticsGroupingKey rowKey;
	private StatisticsGroupingKey columnKey;

	public StatisticsContactCountDto(Integer contactCount, Integer population, StatisticsGroupingKey rowKey, StatisticsGroupingKey columnKey) {

		super();
		this.contactCount = contactCount;
		this.population = population;
		this.rowKey = rowKey;
		this.columnKey = columnKey;
	}

	public Integer getContactCount() {
		return contactCount;
	}

	public void setContactCount(Integer contactCount) {
		this.contactCount = contactCount;
	}

	public Integer getPopulation() {
		return population;
	}

	public void setPopulation(Integer population) {
		this.population = population;
	}

	public StatisticsGroupingKey getRowKey() {
		return rowKey;
	}

	public void setRowKey(StatisticsGroupingKey rowKey) {
		this.rowKey = rowKey;
	}

	public StatisticsGroupingKey getColumnKey() {
		return columnKey;
	}

	public void setColumnKey(StatisticsGroupingKey columnKey) {
		this.columnKey = columnKey;
	}

	public BigDecimal getIncidence(int divisor) {

		if (population == null) {
			return null;
		}
		if (contactCount == null || contactCount.intValue() == 0) {
			return BigDecimal.ZERO;
		}

		return InfrastructureHelper.getContactIncidence(contactCount.intValue(), population.intValue(), divisor);
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof StatisticsContactCountDto) {
			StatisticsContactCountDto other = (StatisticsContactCountDto) obj;
			return DataHelper.equal(rowKey, other.rowKey) && DataHelper.equal(columnKey, other.columnKey);
		}

		return super.equals(obj);
	}

	@Override
	public int hashCode() {

		int prime = 31;
		int result = 1;
		result = prime * result + ((rowKey == null) ? 0 : rowKey.hashCode());
		result = prime * result + ((columnKey == null) ? 0 : columnKey.hashCode());
		return result;
	}
}
