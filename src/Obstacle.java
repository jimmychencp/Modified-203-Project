import processing.core.PImage;

import java.util.List;

public class Obstacle extends Animating{

    public Obstacle(
            String id,
            Point position,
            List<PImage> images,
            int animationPeriod) {
        super(id, position, images, animationPeriod);
    }

}
