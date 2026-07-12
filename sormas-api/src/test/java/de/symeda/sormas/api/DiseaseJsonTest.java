package de.symeda.sormas.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DiseaseJsonTest {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	public void shouldMapLegacyAhfToUnspecifiedVhf() throws Exception {
		assertEquals(Disease.UNSPECIFIED_VHF, objectMapper.readValue("\"AHF\"", Disease.class));
	}

	@Test
	public void shouldKeepCurrentDiseaseSerialization() throws Exception {
		assertEquals(Disease.UNSPECIFIED_VHF, objectMapper.readValue("\"UNSPECIFIED_VHF\"", Disease.class));
		assertEquals("\"UNSPECIFIED_VHF\"", objectMapper.writeValueAsString(Disease.UNSPECIFIED_VHF));
	}
}
