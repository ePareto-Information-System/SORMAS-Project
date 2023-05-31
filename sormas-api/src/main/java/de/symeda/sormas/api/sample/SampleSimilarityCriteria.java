/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2021 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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

package de.symeda.sormas.api.sample;

import de.symeda.sormas.api.caze.CaseCriteria;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.caze.CaseLogic;
import de.symeda.sormas.api.caze.CaseSimilarityCriteria;
import de.symeda.sormas.api.utils.IgnoreForUrl;

import java.io.Serializable;
import java.util.Date;

public class SampleSimilarityCriteria implements Serializable {

	private static final long serialVersionUID = -2051359287159310211L;

	private SampleCriteria sampleCriteria;
	private String labSampleId;
	private Date sampleDateTime;
	private SampleMaterial sampleMaterial;
	private Date reportDate;

	public SampleCriteria getSampleCriteria() {
		return sampleCriteria;
	}

	public SampleSimilarityCriteria sampleCriteria(SampleCriteria sampleCriteria) {
		this.sampleCriteria = sampleCriteria;

		return this;
	}

	public String getLabSampleId() {
		return labSampleId;
	}

	public void setLabSampleId(String labSampleId) {
		this.labSampleId = labSampleId;
	}

	public Date getSampleDateTime() {
		return sampleDateTime;
	}

	public void setSampleDateTime(Date sampleDateTime) {
		this.sampleDateTime = sampleDateTime;
	}

	public SampleMaterial getSampleMaterial() {
		return sampleMaterial;
	}

	public void setSampleMaterial(SampleMaterial sampleMaterial) {
		this.sampleMaterial = sampleMaterial;
	}
	public static SampleSimilarityCriteria forSample(SampleDto sample, String personUuid) {
		SampleCriteria caseCriteria = new SampleCriteria().lab(sample.getLab());
		//return new SampleSimilarityCriteria().personUuid(personUuid).caseCriteria(caseCriteria).reportDate(caze.getReportDate());

		return new SampleSimilarityCriteria().sampleCriteria(caseCriteria).reportDate(sample.getReportDateTime());
	}

	@IgnoreForUrl
	public Date getReportDate() {
		return reportDate;
	}

	public SampleSimilarityCriteria reportDate(Date reportDate) {
		this.reportDate = reportDate;
		return this;
	}
}
