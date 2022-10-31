import java.util.*;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public abstract class Entity{
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;

    public Entity(String id,
                  Point position,
                  List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }

    public Point getPosition() {return this.position;}
    public String getId() {return this.id;}
    public void setPosition(Point p) {position = p;}
    public PImage getCurrentImage() {return images.get(imageIndex);}
    public List<PImage> getImages() {return images;}
    public int getImageIndex() {return imageIndex;}
    public void setImageIndex(int index) {imageIndex = index;}
}

