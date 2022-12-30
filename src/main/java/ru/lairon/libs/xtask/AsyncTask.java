package ru.lairon.libs.xtask;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.function.Supplier;

public class AsyncTask<T> extends Task<T> {

    @NonNull
    private Plugin plugin;
    private T obj;
    private boolean isNull = false;
    private TaskListener<T> taskListener;
    private Throwable throwable;

    public AsyncTask(@NonNull Plugin plugin, @NonNull Supplier<T> action) {
        this.plugin = plugin;
        if (plugin.isEnabled()) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                try {
                    obj = action.get();
                } catch (Exception e) {
                    throwable = e;
                    finish();
                    return;
                }
                isNull = obj == null;
                finish();
            });
        }
    }

    @Override
    public void listen(TaskListener<T> taskListener) {
        this.taskListener = taskListener;
        finish();
    }

    private void finish() {
        if (obj == null && isNull) {
            Bukkit.getScheduler().runTask(plugin, () -> taskListener.action());
            return;
        }
        if (throwable != null) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                taskListener.setThrowable(throwable);
                taskListener.action();
            });
            return;
        }
        if (obj != null) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                taskListener.finish(obj);
                taskListener.action();
            });
        }
    }
}
