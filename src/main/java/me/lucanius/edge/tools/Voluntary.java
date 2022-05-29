package me.lucanius.edge.tools;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Lucanius
 * @since May 28, 2022
 *
 * Taken from my Twilight project.
 */
public class Voluntary<V> {

    /**
     * Empty Voluntary object that we can use to not throw any NullPointerExceptions.
     * This is static because it's not needed to be created every time.
     */
    private final static Voluntary<?> EMPTY = new Voluntary<>();

    private final V value;

    private Voluntary() {
        this.value = null;
    }

    private Voluntary(V value) {
        this.value = value;
    }

    /**
     * Will throw a NullPointerException if the value is null.
     */
    public static <V> Voluntary<V> of(V value) {
        return new Voluntary<>(value);
    }

    /**
     * Won't throw a NullPointerException if the value is null.
     */
    public static <V> Voluntary<V> ofNull(V value) {
        return value != null ? new Voluntary<>(value) : empty();
    }

    @SuppressWarnings("unchecked")
    public static <V> Voluntary<V> empty() {
        return (Voluntary<V>) EMPTY;
    }

    public V get() {
        return value;
    }

    public V orElse(V other) {
        return value != null ? value : other;
    }

    public boolean isPresent() {
        return value != null;
    }

    public Voluntary<V> ifPresent(Consumer<? super V> consumer) {
        if (value != null) consumer.accept(value);
        return this;
    }

    public void orElseDo(Consumer<? super V> consumer) {
        if (value == null) consumer.accept(null);
    }

    public Voluntary<V> filter(Predicate<? super V> predicate) {
        return value != null && predicate.test(value) ? this : empty();
    }

    public <U> Voluntary<U> map(Function<? super V, ? extends U> mapper) {
        return value != null ? new Voluntary<>(mapper.apply(value)) : empty();
    }

    public <U> Voluntary<U> flatMap(Function<? super V, ? extends Voluntary<U>> mapper) {
        return value != null ? mapper.apply(value) : empty();
    }

    public static <V> Voluntary<V> ofOptional(Optional<V> optional) {
        return optional.map(Voluntary::new).orElseGet(Voluntary::empty);
    }

    public Optional<V> toOptional() {
        return Optional.ofNullable(value);
    }
}
