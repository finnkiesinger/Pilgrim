package Utilities;

import java.util.Collection;
import java.util.HashSet;

public class Signature<T> extends HashSet<T> {
    public Signature() {
        super();
    }

    public Signature(Collection<? extends T> c) {
        super(c);
    }

    public static <T> Signature<T> intersect(Signature<T> first, Signature<T> second) {
        Signature<T> intersection = new Signature<>(first);
        intersection.retainAll(second);

        return intersection;
    }

    public static <T> Signature<T> unite(Signature<T> first, Signature<T> second) {
        Signature<T> union = new Signature<>(first);
        union.addAll(second);

        return union;
    }

    public Signature<T> intersection(Signature<T> other) {
        return intersect(this, other);
    }

    public Signature<T> union(Signature<T> other) {
        return unite(this, other);
    }
}
