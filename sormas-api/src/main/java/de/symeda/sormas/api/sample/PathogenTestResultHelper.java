package de.symeda.sormas.api.sample;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

	public static PathogenTestResultType resolveFinalIliResult(PathogenTestResultType... results) {
		List<PathogenTestResultType> enteredResults = Arrays.stream(results).filter(Objects::nonNull).collect(Collectors.toList());
		if (enteredResults.isEmpty()) {
			return null;
		}
		if (enteredResults.contains(PathogenTestResultType.POSITIVE)) {
			return PathogenTestResultType.POSITIVE;
		}
		if (enteredResults.contains(PathogenTestResultType.PENDING)) {
			return resolveFinalResult(enteredResults);
		}
		if (enteredResults.stream().allMatch(result -> result == PathogenTestResultType.INDETERMINATE)) {
			return PathogenTestResultType.PENDING;
		}
		if (enteredResults.contains(PathogenTestResultType.NEGATIVE)) {
			return PathogenTestResultType.NEGATIVE;
		}
		return resolveFinalResult(enteredResults);
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
