import processing.core.PImage;

import java.util.List;

public abstract class Animating extends Entity {
    private final int animationPeriod;

    public Animating(String id, Point position, List<PImage> images, int animationPeriod) {
        super(id, position, images);
        this.animationPeriod = animationPeriod;
    }

    public void nextImage() {this.setImageIndex((this.getImageIndex() + 1) % this.getImages().size());}
    public int getAnimationPeriod() {return this.animationPeriod;}

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore) {

        scheduler.scheduleEvent(this,
                Functions.createAnimationAction(this, 0),
                this.animationPeriod);
    }
}
