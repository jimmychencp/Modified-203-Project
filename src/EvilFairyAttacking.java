import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class EvilFairyAttacking extends Fairy{

    private boolean first_run;
    private Optional<Entity> target;

    public EvilFairyAttacking(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod, Optional<Entity> target) {
        super(id, position, images, animationPeriod, actionPeriod);
        this.first_run = true;
        this.target = target;
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {

        if (!first_run) {
            transformEvilFairyAttacking(world, scheduler, imageStore);
        }

        if (target.isPresent() && first_run) {
            world.removeEntity(target.get());
            scheduler.unscheduleAllEvents(target.get());

            }
        first_run = false;
        scheduler.scheduleEvent(this,
                Functions.createActivityAction(this, world, imageStore),
                this.getActionPeriod());
    }

    private void transformEvilFairyAttacking(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore) {
        EvilFairy attackingfairy = Functions.createEvilFairy(Functions.EVILFAIRY_KEY,
                this.getPosition(), Functions.EVILFAIRY_ACTION_PERIOD,
                Functions.EVILFAIRY_ANIMATION_PERIOD,
                imageStore.getImageList(Functions.EVILFAIRY_KEY));

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(attackingfairy);
        attackingfairy.scheduleActions(scheduler, world, imageStore);
    }


}
