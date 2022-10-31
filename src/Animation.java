
public class Animation implements Action{
    private Animating animating;
    private int repeatCount;

    public Animation(Animating animating, int repeatCount) {
        this.animating = animating;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler) {
        animating.nextImage();
        if (repeatCount != 1) {
            scheduler.scheduleEvent(animating,
                    Functions.createAnimationAction(animating,
                            Math.max(repeatCount - 1,
                                    0)),
                    animating.getAnimationPeriod());
        }
    }
}
