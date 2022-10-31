import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PriestAttacking extends Movable{

    private boolean first_run;
    private Optional<Entity> target;

    public PriestAttacking(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod, Optional<Entity> target) {
        super(id, position, images, animationPeriod, actionPeriod);
        this.first_run = true;
        this.target = target;
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {

        if (!first_run) {
            transformPriestAttacking(world, scheduler, imageStore);
            //world.removeEntity(this);
            //scheduler.unscheduleAllEvents(this);
        }

        if (target.isPresent() && first_run) {
            Point tgtPos = target.get().getPosition();
            world.removeEntity(target.get());
            scheduler.unscheduleAllEvents(target.get());
            Fairy fairy = Functions.createFairy("fairy_" + this.getId(),
                    tgtPos,
                    Functions.FAIRY_ACTION_PERIOD,
                    Functions.FAIRY_ANIMATION_PERIOD,
                    imageStore.getImageList(Functions.FAIRY_KEY));

            world.addEntity(fairy);
            fairy.scheduleActions(scheduler, world, imageStore);
        }
        first_run = false;

        scheduler.scheduleEvent(this,
                Functions.createActivityAction(this, world, imageStore),
                this.getActionPeriod());
    }

    private void transformPriestAttacking(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore) {
        Priest priest = Functions.createPriest(Functions.PRIEST_KEY,
                this.getPosition(), Functions.PRIEST_ACTION_PERIOD,
                Functions.PRIEST_ANIMATION_PERIOD,
                imageStore.getImageList(Functions.PRIEST_KEY));

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(priest);
        priest.scheduleActions(scheduler, world, imageStore);
    }
}
