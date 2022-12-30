package ru.lairon.libs.xtask;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

// Author: Dymeth.Ru
public final class TasksQueue {
    private final List<Runnable> actions = new ArrayList<>();
    private int actionIndex = 0;
    private long sleepTicks = 0;
    private BukkitTask task = null;

    public TasksQueue action(Runnable runnable) {
        if (this.isRunning()) {
            throw new IllegalStateException("Unable to add actions to running " + this.getClass().getSimpleName());
        }
        this.actions.add(runnable);
        return this;
    }

    /**
     * @param ticks Время ожидания в тиках после завершения предыдущей задачи
     */
    public TasksQueue sleepTicks(long ticks) {
        if (ticks <= 0) {
            throw new IllegalArgumentException("Ticks must be > 0");
        }
        return this.action(() -> this.sleepTicks = ticks);
    }

    /**
     * @param duration Значение продолжительности
     * @param timeUnit Единицы измерения
     */
    public TasksQueue sleep(long duration, TimeUnit timeUnit) {
        return this.sleepTicks(TimeUnit.MILLISECONDS.convert(duration, timeUnit) / 50);
    }

    public boolean isRunning() {
        return this.task != null;
    }

    public void startAsync(Plugin plugin) {
        this.start(plugin, true);
    }

    public void start(Plugin plugin) {
        this.start(plugin, false);
    }

    private void start(Plugin plugin, boolean async) {
        if (this.isRunning()) {
            throw new IllegalStateException(this.getClass().getSimpleName() + " started running");
        }
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        this.task = async
                ? scheduler.runTaskTimerAsynchronously(plugin, this::run, 1L, 1L)
                : scheduler.runTaskTimer(plugin, this::run, 1L, 1L);
    }

    public void stop() {
        if (!this.isRunning()) return;
        this.task.cancel();
        this.actionIndex = 0;
        this.sleepTicks = 0;
        this.task = null;
    }

    private void run() {
        while (this.sleepTicks == 0) {
            this.actions.get(this.actionIndex++).run();
            if (this.actionIndex == this.actions.size()) {
                this.stop();
                return;
            }
        }
        this.sleepTicks--;
    }
}