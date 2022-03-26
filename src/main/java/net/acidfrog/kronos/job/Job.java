package net.acidfrog.kronos.job;

import net.acidfrog.kronos.core.lang.logger.Logger;
import net.acidfrog.kronos.job.tasks.Task;

public class Job implements Runnable {

    private final String name;
    private final JobPriority priority;
    private final Task task;

    public Job(String name, JobPriority priority, Task task) {
        this.name = name;
        this.priority = priority;
        this.task = task;
    }

    @Override
    public void run() {
        try {
            task.perform();
        } catch (Exception e) {
            Logger.instance.logWarn("Task not performed: " + name);
        }
    }

    public String getName() {
        return name;
    }

    public JobPriority getPriority() {
        return priority;
    }

    public Task getTask() {
        return task;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Job [name=");
        sb.append(name);
        sb.append(", priority=");
        sb.append(priority);
        sb.append("]");
        return sb.toString();
    }
    
}