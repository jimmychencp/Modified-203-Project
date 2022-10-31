import processing.core.PImage;

import java.util.List;

public abstract class Dude extends Movable{
    private int resourceLimit;
    private int resourceCount;

    public Dude(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int animationPeriod, int actionPeriod) {
        super(id, position, images, animationPeriod, actionPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

    public Point nextPosition(
            WorldModel world, Point destPos) {
        /*int horiz = Integer.signum(destPos.x - this.getPosition().x);
        Point newPos = new Point(this.getPosition().x + horiz, this.getPosition().y);
        if (horiz == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).getClass() != Stump.class) {
            int vert = Integer.signum(destPos.y - this.getPosition().y);
            newPos = new Point(this.getPosition().x, this.getPosition().y + vert);
            if (vert == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).getClass() != Stump.class) {
                newPos = this.getPosition();
            }
        }
        return newPos; */

        PathingStrategy pathingStrategy = new AStarPathingStrategy();
        //PathingStrategy pathingStrategy = new SingleStepPathingStrategy();
        List<Point> pathing = pathingStrategy.computePath(
                this.getPosition(),
                destPos,
                pp -> (!world.isOccupied(pp) || (world.getOccupancyCell(pp).getClass() == Stump.class)),
                (p1,  p2) -> Functions.adjacent(p2, p1),
                PathingStrategy.CARDINAL_NEIGHBORS
        );

        return pathing.size() != 0 ? pathing.get(0) : getPosition();


    }


    public int getResourceLimit() {return this.resourceLimit;}

    public int getResourceCount() {return this.resourceCount;}

    public void addResourceCount() {this.resourceCount += 1;}


}
