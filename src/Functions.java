import java.util.*;

import processing.core.PImage;
import processing.core.PApplet;

/**
 * This class contains many functions written in a procedural style.
 * You will reduce the size of this class over the next several weeks
 * by refactoring this codebase to follow an OOP style.
 */
public final class Functions {
    public static final Random rand = new Random();

    public static final int COLOR_MASK = 0xffffff;
    public static final int KEYED_IMAGE_MIN = 5;
    private static final int KEYED_RED_IDX = 2;
    private static final int KEYED_GREEN_IDX = 3;
    private static final int KEYED_BLUE_IDX = 4;

    public static final int PROPERTY_KEY = 0;

    public static final List<String> PATH_KEYS = new ArrayList<>(Arrays.asList("bridge", "dirt", "dirt_horiz", "dirt_vert_left", "dirt_vert_right",
            "dirt_bot_left_corner", "dirt_bot_right_up", "dirt_vert_left_bot"));

    public static final String SAPLING_KEY = "sapling";
    public static final int SAPLING_HEALTH_LIMIT = 5;
    public static final int SAPLING_ACTION_ANIMATION_PERIOD = 1000; // have to be in sync since grows and gains health at same time
    public static final int SAPLING_NUM_PROPERTIES = 4;
    public static final int SAPLING_ID = 1;
    public static final int SAPLING_COL = 2;
    public static final int SAPLING_ROW = 3;
    public static final int SAPLING_HEALTH = 4;

    public static final String BGND_KEY = "background";
    public static final int BGND_NUM_PROPERTIES = 4;
    public static final int BGND_ID = 1;
    public static final int BGND_COL = 2;
    public static final int BGND_ROW = 3;

    public static final String PORTAL_KEY = "portal";

    public static final String OBSTACLE_KEY = "obstacle";
    public static final int OBSTACLE_NUM_PROPERTIES = 5;
    public static final int OBSTACLE_ID = 1;
    public static final int OBSTACLE_COL = 2;
    public static final int OBSTACLE_ROW = 3;
    public static final int OBSTACLE_ANIMATION_PERIOD = 4;

    public static final String DUDE_KEY = "dude";
    public static final int DUDE_NUM_PROPERTIES = 7;
    public static final int DUDE_ID = 1;
    public static final int DUDE_COL = 2;
    public static final int DUDE_ROW = 3;
    public static final int DUDE_LIMIT = 4;
    public static final int DUDE_ACTION_PERIOD = 5;
    public static final int DUDE_ANIMATION_PERIOD = 6;

    public static final String HOUSE_KEY = "house";
    public static final int HOUSE_NUM_PROPERTIES = 4;
    public static final int HOUSE_ID = 1;
    public static final int HOUSE_COL = 2;
    public static final int HOUSE_ROW = 3;

    public static final String FAIRY_KEY = "fairy";
    public static final int FAIRY_NUM_PROPERTIES = 6;
    public static final int FAIRY_ID = 1;
    public static final int FAIRY_COL = 2;
    public static final int FAIRY_ROW = 3;
    public static final int FAIRY_ANIMATION_PERIOD = 4;
    public static final int FAIRY_ACTION_PERIOD = 5;

    public static final String EVILFAIRY_KEY = "evilfairy";
    public static final int EVILFAIRY_ANIMATION_PERIOD = 7;
    public static final int EVILFAIRY_ACTION_PERIOD = 40;

    public static final String EVILFAIRYATTACKING_KEY = "evilfairyattacking";
    public static final int EVILFAIRYATTACKING_ANIMATION_PERIOD = 20;
    public static final int EVILFAIRYATTACKING_ACTION_PERIOD = 1000;

    public static final String STUMP_KEY = "stump";

    public static final String TREE_KEY = "tree";
    public static final int TREE_NUM_PROPERTIES = 7;
    public static final int TREE_ID = 1;
    public static final int TREE_COL = 2;
    public static final int TREE_ROW = 3;
    public static final int TREE_ANIMATION_PERIOD = 4;
    public static final int TREE_ACTION_PERIOD = 5;
    public static final int TREE_HEALTH = 6;

    public static final int TREE_ANIMATION_MAX = 600;
    public static final int TREE_ANIMATION_MIN = 50;
    public static final int TREE_ACTION_MAX = 1400;
    public static final int TREE_ACTION_MIN = 1000;
    public static final int TREE_HEALTH_MAX = 3;
    public static final int TREE_HEALTH_MIN = 1;

    public static final int PRIEST_SPAWN_X = 1;
    public static final int PRIEST_SPAWN_Y = 28;
    public static final String PRIEST_KEY = "priest";
    public static final int PRIEST_ANIMATION_PERIOD = 7;
    public static final int PRIEST_ACTION_PERIOD = 20;

    public static final String PRIESTATTACKING_KEY = "priestattacking";
    public static final int PRIESTATTACKING_ANIMATION_PERIOD = 12;
    public static final int PRIESTATTACKING_ACTION_PERIOD = 600;




    public static boolean parseBackground(
            String[] properties, WorldModel world, ImageStore imageStore) {
        if (properties.length == BGND_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                    Integer.parseInt(properties[BGND_ROW]));
            String id = properties[BGND_ID];
            world.setBackground(pt,
                    new Background(id, imageStore.getImageList(id)));
        }

        return properties.length == BGND_NUM_PROPERTIES;
    }

    public static boolean parseSapling(
            String[] properties, WorldModel world, ImageStore imageStore) {
        if (properties.length == SAPLING_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[SAPLING_COL]),
                    Integer.parseInt(properties[SAPLING_ROW]));
            String id = properties[SAPLING_ID];
            int health = Integer.parseInt(properties[SAPLING_HEALTH]);
            Sapling sapling = new Sapling(id, pt, imageStore.getImageList(SAPLING_KEY),
                    SAPLING_ACTION_ANIMATION_PERIOD, SAPLING_ACTION_ANIMATION_PERIOD, health, SAPLING_HEALTH_LIMIT);
            world.tryAddEntity(sapling);
        }

        return properties.length == SAPLING_NUM_PROPERTIES;
    }

    public static boolean parseDude(
            String[] properties, WorldModel world, ImageStore imageStore) {
        if (properties.length == DUDE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[DUDE_COL]),
                    Integer.parseInt(properties[DUDE_ROW]));
            Entity entity = createDudeNotFull(properties[DUDE_ID],
                    pt,
                    Integer.parseInt(properties[DUDE_ACTION_PERIOD]),
                    Integer.parseInt(properties[DUDE_ANIMATION_PERIOD]),
                    Integer.parseInt(properties[DUDE_LIMIT]),
                    imageStore.getImageList(DUDE_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == DUDE_NUM_PROPERTIES;
    }

    public static boolean parseFairy(
            String[] properties, WorldModel world, ImageStore imageStore) {
        if (properties.length == FAIRY_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[FAIRY_COL]),
                    Integer.parseInt(properties[FAIRY_ROW]));
            Entity entity = createFairy(properties[FAIRY_ID],
                    pt,
                    Integer.parseInt(properties[FAIRY_ACTION_PERIOD]),
                    Integer.parseInt(properties[FAIRY_ANIMATION_PERIOD]),
                    imageStore.getImageList(FAIRY_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == FAIRY_NUM_PROPERTIES;
    }

    public static boolean parseTree(
            String[] properties, WorldModel world, ImageStore imageStore) {
        if (properties.length == TREE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[TREE_COL]),
                    Integer.parseInt(properties[TREE_ROW]));
            Entity entity = createTree(properties[TREE_ID],
                    pt,
                    Integer.parseInt(properties[TREE_ACTION_PERIOD]),
                    Integer.parseInt(properties[TREE_ANIMATION_PERIOD]),
                    Integer.parseInt(properties[TREE_HEALTH]),
                    imageStore.getImageList(TREE_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == TREE_NUM_PROPERTIES;
    }

    public static boolean parseObstacle(
            String[] properties, WorldModel world, ImageStore imageStore) {
        if (properties.length == OBSTACLE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[OBSTACLE_COL]),
                    Integer.parseInt(properties[OBSTACLE_ROW]));
            Entity entity = createObstacle(properties[OBSTACLE_ID], pt,
                    Integer.parseInt(properties[OBSTACLE_ANIMATION_PERIOD]),
                    imageStore.getImageList(
                            OBSTACLE_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == OBSTACLE_NUM_PROPERTIES;
    }

    public static boolean parseHouse(
            String[] properties, WorldModel world, ImageStore imageStore) {
        if (properties.length == HOUSE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[HOUSE_COL]),
                    Integer.parseInt(properties[HOUSE_ROW]));
            Entity entity = createHouse(properties[HOUSE_ID], pt,
                    imageStore.getImageList(
                            HOUSE_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == HOUSE_NUM_PROPERTIES;
    }

    public static boolean adjacent(Point p1, Point p2) {
        return (p1.x == p2.x && Math.abs(p1.y - p2.y) == 1) || (p1.y == p2.y
                && Math.abs(p1.x - p2.x) == 1);
    }

    public static int getNumFromRange(int max, int min) {
        Random rand = new Random();
        return min + rand.nextInt(
                max
                        - min);
    }


    public static void loadImages(
            Scanner in, ImageStore imageStore, PApplet screen) {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                processImageLine(imageStore.images, in.nextLine(), screen);
            } catch (NumberFormatException e) {
                System.out.println(
                        String.format("Image format error on line %d",
                                lineNumber));
            }
            lineNumber++;
        }
    }

    public static void processImageLine(
            Map<String, List<PImage>> images, String line, PApplet screen) {
        String[] attrs = line.split("\\s");
        if (attrs.length >= 2) {
            String key = attrs[0];
            PImage img = screen.loadImage(attrs[1]);
            if (img != null && img.width != -1) {
                List<PImage> imgs = getImages(images, key);
                imgs.add(img);

                if (attrs.length >= KEYED_IMAGE_MIN) {
                    int r = Integer.parseInt(attrs[KEYED_RED_IDX]);
                    int g = Integer.parseInt(attrs[KEYED_GREEN_IDX]);
                    int b = Integer.parseInt(attrs[KEYED_BLUE_IDX]);
                    setAlpha(img, screen.color(r, g, b), 0);
                }
            }
        }
    }

    public static List<PImage> getImages(
            Map<String, List<PImage>> images, String key) {
        List<PImage> imgs = images.get(key);
        if (imgs == null) {
            imgs = new LinkedList<>();
            images.put(key, imgs);
        }
        return imgs;
    }

    /*
      Called with color for which alpha should be set and alpha value.
      setAlpha(img, color(255, 255, 255), 0));
    */
    public static void setAlpha(PImage img, int maskColor, int alpha) {
        int alphaValue = alpha << 24;
        int nonAlpha = maskColor & COLOR_MASK;
        img.format = PApplet.ARGB;
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            if ((img.pixels[i] & COLOR_MASK) == nonAlpha) {
                img.pixels[i] = alphaValue | nonAlpha;
            }
        }
        img.updatePixels();
    }

    public static void load(
            Scanner in, WorldModel world, ImageStore imageStore) {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                if (!processLine(in.nextLine(), world, imageStore)) {
                    System.err.println(String.format("invalid entry on line %d",
                            lineNumber));
                }
            } catch (NumberFormatException e) {
                System.err.println(
                        String.format("invalid entry on line %d", lineNumber));
            } catch (IllegalArgumentException e) {
                System.err.println(
                        String.format("issue on line %d: %s", lineNumber,
                                e.getMessage()));
            }
            lineNumber++;
        }
    }

    public static boolean processLine(
            String line, WorldModel world, ImageStore imageStore) {
        String[] properties = line.split("\\s");
        if (properties.length > 0) {
            switch (properties[PROPERTY_KEY]) {
                case BGND_KEY:
                    return parseBackground(properties, world, imageStore);
                case DUDE_KEY:
                    return parseDude(properties, world, imageStore);
                case OBSTACLE_KEY:
                    return parseObstacle(properties, world, imageStore);
                case FAIRY_KEY:
                    return parseFairy(properties, world, imageStore);
                case HOUSE_KEY:
                    return parseHouse(properties, world, imageStore);
                case TREE_KEY:
                    return parseTree(properties, world, imageStore);
                case SAPLING_KEY:
                    return parseSapling(properties, world, imageStore);
            }
        }

        return false;
    }

    /*
       Assumes that there is no entity currently occupying the
       intended destination cell.
    */

    public static Action createAnimationAction(Animating animating, int repeatCount) {
        return new Animation(animating, repeatCount);
    }

    public static Action createActivityAction(
            ExecutingActivity executingactivity, WorldModel world, ImageStore imageStore) {
        return new Activity(executingactivity, world, imageStore);
    }

    public static House createHouse(
            String id, Point position, List<PImage> images)
    {
        return new House(id, position, images);
    }

    public static Obstacle createObstacle(
            String id, Point position, int animationPeriod, List<PImage> images)
    {
        return new Obstacle(id, position, images, animationPeriod);
    }

    public static Tree createTree(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int health,
            List<PImage> images)
    {
        return new Tree(id, position, images, animationPeriod, actionPeriod, health, 0);
    }

    public static Stump createStump(
            String id,
            Point position,
            List<PImage> images)
    {
        return new Stump(id, position, images);
    }

    // health starts at 0 and builds up until ready to convert to Tree
    public static Sapling createSapling(
            String id,
            Point position,
            List<PImage> images)
    {
        return new Sapling(id, position, images,
                SAPLING_ACTION_ANIMATION_PERIOD, SAPLING_ACTION_ANIMATION_PERIOD, 0, SAPLING_HEALTH_LIMIT);
    }

    public static Fairy createFairy(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new Fairy(id, position, images,
                animationPeriod, actionPeriod);
    }

    // need resource count, though it always starts at 0
    public static DudeNotFull createDudeNotFull(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int resourceLimit,
            List<PImage> images)
    {
        return new DudeNotFull(id, position, images, resourceLimit, 0,
                animationPeriod, actionPeriod);
    }

    // don't technically need resource count ... full
    public static DudeFull createDudeFull(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int resourceLimit,
            List<PImage> images) {
        return new DudeFull(id, position, images, resourceLimit, 0,
                animationPeriod, actionPeriod);
    }

    public static EvilFairy createEvilFairy(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images) {

            return new EvilFairy(id, position, images, animationPeriod, actionPeriod);
    }

    public static EvilFairyAttacking createEvilFairyAttacking(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images,
            Optional<Entity> target) {

        return new EvilFairyAttacking(id, position, images, animationPeriod, actionPeriod, target);
    }

    public static Priest createPriest(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images) {

        return new Priest(id, position, images, animationPeriod, actionPeriod);
    }

    public static PriestAttacking createPriestAttacking(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images,
            Optional<Entity> target) {

        return new PriestAttacking(id, position, images, animationPeriod, actionPeriod, target);
    }

    public static Optional<Entity> findNearest(
            WorldModel world, Point pos, List<Class> kinds) {
        List<Entity> ofType = new LinkedList<>();
        for (Class kind : kinds) {
            for (Entity entity : world.entities) {
                if (entity.getClass() == kind) {
                    ofType.add(entity);
                }
            }
        }

        return Functions.nearestEntity(ofType, pos);
    }

    public static Optional<Entity> nearestEntity(
            List<Entity> entities, Point pos) {
        if (entities.isEmpty()) {
            return Optional.empty();
        } else {
            Entity nearest = entities.get(0);
            int nearestDistance = distanceSquared(nearest.getPosition(), pos);

            for (Entity other : entities) {
                int otherDistance = distanceSquared(other.getPosition(), pos);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }

    public static int distanceSquared(Point p1, Point p2) {
        int deltaX = p1.x - p2.x;
        int deltaY = p1.y - p2.y;

        return deltaX * deltaX + deltaY * deltaY;
    }


}
