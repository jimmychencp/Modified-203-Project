import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Optional;

import processing.core.*;

public final class VirtualWorld extends PApplet
{
    public static final int TIMER_ACTION_PERIOD = 100;

    public static final int VIEW_WIDTH = 640;
    public static final int VIEW_HEIGHT = 480;
    public static final int TILE_WIDTH = 32;
    public static final int TILE_HEIGHT = 32;
    public static final int WORLD_WIDTH_SCALE = 2;
    public static final int WORLD_HEIGHT_SCALE = 2;

    public static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    public static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
    public static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
    public static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

    public static final String IMAGE_LIST_FILE_NAME = "imagelist";
    public static final String DEFAULT_IMAGE_NAME = "background_default";
    public static final int DEFAULT_IMAGE_COLOR = 0x808080;

    public static String LOAD_FILE_NAME = "world.sav";

    public static final String FAST_FLAG = "-fast";
    public static final String FASTER_FLAG = "-faster";
    public static final String FASTEST_FLAG = "-fastest";
    public static final double FAST_SCALE = 0.5;
    public static final double FASTER_SCALE = 0.25;
    public static final double FASTEST_SCALE = 0.10;

    public static double timeScale = 1.0;

    public ImageStore imageStore;
    public WorldModel world;
    public WorldView view;
    public EventScheduler scheduler;

    public long nextTime;

    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        this.imageStore = new ImageStore(
                createImageColored(TILE_WIDTH, TILE_HEIGHT,
                                   DEFAULT_IMAGE_COLOR));
        this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
                                    createDefaultBackground(imageStore));
        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world, TILE_WIDTH,
                                  TILE_HEIGHT);
        this.scheduler = new EventScheduler(timeScale);

        loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
        loadWorld(world, LOAD_FILE_NAME, imageStore);

        scheduleActions(world, scheduler, imageStore);

        nextTime = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
    }

    public void draw() {
        long time = System.currentTimeMillis();
        if (time >= nextTime) {
            scheduler.updateOnTime(time);
            nextTime = time + TIMER_ACTION_PERIOD;
        }

        view.drawViewport();
    }

    // Just for debugging and for P5
    // This should be refactored as appropriate
    public void mousePressed() {
        Point pressed = mouseToPoint(mouseX, mouseY);
        System.out.println("CLICK! " + pressed.x + ", " + pressed.y);

        Point spawn_point = new Point(Functions.PRIEST_SPAWN_X, Functions.PRIEST_SPAWN_Y);

        if (!world.isOccupied(spawn_point)) {
            Priest priest = Functions.createPriest(
                    Functions.PRIEST_KEY,
                    spawn_point,
                    Functions.PRIEST_ACTION_PERIOD,
                    Functions.PRIEST_ANIMATION_PERIOD,
                    imageStore.getImageList(Functions.PRIEST_KEY));
            if (!priest.getExist()) {
                world.tryAddEntity(priest);
                priest.scheduleActions(scheduler, world, imageStore);
                priest.setExist(true);
            }

        }


        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                Point new_background = new Point(pressed.x + i, pressed.y + j);
                world.setBackground(new_background, new Background(Functions.PORTAL_KEY, imageStore.getImageList(Functions.PORTAL_KEY)));
                Optional<Entity> entityOptional = world.getOccupant(new_background);
                if (entityOptional.isPresent())
                {
                    Entity entity = entityOptional.get();
                    if (entity.getClass() == Fairy.class){
                        world.removeEntity(entity);
                        scheduler.unscheduleAllEvents(entity);
                        EvilFairy evilfairy = Functions.createEvilFairy(
                                Functions.EVILFAIRY_KEY,
                                new_background,
                                Functions.EVILFAIRY_ACTION_PERIOD,
                                Functions.EVILFAIRY_ANIMATION_PERIOD,
                                imageStore.getImageList(Functions.EVILFAIRY_KEY));
                        world.tryAddEntity(evilfairy);
                        evilfairy.scheduleActions(scheduler, world, imageStore);
                    }
                }
            }
        }

    }

    private Point mouseToPoint(int x, int y)
    {
        return view.viewport.viewportToWorld(mouseX/TILE_WIDTH, mouseY/TILE_HEIGHT);
    }
    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP:
                    dy = -1;
                    break;
                case DOWN:
                    dy = 1;
                    break;
                case LEFT:
                    dx = -1;
                    break;
                case RIGHT:
                    dx = 1;
                    break;
            }
            view.shiftView(dx, dy);
        }
    }

    public static Background createDefaultBackground(ImageStore imageStore) {
        return new Background(DEFAULT_IMAGE_NAME,
                              imageStore.getImageList(DEFAULT_IMAGE_NAME));
    }

    public static PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            img.pixels[i] = color;
        }
        img.updatePixels();
        return img;
    }

    static void loadImages(
            String filename, ImageStore imageStore, PApplet screen)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            Functions.loadImages(in, imageStore, screen);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void loadWorld(
            WorldModel world, String filename, ImageStore imageStore)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            Functions.load(in, world, imageStore);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void scheduleActions(
            WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        for (Entity entity : world.entities) {
            if (entity instanceof Animating) {
                ((Animating) entity).scheduleActions(scheduler, world, imageStore);
            }
        }
    }

    public static void parseCommandLine(String[] args) {
        if (args.length > 1)
        {
            if (args[0].equals("file"))
            {

            }
        }
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG:
                    timeScale = Math.min(FAST_SCALE, timeScale);
                    break;
                case FASTER_FLAG:
                    timeScale = Math.min(FASTER_SCALE, timeScale);
                    break;
                case FASTEST_FLAG:
                    timeScale = Math.min(FASTEST_SCALE, timeScale);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        parseCommandLine(args);
        PApplet.main(VirtualWorld.class);
    }
}
