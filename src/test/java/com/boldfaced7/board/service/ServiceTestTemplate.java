package com.boldfaced7.board.service;

import com.boldfaced7.board.Context;

import java.util.function.Supplier;

public class ServiceTestTemplate {

    public static <T> void doTest(Runnable targetMethod, Context<T, ?> context, T mockedElement) {
        context.executeGivens(mockedElement);
        try {
            targetMethod.run();
            context.doAssert();
        } catch (Throwable t) {
            context.doAssert(t);
        }
        context.executeThens(mockedElement);
    }
    public static <T, R> void doTest(Supplier<R> targetMethod, Context<T, R> context, T mockedElement) {
        context.executeGivens(mockedElement);
        try {
            R target = targetMethod.get();
            context.doAssert(target);
            context.doAssert();
        } catch (Throwable t) {
            context.doAssert(t);
        }
        context.executeThens(mockedElement);
    }
}