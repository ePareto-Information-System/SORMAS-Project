/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package de.symeda.sormas.app.backend.afpimmunization;

import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_DEFAULT;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import de.symeda.sormas.api.riskfactor.DrinkingWaterSource;
import de.symeda.sormas.api.utils.CardRecall;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.app.backend.common.EmbeddedAdo;
import de.symeda.sormas.app.backend.common.PseudonymizableAdo;

@Entity(name = AfpImmunization.TABLE_NAME)
@DatabaseTable(tableName = AfpImmunization.TABLE_NAME)
@EmbeddedAdo
public class AfpImmunization extends PseudonymizableAdo {

	private static final long serialVersionUID = -8294812479501735785L;

	public static final String TABLE_NAME = "afpimmunization";
	public static final String I18N_PREFIX = "CaseAfpImmunization";

	@Column
	private Integer totalNumberDoses;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date opvDoseAtBirth;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date secondDose;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date fourthDose;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date firstDose;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date thirdDose;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date lastDose;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String totalOpvDosesReceivedThroughSia;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String totalOpvDosesReceivedThroughRi;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateLastOpvDosesReceivedThroughSia;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String totalIpvDosesReceivedThroughSia;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String totalIpvDosesReceivedThroughRi;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateLastIpvDosesReceivedThroughSia;
	@Enumerated(EnumType.STRING)
	private CardRecall sourceRiVaccinationInformation;

	public Integer getTotalNumberDoses() {
		return totalNumberDoses;
	}

	public void setTotalNumberDoses(Integer totalNumberDoses) {
		this.totalNumberDoses = totalNumberDoses;
	}

	public Date getOpvDoseAtBirth() {
		return opvDoseAtBirth;
	}

	public void setOpvDoseAtBirth(Date opvDoseAtBirth) {
		this.opvDoseAtBirth = opvDoseAtBirth;
	}

	public Date getSecondDose() {
		return secondDose;
	}

	public void setSecondDose(Date secondDose) {
		this.secondDose = secondDose;
	}

	public Date getFourthDose() {
		return fourthDose;
	}

	public void setFourthDose(Date fourthDose) {
		this.fourthDose = fourthDose;
	}

	public Date getFirstDose() {
		return firstDose;
	}

	public void setFirstDose(Date firstDose) {
		this.firstDose = firstDose;
	}

	public Date getThirdDose() {
		return thirdDose;
	}

	public void setThirdDose(Date thirdDose) {
		this.thirdDose = thirdDose;
	}

	public Date getLastDose() {
		return lastDose;
	}

	public void setLastDose(Date lastDose) {
		this.lastDose = lastDose;
	}

	public String getTotalOpvDosesReceivedThroughSia() {
		return totalOpvDosesReceivedThroughSia;
	}

	public void setTotalOpvDosesReceivedThroughSia(String totalOpvDosesReceivedThroughSia) {
		this.totalOpvDosesReceivedThroughSia = totalOpvDosesReceivedThroughSia;
	}

	public String getTotalOpvDosesReceivedThroughRi() {
		return totalOpvDosesReceivedThroughRi;
	}

	public void setTotalOpvDosesReceivedThroughRi(String totalOpvDosesReceivedThroughRi) {
		this.totalOpvDosesReceivedThroughRi = totalOpvDosesReceivedThroughRi;
	}

	public Date getDateLastOpvDosesReceivedThroughSia() {
		return dateLastOpvDosesReceivedThroughSia;
	}

	public void setDateLastOpvDosesReceivedThroughSia(Date dateLastOpvDosesReceivedThroughSia) {
		this.dateLastOpvDosesReceivedThroughSia = dateLastOpvDosesReceivedThroughSia;
	}

	public String getTotalIpvDosesReceivedThroughSia() {
		return totalIpvDosesReceivedThroughSia;
	}

	public void setTotalIpvDosesReceivedThroughSia(String totalIpvDosesReceivedThroughSia) {
		this.totalIpvDosesReceivedThroughSia = totalIpvDosesReceivedThroughSia;
	}

	public String getTotalIpvDosesReceivedThroughRi() {
		return totalIpvDosesReceivedThroughRi;
	}

	public void setTotalIpvDosesReceivedThroughRi(String totalIpvDosesReceivedThroughRi) {
		this.totalIpvDosesReceivedThroughRi = totalIpvDosesReceivedThroughRi;
	}

	public Date getDateLastIpvDosesReceivedThroughSia() {
		return dateLastIpvDosesReceivedThroughSia;
	}

	public void setDateLastIpvDosesReceivedThroughSia(Date dateLastIpvDosesReceivedThroughSia) {
		this.dateLastIpvDosesReceivedThroughSia = dateLastIpvDosesReceivedThroughSia;
	}

	public CardRecall getSourceRiVaccinationInformation() {
		return sourceRiVaccinationInformation;
	}

	public void setSourceRiVaccinationInformation(CardRecall sourceRiVaccinationInformation) {
		this.sourceRiVaccinationInformation = sourceRiVaccinationInformation;
	}

	@Override
	public String getI18nPrefix() {
		return I18N_PREFIX;
	}





}
