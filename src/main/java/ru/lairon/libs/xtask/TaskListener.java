package ru.lairon.libs.xtask;

public abstract class TaskListener<T> {

    private T obj;
    private Throwable throwable;

    public T get() throws Throwable{
        if(throwable != null) throw throwable;
        return obj;
    }

    public abstract void action();

    protected void finish(T obj) {
        this.obj = obj;
    }

    protected void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
