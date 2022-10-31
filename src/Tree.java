import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Tree extends Compost{
    public Tree(
            String id,
            Point position,
            List<PImage> images,
            int animationPeriod,
            int actionPeriod,
            int health,
            int healthLimit) {
        super(id, position, images, animationPeriod, actionPeriod, health, healthLimit);
    }

    private boolean transformTree(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore) {
        if (this.getHealth() <= 0) {
            Stump stump = Functions.createStump(this.getId(),
                    this.getPosition(),
                    imageStore.getImageList(Functions.STUMP_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(stump);
            return true;
        }

        return false;
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {

        if (!this.transformTree(world, scheduler, imageStore)) {

            scheduler.scheduleEvent(this,
                    Functions.createActivityAction(this, world, imageStore),
                    this.getActionPeriod());
        }
    }

}
