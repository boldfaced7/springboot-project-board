package com.boldfaced7.board;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ObjectAssert;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Assertion<T> {
    Consumer<Supplier<T>> assertion;

    public Assertion() {
        assertion = target -> {
            T result = target.get();

            if (result instanceof List) {
                Assertions.assertThat((List<?>) result).isNotEmpty();
            } else {
                Assertions.assertThat(result).isNotNull();
            }
        };
    }

    public Assertion(T expected) {
        assertion = target -> Assertions.assertThat(target.get()).isEqualTo(expected);
    }

    public Assertion(String name, Object value) {
        assertion = target -> Assertions.assertThat(target.get()).hasFieldOrPropertyWithValue(name, value);
    }

    public Assertion(Map<String, Object> map) {
        assertion = target -> {
            ObjectAssert<T> assertThat = Assertions.assertThat(target.get());
            map.forEach(assertThat::hasFieldOrPropertyWithValue);
        };
    }

    public Assertion(Class<?> exceptionType) {
        assertion = target -> Assertions.assertThatThrownBy(target::get).isInstanceOf(exceptionType);
    }

    public Assertion(Class<?> exceptionType, String message) {
        assertion = target -> Assertions.assertThatThrownBy(target::get)
                .isInstanceOf(exceptionType)
                .hasMessage(message);
    }
    public Runnable convert(Supplier<T> method) {
        return () -> assertion.accept(method);
    }

    public <P> Runnable convert(Consumer<P> method, P param, T validationObject) {
        Supplier<T> toSupplier = () -> {
            method.accept(param);
            return validationObject;
        };
        return () -> assertion.accept(toSupplier);
    }

    public <P> Runnable convert(Function<P, T> method, P param) {
        Supplier<T> toSupplier = () -> method.apply(param);
        return () -> assertion.accept(toSupplier);
    }

}
