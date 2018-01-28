import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import java.util.ArrayList;

/*
 * Performance requirements:
 *
 * Your implementation should support insert() and contains() in time
 * proportional to the logarithm of the number of points in the set in
 * the worst case;
 *
 * it should support nearest() and range() in time proportional to the
 * number of points in the set.
*/
public class PointSET {
    private SET<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);

        for (Point2D point : points)
            StdDraw.point(point.x(), point.y());
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        ArrayList<Point2D> list = new ArrayList<>();

        for (Point2D point : points)
            if (rect.contains(point))
                list.add(point);

        return list;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        Point2D nearestNeighbor = null;
        double distance = Double.POSITIVE_INFINITY;

        for (Point2D point : points) {
            double d = p.distanceSquaredTo(point);

            if (d < distance) {
                distance = d;
                nearestNeighbor = point;
            }
        }

        return nearestNeighbor;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}
