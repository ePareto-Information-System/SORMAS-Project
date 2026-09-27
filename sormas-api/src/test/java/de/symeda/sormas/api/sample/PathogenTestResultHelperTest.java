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
	public void shouldResolveIliResults() {
		assertEquals(
			PathogenTestResultType.POSITIVE,
			PathogenTestResultHelper.resolveFinalIliResult(
				PathogenTestResultType.NEGATIVE,
				PathogenTestResultType.POSITIVE,
				PathogenTestResultType.NEGATIVE));
		assertEquals(
			PathogenTestResultType.POSITIVE,
			PathogenTestResultHelper.resolveFinalIliResult(
				PathogenTestResultType.INDETERMINATE,
				PathogenTestResultType.NEGATIVE,
				PathogenTestResultType.POSITIVE));
		assertEquals(
			PathogenTestResultType.POSITIVE,
			PathogenTestResultHelper.resolveFinalIliResult(
				PathogenTestResultType.POSITIVE,
				PathogenTestResultType.NEGATIVE,
				PathogenTestResultType.INDETERMINATE));
		assertEquals(
			PathogenTestResultType.NEGATIVE,
			PathogenTestResultHelper.resolveFinalIliResult(
				PathogenTestResultType.NEGATIVE,
				PathogenTestResultType.INDETERMINATE,
				PathogenTestResultType.NEGATIVE));
		assertEquals(
			PathogenTestResultType.NEGATIVE,
			PathogenTestResultHelper.resolveFinalIliResult(
				PathogenTestResultType.NEGATIVE,
				PathogenTestResultType.NEGATIVE,
				PathogenTestResultType.NEGATIVE));
		assertEquals(
			PathogenTestResultType.PENDING,
			PathogenTestResultHelper.resolveFinalIliResult(
				PathogenTestResultType.INDETERMINATE,
				PathogenTestResultType.INDETERMINATE,
				PathogenTestResultType.INDETERMINATE));
		assertEquals(
			PathogenTestResultType.PENDING,
			PathogenTestResultHelper.resolveFinalIliResult(
				PathogenTestResultType.NEGATIVE,
				PathogenTestResultType.PENDING,
				null));
		assertNull(PathogenTestResultHelper.resolveFinalIliResult(null, null, null));
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
