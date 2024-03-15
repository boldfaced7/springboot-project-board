package com.boldfaced7.board.service;

import com.boldfaced7.board.Assertion;
import com.boldfaced7.board.Context;
import com.boldfaced7.board.GivenAndThen;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@AllArgsConstructor
public class ServiceTestTemplate {
    DependencyHolder dependencyHolder;

    <R> void performRequest(List<Context<DependencyHolder>> contexts, Supplier<R> targetMethod, List<Assertion<R>> assertions) {
        List<GivenAndThen> givenAndThens = contexts != null ? contexts.stream().map(c -> c.convert(dependencyHolder)).toList() : List.of();
        List<Runnable> assertThat;

        if (assertions == null || assertions.isEmpty()) {
            assertThat = List.of(targetMethod::get);
        } else {
            assertThat = assertions.stream().map(a -> a.convert(targetMethod)).toList();
        }

        doTest(givenAndThens, assertThat);
    }

    <P, R> void performRequest(List<Context<DependencyHolder>> contexts, Function<P, R> targetMethod, P request, List<Assertion<R>> assertions) {
        List<GivenAndThen> givenAndThens = contexts != null ? contexts.stream().map(c -> c.convert(dependencyHolder)).toList() : List.of();
        List<Runnable> assertThat;

        if (assertions == null || assertions.isEmpty()) {
            assertThat = List.of(() -> targetMethod.apply(request));
        } else {
            assertThat = assertions.stream().map(a -> a.convert(targetMethod, request)).toList();
        }

        doTest(givenAndThens, assertThat);
    }

    <P, R> void performRequest(List<Context<DependencyHolder>> contexts, Consumer<P> targetMethod, P request, List<Assertion<R>> assertions, R validationObject) {
        List<GivenAndThen> givenAndThens = contexts != null ? contexts.stream().map(c -> c.convert(dependencyHolder)).toList() : List.of();
        List<Runnable> assertThat;

        if (assertions == null || assertions.isEmpty()) {
            assertThat = List.of(() -> targetMethod.accept(request));
        } else {
            assertThat = assertions.stream().map(a -> a.convert(targetMethod, request, validationObject)).toList();
        }

        doTest(givenAndThens, assertThat);
    }

    private static void doTest(List<GivenAndThen> givenAndThens, List<Runnable> assertThat) {
        // Given
        givenAndThens.forEach(g -> g.getGiven().run());

        // When & Then
        assertThat.forEach(Runnable::run);
        givenAndThens.forEach(g -> g.getThen().run());
    }
}
