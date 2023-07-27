package de.symeda.sormas.api.caze.caseimport;

import java.util.Comparator;

public class GenericComparator<T extends Comparable<T>> implements Comparator<T> {
    @Override
    public int compare(T item1, T item2) {
        // Define your comparison logic here
        // Return a negative integer if item1 should come before item2
        // Return a positive integer if item1 should come after item2
        // Return 0 if item1 and item2 are considered equal
        return 0;
    }
}

