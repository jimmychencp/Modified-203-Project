import processing.core.PImage;

import java.util.List;

public class Sapling extends Compost{
    public Sapling(
            String id,
            Point position,
            List<PImage> images,
            int animationPeriod,
            int actionPeriod,
            int health,
            int healthLimit) {
       super(id, position, images, animationPeriod, actionPeriod, health, healthLimit);
    }

    private boolean transformSapling(
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
        } else if (this.getHealth() >= this.getHealth()) {
            Tree tree = Functions.createTree("tree_" + this.getId(),
                    this.getPosition(),
                    Functions.getNumFromRange(Functions.TREE_ACTION_MAX, Functions.TREE_ACTION_MIN),
                    Functions.getNumFromRange(Functions.TREE_ANIMATION_MAX, Functions.TREE_ANIMATION_MIN),
                    Functions.getNumFromRange(Functions.TREE_HEALTH_MAX, Functions.TREE_HEALTH_MIN),
                    imageStore.getImageList(Functions.TREE_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(tree);
            tree.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {
            this.gainHealth();
        if (!this.transformSapling(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this,
                    Functions.createActivityAction(this, world, imageStore),
                    this.getActionPeriod());
        }
    }

}
