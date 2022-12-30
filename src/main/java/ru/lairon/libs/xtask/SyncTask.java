package ru.lairon.libs.xtask;

import lombok.NonNull;

import java.util.function.Supplier;

public class SyncTask<T> extends Task<T> {

    private T obj;
    private boolean isNull = false;
    private TaskListener<T> taskListener;
    private Throwable throwable;

    public SyncTask(@NonNull Supplier<T> action) {
        try {
            obj = action.get();
        } catch (Exception e) {
            throwable = e;
            finish();
            return;
        }
        isNull = obj == null;
        finish();
    }

    @Override
    public void listen(TaskListener<T> taskListener) {
        this.taskListener = taskListener;
        finish();
    }

    private void finish() {
        if(taskListener == null) return;
        if (obj == null && isNull) {
            taskListener.action();
            return;
        }
        if (throwable != null) {
            taskListener.setThrowable(throwable);
            taskListener.action();
            return;
        }
        if (obj != null) {
            taskListener.finish(obj);
            taskListener.action();
        }
    }
}
