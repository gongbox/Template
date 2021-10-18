package com.gongbo.common.utils;

import java.util.AbstractList;
import java.util.Objects;
import java.util.function.BiFunction;

public class RangeList<E extends Comparable<? super E>> extends AbstractList<E> {

    private final Range<E> range;
    private final int size;

    private final BiFunction<E, Integer, E> stepTo;

    public static <E extends Comparable<? super E>> RangeList<E> of(Range<E> range, BiFunction<E, Integer, E> stepTo) {
        return new RangeList<>(range, stepTo);
    }

    public RangeList(Range<E> range, BiFunction<E, Integer, E> stepTo) {
        if (range == null || range.getStart() == null || range.getEnd() == null || stepTo == null) {
            throw new NullPointerException();
        }
        if (range.getEnd().compareTo(range.getStart()) < 0) {
            throw new IllegalArgumentException("the range end is small then range start!");
        }
        this.range = range;
        this.stepTo = stepTo;
        this.size = applySize();
    }

    @Override
    public E get(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        return stepTo.apply(range.getStart(), index);
    }

    @Override
    public int size() {
        return size;
    }


    /**
     * 计算size
     */
    private int applySize() {
        E temp = range.getStart();
        E end = range.getEnd();
        int addValue = 1;
        int sizeTemp = 1;

        while (true) {
            int compare = temp.compareTo(end);
            if (compare < 0) {
                addValue = addValue << 1;
                temp = stepTo.apply(temp, addValue);
                sizeTemp = sizeTemp + addValue;
            } else if (compare > 0) {
                addValue = Math.max(addValue >> 1, 1);
                temp = stepTo.apply(temp, -addValue);
                sizeTemp = sizeTemp - addValue;
            } else {
                break;
            }
        }
        return sizeTemp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RangeList<?> rangeList = (RangeList<?>) o;
        if (size != rangeList.size || !Objects.equals(range, rangeList.range)) {
            return false;
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), range, size);
    }
}
