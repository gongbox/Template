package com.gongbo.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Streams {

    /**
     * 将null值替换为指定值
     */
    public static <T> Function<T, T> nullAs(T value) {
        return t -> {
            if (t == null) {
                return value;
            }
            return t;
        };
    }

    /**
     * 将负数替换为指定值
     */
    public static UnaryOperator<BigDecimal> negativeAs(BigDecimal value) {
        return t -> {
            if (t != null && t.signum() < 0) {
                return value;
            }
            return t;
        };
    }

    /**
     * BigDecimal求取平均值（如果所有值为null，则结果也为null）
     */
    public static Collector<BigDecimal, ?, BigDecimal> averagingBigDecimal(int scale, RoundingMode roundingMode) {
        return averagingBigDecimal(Function.identity(), scale, roundingMode);
    }

    /**
     * BigDecimal求取平均值
     *
     * @param mapper 转换为BigDecimal类型(值不能为null)，
     *               如果值为null时不参与求平均就先过滤掉值为null的元素
     *               如果值为null时参与求平均值就先将值为null的替换为默认值，如0
     * @param scale  结果保留小数位数
     */
    public static <T> Collector<T, ?, BigDecimal> averagingBigDecimal(Function<? super T, BigDecimal> mapper, int scale, RoundingMode roundingMode) {
        return new CollectorImpl<>(
                () -> new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO},
                (a, t) -> {
                    BigDecimal value = mapper.apply(t);
                    if (value != null) {
                        a[0] = a[0].add(value);
                        a[1] = a[1].add(BigDecimal.ONE);
                    }
                },
                (a, b) -> {
                    a[0] = a[0].add(b[0]);
                    a[1] = a[1].add(b[1]);
                    return a;
                },
                a -> (a[1].compareTo(BigDecimal.ZERO) == 0) ? null : a[0].divide(a[1], scale, roundingMode), Collections.emptySet());
    }

    /**
     * BigDecimal求和(如果所有值为null，则结果也为null)
     */
    public static Collector<BigDecimal, ?, BigDecimal> summingBigDecimal() {
        return summingBigDecimal(null, Function.identity());
    }

    /**
     * BigDecimal求和(如果所有值为null，则结果也为null)
     */
    public static <T> Collector<T, ?, BigDecimal> summingBigDecimal(Function<? super T, BigDecimal> mapper) {
        return summingBigDecimal(null, mapper);
    }

    /**
     * BigDecimal求和(如果所有值为null，则结果为initValue)
     */
    public static Collector<BigDecimal, ?, BigDecimal> summingBigDecimal(BigDecimal initValue) {
        return summingBigDecimal(initValue, Function.identity());
    }

    /**
     * BigDecimal求和(如果所有值为null，则结果为initValue)
     */
    public static <T> Collector<T, ?, BigDecimal> summingBigDecimal(BigDecimal initValue, Function<? super T, BigDecimal> mapper) {
        return new CollectorImpl<>(
                () -> new BigDecimal[]{initValue},
                (a, t) -> {
                    BigDecimal value = mapper.apply(t);
                    if (value != null) {
                        a[0] = a[0] == null ? value : a[0].add(value);
                    }
                },
                (a, b) -> {
                    if (a[0] == null && b[0] == null) {
                        return null;
                    } else if (a[0] != null && b[0] == null) {
                        return a;
                    } else if (a[0] == null && b[0] != null) {
                        return b;
                    } else {
                        a[0] = a[0].add(b[0]);
                        return a;
                    }
                },
                a -> a[0], Collections.emptySet());
    }

    /**
     * 求最大值
     */
    public static <T extends Comparable<? super T>> Collector<T, ValueWrapper<T>, T> max() {
        return new CollectorImpl<>(
                () -> ValueWrapper.of(null),
                (w, t) -> {
                    if (w.getValue() == null || t.compareTo(w.getValue()) > 0) {
                        w.setValue(t);
                    }
                },
                (w, b) -> {
                    if (w.getValue() == null) {
                        return b;
                    } else if (b.getValue() == null) {
                        return w;
                    } else if (b.getValue().compareTo(w.getValue()) > 0) {
                        return b;
                    } else {
                        return w;
                    }
                },
                ValueWrapper::getValue,
                Collections.emptySet()
        );
    }

    /**
     * 求最小值
     */
    public static <T extends Comparable<? super T>> Collector<T, ValueWrapper<T>, T> min() {
        return new CollectorImpl<>(
                () -> ValueWrapper.of(null),
                (w, t) -> {
                    if (w.getValue() == null || t.compareTo(w.getValue()) < 0) {
                        w.setValue(t);
                    }
                },
                (w, b) -> {
                    if (w.getValue() == null) {
                        return b;
                    } else if (b.getValue() == null) {
                        return w;
                    } else if (b.getValue().compareTo(w.getValue()) < 0) {
                        return b;
                    } else {
                        return w;
                    }
                },
                ValueWrapper::getValue,
                Collections.emptySet()
        );
    }

    /**
     * 求所有并列最大值
     */
    public static <T> Collector<T, List<T>, List<T>> listMax(Comparator<T> comparator) {
        return new CollectorImpl<>(LinkedList::new, (ts, t) -> {
            if (ts.isEmpty()) {
                ts.add(t);
            } else {
                T t1 = ts.get(0);
                switch (comparator.compare(t1, t)) {
                    case -1:
                        ts.clear();
                    case 0:
                        ts.add(t);
                        break;
                    default:
                        break;
                }
            }
        }, (ts1, ts2) -> {
            if (ts1.isEmpty()) {
                return ts2;
            }
            if (ts2.isEmpty()) {
                return ts1;
            }

            switch (comparator.compare(ts1.get(0), ts2.get(0))) {
                case 0:
                    ts1.addAll(ts2);
                    return ts1;
                case -1:
                    return ts1;
                case 1:
                    return ts2;
                default:
                    return Collections.emptyList();
            }
        }, Function.identity(), Collections.emptySet());
    }

    /**
     * 求所有并列最小值
     */
    public static <T> Collector<T, List<T>, List<T>> listMin(Comparator<T> comparator) {
        return listMax(comparator.reversed());
    }

    private static class CollectorImpl<T, A, R> implements Collector<T, A, R> {
        private final Supplier<A> supplier;
        private final BiConsumer<A, T> accumulator;
        private final BinaryOperator<A> combiner;
        private final Function<A, R> finisher;
        private final Set<Characteristics> characteristics;

        CollectorImpl(Supplier<A> supplier,
                      BiConsumer<A, T> accumulator,
                      BinaryOperator<A> combiner,
                      Function<A, R> finisher,
                      Set<Characteristics> characteristics) {
            this.supplier = supplier;
            this.accumulator = accumulator;
            this.combiner = combiner;
            this.finisher = finisher;
            this.characteristics = characteristics;
        }

        CollectorImpl(Supplier<A> supplier,
                      BiConsumer<A, T> accumulator,
                      BinaryOperator<A> combiner,
                      Set<Characteristics> characteristics) {
            this(supplier, accumulator, combiner, castingIdentity(), characteristics);
        }

        @Override
        public BiConsumer<A, T> accumulator() {
            return accumulator;
        }

        @Override
        public Supplier<A> supplier() {
            return supplier;
        }

        @Override
        public BinaryOperator<A> combiner() {
            return combiner;
        }

        @Override
        public Function<A, R> finisher() {
            return finisher;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return characteristics;
        }
    }

    private static <I, R> Function<I, R> castingIdentity() {
        return i -> (R) i;
    }
}
