import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class Movable extends ExecutingActivity {

    public Movable(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod) {
        super(id, position, images, animationPeriod, actionPeriod);
    }

    public Point nextPosition(
            WorldModel world, Point destPos) {
        /* int horiz = Integer.signum(destPos.x - this.getPosition().x);
        Point newPos = new Point(this.getPosition().x + horiz, this.getPosition().y);
        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.y - this.getPosition().y);
            newPos = new Point(this.getPosition().x, this.getPosition().y + vert);
            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = this.getPosition();
            }
        }
        return newPos;
         */

        PathingStrategy pathingStrategy = new AStarPathingStrategy();
        //PathingStrategy pathingStrategy = new SingleStepPathingStrategy();
        List<Point> pathing = pathingStrategy.computePath(
                this.getPosition(),
                destPos,
                pp -> (!world.isOccupied(pp) && world.withinBounds(pp)),
                (p1,  p2) -> Functions.adjacent(p2, p1),
                PathingStrategy.CARDINAL_NEIGHBORS
        );

        return pathing.size() != 0 ? pathing.get(0) : getPosition();
    }

    public boolean moveTo(WorldModel world,
                          Entity target,
                          EventScheduler scheduler) {
        Point nextPos = nextPosition(world, target.getPosition());

        if (!this.getPosition().equals(nextPos)) {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent()) {
                scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(this, nextPos);
        }
        return false;
    }
}
