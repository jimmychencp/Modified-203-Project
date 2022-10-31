public class Activity implements Action{
    public ExecutingActivity executingactivity;
    public WorldModel world;
    public ImageStore imageStore;

    public Activity(ExecutingActivity executingactivity, WorldModel world, ImageStore imageStore) {
        this.executingactivity = executingactivity;
        this.world = world;
        this.imageStore = imageStore;
    }

    public void executeAction(EventScheduler scheduler) {
        executingactivity.executeActivity(world, imageStore, scheduler);
    }
}
