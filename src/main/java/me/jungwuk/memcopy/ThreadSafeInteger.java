package me.jungwuk.memcopy;

import lombok.Getter;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadSafeInteger {
    private final Lock lock = new ReentrantLock();
    @Getter
    private final Condition condition = lock.newCondition();
    private int value;

    public ThreadSafeInteger(int value) {
        this.value = value;
    }

    public int getValue() {
        lock.lock();
        int ret = value;
        lock.unlock();

        return ret;
    }

    public void increment() {
        lock.lock();
        value++;
        condition.signal();
        lock.unlock();
    }

    public void decrement() {
        lock.lock();
        value--;
        condition.signal();
        lock.unlock();
    }

    public void awaitValue(int value) throws InterruptedException {
        lock.lock();
        while (getValue() != value) {
            condition.await();
        }
        lock.unlock();
    }
}
