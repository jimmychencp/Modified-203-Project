import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeFull extends Dude{
    public DudeFull(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int animationPeriod,
            int actionPeriod)
    {
        super(id, position, images, resourceLimit, resourceCount, animationPeriod, actionPeriod);
    }

    private void transformFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore) {
        DudeNotFull dudeNotFull = Functions.createDudeNotFull(this.getId(),
                this.getPosition(), this.getActionPeriod(),
                this.getAnimationPeriod(),
                this.getResourceLimit(),
                this.getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(dudeNotFull);
        dudeNotFull.scheduleActions(scheduler, world, imageStore);
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {
        Optional<Entity> fullTarget =
                Functions.findNearest(world, this.getPosition(), new ArrayList<>(Arrays.asList(House.class)));

        if (fullTarget.isPresent() && moveTo(world,
                fullTarget.get(), scheduler)) {
            this.transformFull(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(this,
                    Functions.createActivityAction(this, world, imageStore),
                    this.getActionPeriod());
        }
    }

    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler) {
        if (Functions.adjacent(this.getPosition(), target.getPosition())) {
            return true;
        } else {
            return super.moveTo(world, target, scheduler);
         }
        }

}

