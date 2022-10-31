import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Priest extends Movable{
    static boolean exist = false;

    public Priest(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod) {
        super(id, position, images, animationPeriod, actionPeriod);
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {
        Optional<Entity> cleanseTarget =
                Functions.findNearest(world, this.getPosition(), new ArrayList<>(Arrays.asList(EvilFairy.class, EvilFairyAttacking.class)));

        if (cleanseTarget.isPresent()) {
            if (this.moveTo(world, cleanseTarget.get(), scheduler)) {
                transformPriest(world, scheduler, imageStore, cleanseTarget);
            }
        }
        scheduler.scheduleEvent(this,
                Functions.createActivityAction(this, world, imageStore),
                this.getActionPeriod());
    }

    private void transformPriest(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore, Optional<Entity> cleansetarget) {
        PriestAttacking attackingpriest = Functions.createPriestAttacking(Functions.PRIESTATTACKING_KEY,
                this.getPosition(), Functions.PRIESTATTACKING_ACTION_PERIOD,
                Functions.PRIESTATTACKING_ANIMATION_PERIOD,
                imageStore.getImageList(Functions.PRIESTATTACKING_KEY), cleansetarget);

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(attackingpriest);
        attackingpriest.scheduleActions(scheduler, world, imageStore);
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

    public void setExist(boolean spawned) {exist = spawned;}
    public boolean getExist() {return exist;}

}
