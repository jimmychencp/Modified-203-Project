import processing.core.PImage;

import java.util.List;

public abstract class Compost extends ExecutingActivity {
    private int health;
    private int healthLimit;

    public Compost( String id,
                    Point position,
                    List<PImage> images,
                    int actionPeriod,
                    int animationPeriod,
                    int health,
                    int healthLimit)
    {
        super(id, position, images, animationPeriod, actionPeriod);
        this.health = health;
        this.healthLimit = healthLimit;
    }

    public void loseHealth() {this.health--;}
    public int getHealth() {return this.health;}
    public void gainHealth() {this.health++;}

}
