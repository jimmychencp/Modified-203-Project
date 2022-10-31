import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeNotFull extends Dude {
    public DudeNotFull(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int animationPeriod,
            int actionPeriod) {
            super(id, position, images, resourceLimit, resourceCount, animationPeriod, actionPeriod);
    }

    private boolean transformNotFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore) {
        if (this.getResourceCount() >= this.getResourceLimit()) {
            DudeFull dudeFull = Functions.createDudeFull(this.getId(),
                    this.getPosition(), this.getActionPeriod(),
                    this.getAnimationPeriod(),
                    this.getResourceLimit(),
                    this.getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(dudeFull);
            dudeFull.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {
        Optional<Entity> target =
                Functions.findNearest(world, this.getPosition(), new ArrayList<>(Arrays.asList(Tree.class, Sapling.class)));

        if (!target.isPresent() || !moveTo(world,
                (Compost) target.get(),
                scheduler)
                || !this.transformNotFull(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this,
                    Functions.createActivityAction(this, world, imageStore),
                    this.getActionPeriod());
        }
    }

    public boolean moveTo(
            WorldModel world,
            Compost target,
            EventScheduler scheduler) {
        if (Functions.adjacent(this.getPosition(), target.getPosition())) {
            this.addResourceCount();
            target.loseHealth();
            return true;
        } else {
                return super.moveTo(world, target, scheduler);
            }

        }

}

