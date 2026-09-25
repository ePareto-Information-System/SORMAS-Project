package de.symeda.sormas.api.sample;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import de.symeda.sormas.api.utils.PosNeg;

public class PathogenTestResultHelperTest {

	@Test
	public void shouldResolveCombinedPathogenResults() {
		assertEquals(
			PathogenTestResultType.POSITIVE,
			PathogenTestResultHelper.resolveFinalResult(
				PathogenTestResultType.NEGATIVE,
				PathogenTestResultType.POSITIVE,
				PathogenTestResultType.PENDING));
		assertEquals(
			PathogenTestResultType.PENDING,
			PathogenTestResultHelper.resolveFinalResult(PathogenTestResultType.NEGATIVE, PathogenTestResultType.PENDING, null));
		assertEquals(
			PathogenTestResultType.NEGATIVE,
			PathogenTestResultHelper.resolveFinalResult(
				PathogenTestResultType.NEGATIVE,
				PathogenTestResultType.NEGATIVE,
				PathogenTestResultType.NEGATIVE));
		assertNull(PathogenTestResultHelper.resolveFinalResult(null, null, null));
	}

	@Test
	public void shouldResolveVhfSpecificResults() {
		assertEquals(
			PathogenTestResultType.POSITIVE,
			PathogenTestResultHelper.resolveFinalVhfResult(PosNeg.NEGATIVE, PosNeg.POSITIVE));
		assertEquals(PathogenTestResultType.NEGATIVE, PathogenTestResultHelper.resolveFinalVhfResult(PosNeg.NEGATIVE, null));
		assertNull(PathogenTestResultHelper.resolveFinalVhfResult(null, null));
	}
}
