/******************************************************************************
 *  Compilation:  javac KdTreeVisualizer.java
 *  Execution:    java KdTreeVisualizer
 *  Dependencies: KdTree.java
 *
 *  Add the points that the user clicks in the standard draw window
 *  to a kd-tree and draw the resulting kd-tree.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.HashMap;

public class KdTreeVisualizer {

    public static void main(String[] args) {
        RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
        StdDraw.enableDoubleBuffering();
        KdTree kdtree = new KdTree();
        HashMap<Point2D, String> map = new HashMap<>();

        String filename = args[0];
        In in = new In(filename);
        String label;
        Point2D query = new Point2D(0.03125, 0.8125);

        while (!in.isEmpty()) {
            label = in.readString();
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);

            StdOut.printf("%s to X: %f\n", label, p.distanceTo(query));
            StdOut.printf("%s.x to X.x: %f\n", label, Math.abs(p.x() - query.x()));
            StdOut.printf("%s.y to X.y: %f\n", label, Math.abs(p.y() - query.y()));
            map.put(p, label);
            kdtree.insert(p);
        }

        while (true) {
            StdDraw.clear();

            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.rectangle(0.5,0.5,0.48,0.48);

            kdtree.draw();

            StdDraw.setPenColor(StdDraw.BLACK);
            for (Point2D point : kdtree.range(rect))
                StdDraw.text(point.x()  * 0.95 + 0.025, (point.y() - 0.025)  * 0.95 + 0.025, map.get(point));

            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.point(0.03125  * 0.95 + 0.025, 0.8125  * 0.95 + 0.025);
            StdDraw.text(0.03125 * 0.95 + 0.025, (0.8125 - 0.025) * 0.95 + 0.025, "X");

            StdDraw.show();

            StdDraw.pause(20);
        }

    }
}
