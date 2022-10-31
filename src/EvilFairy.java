import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class EvilFairy extends Fairy{

    public EvilFairy(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod) {
        super(id, position, images, animationPeriod, actionPeriod);
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {
        Optional<Entity> evilTarget =
                Functions.findNearest(world, this.getPosition(), new ArrayList<>(Arrays.asList(DudeNotFull.class, DudeFull.class)));

        if (evilTarget.isPresent()) {
            if (this.moveTo(world, evilTarget.get(), scheduler)) {
                transformEvilFairy(world, scheduler, imageStore, evilTarget);
            }
        }
        scheduler.scheduleEvent(this,
                Functions.createActivityAction(this, world, imageStore),
                this.getActionPeriod());
    }

    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler) {
        if (Functions.adjacent(this.getPosition(), target.getPosition())) {
            return true;
        }
        else {
            return super.moveTo(world, target, scheduler);
        }
    }

    private void transformEvilFairy(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore,
            Optional<Entity> target) {
        EvilFairyAttacking attackingfairy = Functions.createEvilFairyAttacking(Functions.EVILFAIRYATTACKING_KEY,
                this.getPosition(), Functions.EVILFAIRYATTACKING_ACTION_PERIOD,
                Functions.EVILFAIRYATTACKING_ANIMATION_PERIOD,
                imageStore.getImageList(Functions.EVILFAIRYATTACKING_KEY),
                target);

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(attackingfairy);
        attackingfairy.scheduleActions(scheduler, world, imageStore);
    }

}
