package com.boldfaced7.noboilerplate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.mockito.BDDMockito.*;

public class Mocker<T> {

    private final List<Consumer<T>> givens = new ArrayList<>();
    private final List<Consumer<T>> thens = new ArrayList<>();
    private String message;

    public Mocker() {}
    public Mocker(String message) {
        this.message = message;
    }

    public <U> void mocks(Function<T, U> facade, Consumer<U> action) {
        Consumer<T> given = t -> action.accept(willDoNothing().given(facade.apply(t)));
        Consumer<T> then  = t -> action.accept(then(facade.apply(t)).should());
        add(given, then);
    }
    public <U, R> void mocks(Function<T, U> facade, Function<U, R> action, R result) {
        Consumer<T> given = t -> given(action.apply(facade.apply(t))).willReturn(result);
        Consumer<T> then  = t -> action.apply(then(facade.apply(t)).should());
        add(given, then);
    }
    public <U> void mocks(Function<T, U> facade, Consumer<U> action, Class<? extends Throwable> type) {
        Consumer<T> given = t -> action.accept(willThrow(type).given(facade.apply(t)));
        Consumer<T> then  = t -> action.accept(then(facade.apply(t)).should());
        add(given, then);
    }
    public <U> void mocks(Function<T, U> facade, Function<U, ?> action, Class<? extends Throwable> type) {
        Consumer<T> given = t -> action.apply(willThrow(type).given(facade.apply(t)));
        Consumer<T> then  = t -> action.apply(then(facade.apply(t)).should());
        add(given, then);
    }

    public void mocks(Consumer<T> action) {
        Consumer<T> given = t -> action.accept(willDoNothing().given(t));
        Consumer<T> then  = t -> action.accept(then(t).should());
        add(given, then);
    }
    public <R> void mocks(Function<T, R> action, R result) {
        Consumer<T> given = t -> given(action.apply(t)).willReturn(result);
        Consumer<T> then  = t -> action.apply(then(t).should());
        add(given, then);
    }
    public void mocksConsumer(Consumer<T> action, Class<? extends Throwable> type) {
        Consumer<T> given = t -> action.accept(willThrow(type).given(t));
        Consumer<T> then  = t -> action.accept(then(t).should());
        add(given, then);
    }
    public void mocksFunction(Function<T, ?> action, Class<? extends Throwable> type) {
        Consumer<T> given = t -> action.apply(willThrow(type).given(t));
        Consumer<T> then  = t -> action.apply(then(t).should());
        add(given, then);
    }

    public void add(Consumer<T> given, Consumer<T> then) {
        givens.add(given);
        thens.add(then);
    }

    public void executeGivens(T t) {
        givens.forEach(given -> given.accept(t));
    }
    public void executeThens(T t) {
        thens.forEach(then -> then.accept(t));
    }

    @Override
    public String toString() {
        return message;
    }
}
