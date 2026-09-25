package de.symeda.sormas.api.sample;

import java.util.Arrays;
import java.util.Collection;

import de.symeda.sormas.api.utils.PosNeg;

public final class PathogenTestResultHelper {

	private PathogenTestResultHelper() {
	}

	public static PathogenTestResultType resolveFinalResult(PathogenTestResultType... results) {
		return resolveFinalResult(Arrays.asList(results));
	}

	public static PathogenTestResultType resolveFinalResult(Collection<PathogenTestResultType> results) {
		if (results.contains(PathogenTestResultType.POSITIVE)) {
			return PathogenTestResultType.POSITIVE;
		}
		if (results.contains(PathogenTestResultType.PENDING)) {
			return PathogenTestResultType.PENDING;
		}
		if (results.contains(PathogenTestResultType.INDETERMINATE)) {
			return PathogenTestResultType.INDETERMINATE;
		}
		if (results.contains(PathogenTestResultType.NEGATIVE)) {
			return PathogenTestResultType.NEGATIVE;
		}
		if (results.contains(PathogenTestResultType.NOT_DONE)) {
			return PathogenTestResultType.NOT_DONE;
		}
		return null;
	}

	public static PathogenTestResultType resolveFinalVhfResult(PosNeg... results) {
		if (Arrays.asList(results).contains(PosNeg.POSITIVE)) {
			return PathogenTestResultType.POSITIVE;
		}
		if (Arrays.asList(results).contains(PosNeg.NEGATIVE)) {
			return PathogenTestResultType.NEGATIVE;
		}
		return null;
	}
}
