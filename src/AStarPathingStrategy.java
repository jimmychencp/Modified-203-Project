import org.junit.platform.engine.support.hierarchical.Node;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Map.*;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.*;

public class AStarPathingStrategy implements PathingStrategy{

    class Node {
        private int g;
        private int h;
        private int f;
        private Node prev_node;
        private Point position;

        public Node (int g, int h, int f, Point position, Node prev_node){
            this.g = g;
            this.h = h;
            this.f = f;
            this.prev_node = prev_node;
            this.position = position;
        }

        public int getH(){return h;}
        public int getF(){return f;}
        public int getG(){return g;}
        public Point getPosition(){return position;}
        public Node getPrevNode(){return prev_node;}
        public void setPrevNode(Node node) {prev_node = node;}
    }

    private int heuristic(Point start, Point end) {return Functions.distanceSquared(start, end);}

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        List<Point> pathing = new ArrayList<Point>();
        HashMap<Point, Node> closedList = new HashMap<Point, Node>();
        HashMap<Point, Node> openList = new HashMap<Point, Node>();
        Queue<Node> openQueue = new PriorityQueue<Node>(Comparator.comparingInt(Node::getF));
        Node current = new Node(0, heuristic(start, end), heuristic(start, end), start,null);
        openQueue.add(current);
        openList.put(start, current);

        while (current != null && !withinReach.test(current.getPosition(), end)) {
            List<Point> neighbors = potentialNeighbors.apply(current.getPosition())
                    .filter(canPassThrough)
                    .filter(pt -> !closedList.containsKey(pt))
                    .collect(Collectors.toList());
            for (Point adj : neighbors) {
                    if (!closedList.containsKey(adj)) {
                        int tempG = current.getG() + 1;
                        if (openList.containsKey(adj)) {
                            if (openList.get(adj).getG() > tempG) {
                                Node newNode = new Node(tempG, heuristic(adj, end), tempG + heuristic(adj, end), adj, current);
                                openQueue.remove(adj);
                                openQueue.add(newNode);
                                openList.replace(adj, newNode);
                            }
                        }
                        else {
                            Node newNode = new Node(tempG, heuristic(adj, end), tempG + heuristic(adj, end), adj, current);
                            openList.put(adj, newNode);
                            openQueue.add(newNode);
                        }
                }
            }
            openList.remove(current.getPosition());
            openQueue.remove(current);
            closedList.put(current.getPosition(), current);
            current = openQueue.poll();
        }
        if (current != null) {
            while (current.getPrevNode() != null) {
                pathing.add(0, current.getPosition());
                current = current.prev_node;
            }
        }
            return pathing;
    }
}


