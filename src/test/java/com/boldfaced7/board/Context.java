package com.boldfaced7.board;

import com.boldfaced7.board.GivenAndThen;
import lombok.Getter;
import org.assertj.core.util.TriFunction;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

@Getter
public class Context<T> {
    Function<T, GivenAndThen> constructor;

    public <P> Context(BiConsumer<T, P> method, P param) {
        constructor = (repo -> new GivenAndThen(repo, method, param));
    }
    public <P, E extends Exception> Context(BiConsumer<T, P> method, P param, E exception) {
        constructor = (repo -> new GivenAndThen(repo, method, param, exception));
    }

    public <R> Context(Function<T, R> method, R result) {
        constructor = (repo -> new GivenAndThen(repo, method, result));
    }

    public <R, E extends Exception> Context(Function<T, R> method, E exception) {
        constructor = (repo -> new GivenAndThen(repo, method, exception));
    }

    public <P, R> Context(BiFunction<T, P, R> method, P param, R result) {
        constructor = (repo -> new GivenAndThen(repo, method, param, result));
    }

    public <P, R, E extends Exception> Context(BiFunction<T, P, R> method, P param, E exception) {
        constructor = (repo -> new GivenAndThen(repo, method, param, exception));
    }

    /////
    public <P, S> Context(BiConsumer<S, P> method, P param, Function<T, S> holder) {
        constructor = (repo -> new GivenAndThen(repo, method, param, holder));
    }

    public <P, S, E extends Exception> Context(BiConsumer<S, P> method, P param, E exception, Function<T, S> holder) {
        constructor = (repo -> new GivenAndThen(repo, method, param, exception, holder));
    }

    public <R, S> Context(Function<S, R> method, R result, Function<T, S> holder) {
        constructor = (repo -> new GivenAndThen(repo, method, result, holder));
    }

    public <R, S, E extends Exception> Context(Function<S, R> method, E exception, Function<T, S> holder) {
        constructor = (repo -> new GivenAndThen(repo, method, exception, holder));
    }

    public <P, R, S> Context(BiFunction<S, P, R> method, P param, R result, Function<T, S> holder) {
        constructor = (repo -> new GivenAndThen(repo, method, param, result, holder));
    }

    public <P, R, S, E extends Exception> Context(BiFunction<S, P, R> method, P param, E exception, Function<T, S> holder) {
        constructor = (repo -> new GivenAndThen(repo, method, param, exception, holder));
    }

    public <P1, P2, R, S, E extends Exception> Context(TriFunction<S, P1, P2, R> method, P1 param1, P2 param2, R result, Function<T, S> holder) {
        constructor = (repo -> new GivenAndThen(repo, method, param1, param2, result, holder));
    }

    public <P1, P2, R, S, E extends Exception> Context(TriFunction<S, P1, P2, R> method, P1 param1, P2 param2, E exception, Function<T, S> holder) {
        constructor = (repo -> new GivenAndThen(repo, method, param1, param2, exception, holder));
    }
    /////

    public GivenAndThen convert(T entity) {
        return constructor.apply(entity);
    }
}
