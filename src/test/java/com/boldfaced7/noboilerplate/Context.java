package com.boldfaced7.noboilerplate;

import java.util.function.Consumer;
import java.util.function.Function;

public class Context<M, A> {
    private final Mocker<M> mocker = new Mocker<>();
    private final Assertion<A> assertion = new Assertion<>();
    private final String message;

    public Context(String message) {
        this.message = message;
    }

    public <N> void mocks(Function<M, N> facade, Consumer<N> action) {
        mocker.mocks(facade, action);
    }
    public <N, O> void mocks(Function<M, N> facade, Function<N, O> action, O result) {
        mocker.mocks(facade, action, result);
    }
    public <N> void mocks(Function<M, N> facade, Consumer<N> action, Class<? extends Throwable> type) {
        mocker.mocks(facade, action, type);
    }
    public <N> void mocks(Function<M, N> facade, Function<N, ?> action, Class<? extends Throwable> type) {
        mocker.mocks(facade, action, type);
    }

    public void executeGivens(M mockedElement) {
        mocker.executeGivens(mockedElement);
    }
    public void executeThens(M mockedElement) {
        mocker.executeThens(mockedElement);
    }

    public void asserts(Runnable assertThat) {
        assertion.add(assertThat);
    }
    public void asserts(Consumer<A> assertThat) {
        assertion.add(assertThat);
    }
    public void assertsThrowable(Consumer<Throwable> assertThat) {
        assertion.addThrowable(assertThat);
    }

    public void doAssert() {
        assertion.execute();
    }
    public void doAssert(A target) {
        assertion.execute(target);
    }
    public void doAssert(Throwable throwable) {
        assertion.execute(throwable);
    }

    @Override
    public String toString() {
        return message;
    }
}
