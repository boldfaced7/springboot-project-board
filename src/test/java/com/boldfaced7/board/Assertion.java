package com.boldfaced7.board;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Assertion<T> {
    private final List<Runnable> runnableAssertions = new ArrayList<>();
    private final List<Consumer<T>> consumerAssertions = new ArrayList<>();
    private final List<Consumer<Throwable>> exceptionAssertions = new ArrayList<>();
    private String message;

    public Assertion() {}
    public Assertion(String message) {
        this.message = message;
    }

    public void add(Runnable assertion) {
        runnableAssertions.add(assertion);
    }
    public void add(Consumer<T> assertion) {
        consumerAssertions.add(assertion);
    }
    public void addThrowable(Consumer<Throwable> assertion) {
        exceptionAssertions.add(assertion);
    }

    public void execute() {
        runnableAssertions.forEach(Runnable::run);
    }
    public void execute(T target) {
        consumerAssertions.forEach(a -> a.accept(target));
    }
    public void execute(Throwable t) {
        exceptionAssertions.forEach(a -> a.accept(t));
    }

    @Override
    public String toString() {
        return message;
    }
}
