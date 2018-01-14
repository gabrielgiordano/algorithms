import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class FastCollinearPoints {
    private List<LineSegment> segments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] original) {
        validateInput(original);

        Point[] points = Arrays.copyOf(original, original.length);

        segments = new ArrayList<LineSegment>();

        for (int i = 0; i < original.length; i++) {
            Point p = original[i];

            Arrays.sort(points);
            Arrays.sort(points, p.slopeOrder());

            for (int min = 1, max = 1; min < points.length; min = max + 1) {
                max = findLastIndexWithSlopeEqualToMin(points, p, min);

                if (hasAtLeastFourElements(min, max) && p.compareTo(points[min]) < 0)
                    segments.add(new LineSegment(p, points[max]));
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

    private int findLastIndexWithSlopeEqualToMin(Point[] points, Point p, int min) {
        int max = min + 1;

        while (max < points.length && p.slopeTo(points[min]) == p.slopeTo(points[max]))
            max++;

        return max - 1;
    }

    private boolean hasAtLeastFourElements(int min, int max) {
        return max - min >= 2;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
