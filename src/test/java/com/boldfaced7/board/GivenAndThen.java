package com.boldfaced7.board;

import lombok.*;
import org.assertj.core.util.TriFunction;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.mockito.BDDMockito.*;
import static org.mockito.BDDMockito.then;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GivenAndThen {
    private Runnable given;
    private Runnable then;

    public <P, T> GivenAndThen(T entity, BiConsumer<T, P> method, P param) {
        given = () -> method.accept(willDoNothing().given(entity), param);
        then = () -> method.accept(then(entity).should(), param);
    }

    public <P, T, E extends Exception> GivenAndThen(T entity, BiConsumer<T, P> method, P param, E exception) {
        given = () -> method.accept(willThrow(exception).given(entity), param);
        then = () -> method.accept(then(entity).should(), param);
    }

    public <R, T> GivenAndThen(T entity, Function<T, R> method, R result) {
        given = () -> given(method.apply(entity)).willReturn(result);
        then = () -> method.apply(then(entity).should());
    }

    public <R, T, E extends Exception> GivenAndThen(T entity, Function<T, R> method, E exception) {
        given = () -> method.apply(willThrow(exception).given(entity));
        then = () -> method.apply(then(entity).should());
    }

    public <P, R, T> GivenAndThen(T entity, BiFunction<T, P, R> method, P param, R result) {
        given = () -> given(method.apply(entity, param)).willReturn(result);
        then = () -> method.apply(then(entity).should(), param);
    }

    public <P, R, T, E extends Exception> GivenAndThen(T entity, BiFunction<T, P, R> method, P param, E exception) {
        given = () -> method.apply(willThrow(exception).given(entity), param);
        then = () -> method.apply(then(entity).should(), param);
    }

    public <P, S, T> GivenAndThen(T entity, BiConsumer<S, P> method, P param, Function<T, S> holder) {
        S covered = holder.apply(entity);
        given = () -> method.accept(willDoNothing().given(covered), param);
        then = () -> method.accept(then(covered).should(), param);
    }

    public <P, S, T, E extends Exception> GivenAndThen(T entity, BiConsumer<S, P> method, P param, E exception, Function<T, S> holder) {
        S covered = holder.apply(entity);
        given = () -> method.accept(willThrow(exception).given(covered), param);
        then = () -> method.accept(then(covered).should(), param);
    }

    public <R, S, T> GivenAndThen(T entity, Function<S, R> method, R result, Function<T, S> holder) {
        S covered = holder.apply(entity);
        given = () -> given(method.apply(covered)).willReturn(result);
        then = () -> method.apply(then(covered).should());
    }

    public <E extends Exception, R, S, T> GivenAndThen(T entity, Function<S, R> method, E exception, Function<T, S> holder) {
        S covered = holder.apply(entity);
        given = () -> method.apply(willThrow(exception).given(covered));
        then = () -> method.apply(then(covered).should());
    }

    public <P, R, S, T> GivenAndThen(T entity, BiFunction<S, P, R> method, P param, R result, Function<T, S> holder) {
        S covered = holder.apply(entity);
        given = () -> given(method.apply(covered, param)).willReturn(result);
        then = () -> method.apply(then(covered).should(), param);
    }

    public <P, R, S, T, E extends Exception> GivenAndThen(T entity, BiFunction<S, P, R> method, P param, E exception, Function<T, S> holder) {
        S covered = holder.apply(entity);
        given = () -> method.apply(willThrow(exception).given(covered), param);
        then = () -> method.apply(then(covered).should(), param);
    }

    public <P1, P2, R, S, T> GivenAndThen(T entity, TriFunction<S, P1, P2, R> method, P1 param1, P2 param2, R result, Function<T, S> holder) {
        S covered = holder.apply(entity);
        given = () -> given(method.apply(covered, param1, param2)).willReturn(result);
        then = () -> method.apply(then(covered).should(), param1, param2);
    }

    public <P1, P2, R, S, T, E extends Exception> GivenAndThen(T entity, TriFunction<S, P1, P2, R> method, P1 param1, P2 param2, E exception, Function<T, S> holder) {
        S covered = holder.apply(entity);
        given = () -> method.apply(willThrow(exception).given(covered), param1, param2);
        then = () -> method.apply(then(covered).should(), param1, param2);
    }

    public <R, T, U> Function<T, R> cover(Function<U, R> target, Function<T, U> holder) {
        return t -> target.apply(holder.apply(t));
    }

    public <P, R, T, U> BiFunction<T, P, R> cover(BiFunction<U, P, R> target, Function<T, U> holder) {
        return (t, param) -> target.apply(holder.apply(t), param);
    }

    public <P, T, U> BiConsumer<T, P> cover(BiConsumer<U, P> target, Function<T, U> holder) {
        return (t, param) -> target.accept(holder.apply(t), param);
    }
}