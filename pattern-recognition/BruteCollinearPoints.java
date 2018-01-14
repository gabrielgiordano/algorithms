import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class BruteCollinearPoints {
    private List<LineSegment> segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] original) {
        validateInput(original);

        Point[] points = Arrays.copyOf(original, original.length);

        Arrays.sort(points);

        segments = new ArrayList<LineSegment>();

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                for (int k = j + 1; k < points.length; k++) {
                    for (int m = k + 1; m < points.length; m++) {
                        if (isCollinear(points[i], points[j], points[k], points[m])) {
                            segments.add(new LineSegment(points[i], points[m]));
                        }
                    }
                }
            }
        }
    }

    private void validateInput(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException();

        for (Point point : points)
            if (point == null) throw new IllegalArgumentException();

        Point[] aux = Arrays.copyOf(points, points.length);

        Arrays.sort(aux);

        for (int i = 1; i < aux.length; i++) {
            if (aux[i - 1].compareTo(aux[i]) == 0)
                throw new IllegalArgumentException();
        }
    }

    private boolean isCollinear(Point p, Point q, Point r, Point s) {
        double a = p.slopeTo(q), b = p.slopeTo(r), c = p.slopeTo(s);

        return a == b && a == c && b == c;
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] aux = new LineSegment[numberOfSegments()];

        return segments.toArray(aux);
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
