import processing.core.PImage;

import java.util.List;

public abstract class ExecutingActivity extends Animating{
    private int actionPeriod;

    public ExecutingActivity(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod) {
        super(id, position, images, animationPeriod);
        this.actionPeriod = actionPeriod;
    }

    abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                Functions.createActivityAction(this, world, imageStore),
                this.actionPeriod);
        super.scheduleActions(scheduler, world, imageStore);
    }

    public int getActionPeriod() {return this.actionPeriod;}
}
