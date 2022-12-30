package ru.lairon.libs.xtask;

public abstract class Task<T> {

    public abstract void listen(TaskListener<T> taskListener);

}
