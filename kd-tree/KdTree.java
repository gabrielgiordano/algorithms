import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import java.util.ArrayList;

public class KdTree {
   private static final boolean X = true;

   private static class Node {
      private Point2D point;
      private Node lb;
      private Node rt;
      private boolean orientation;

      public Node(Point2D point, boolean orientation) {
         this.point = point;
         this.orientation = orientation;
      }
   }

   private Node root;
   private int size;
   private Point2D nearestPoint;
   private double minDistance;

   // construct an empty set of points
   public KdTree() {
      root = null;
      size = 0;
   }

   // is the set empty?
   public boolean isEmpty() {
      return size == 0;
   }

   // number of points in the set
   public int size() {
      return size;
   }

   // add the point to the set (if it is not already in the set)
   public void insert(Point2D p) {
      if (p == null) throw new IllegalArgumentException();

      root = put(p, root, X);
   }

   private Node put(Point2D point, Node node, boolean orientation) {
      if (node == null) {
         size++;
         return new Node(point, orientation);
      }

      if (point.equals(node.point)) {
         node.point = point;
         return node;
      }

      double pointCoordinate, nodeCoordinate;

      if (node.orientation == X) {
         pointCoordinate = point.x();
         nodeCoordinate = node.point.x();
      } else {
         pointCoordinate = point.y();
         nodeCoordinate = node.point.y();
      }

      if (pointCoordinate < nodeCoordinate)
         node.lb = put(point, node.lb, !orientation);
      else
         node.rt = put(point, node.rt, !orientation);

      return node;
   }

   // does the set contain point p?
   public boolean contains(Point2D p) {
      if (p == null) throw new IllegalArgumentException();

      return search(p, root) != null;
   }

   private Node search(Point2D point, Node node) {
      if (node == null) return null;
      if (point.equals(node.point)) return node;

      double pointCoordinate, nodeCoordinate;

      if (node.orientation == X) {
         pointCoordinate = point.x();
         nodeCoordinate = node.point.x();
      } else {
         pointCoordinate = point.y();
         nodeCoordinate = node.point.y();
      }

      if (pointCoordinate < nodeCoordinate)
         return search(point, node.lb);
      else
         return search(point, node.rt);
   }

   // draw all points to standard draw
   public void draw() {
      drawHelper(root, 0, 0, 1, 1);
   }

   private void drawHelper(Node node,
                           double xmin, double ymin, double xmax, double ymax)
   {
      if (node == null) return;

      StdDraw.setPenRadius(0.01);
      StdDraw.setPenColor(StdDraw.BLACK);
      StdDraw.point(node.point.x(), node.point.y());

      StdDraw.setPenRadius(0.0025);

      if (node.orientation == X) {
         StdDraw.setPenColor(StdDraw.RED);
         StdDraw.line(node.point.x(), ymin, node.point.x(), ymax);

         drawHelper(node.lb, xmin, ymin, node.point.x(), ymax);
         drawHelper(node.rt, node.point.x(), ymin, xmax, ymax);
      }
      else {
         StdDraw.setPenColor(StdDraw.BLUE);
         StdDraw.line(xmin, node.point.y(), xmax, node.point.y());

         drawHelper(node.lb, xmin, ymin, xmax, node.point.y());
         drawHelper(node.rt, xmin, node.point.y(), xmax, ymax);
      }
   }

   // all points that are inside the rectangle (or on the boundary)
   public Iterable<Point2D> range(RectHV rect) {
      if (rect == null) throw new IllegalArgumentException();

      ArrayList<Point2D> list = new ArrayList<>();

      rangeHelper(root, list, rect,
                  0, 0, 1, 1);

      return list;
   }

   private void rangeHelper(Node node, ArrayList<Point2D> list, RectHV rect,
                            double xmin, double ymin, double xmax, double ymax)
   {
      if (node == null) return;

      if (rect.contains(node.point))
         list.add(node.point);

      RectHV lbRect, rtRect;

      if (node.orientation == X) {
         lbRect = new RectHV(xmin, ymin, node.point.x(), ymax);
         rtRect = new RectHV(node.point.x(), ymin, xmax, ymax);

         if (rect.intersects(lbRect))
            rangeHelper(node.lb, list, rect,
                        xmin, ymin, node.point.x(), ymax);

         if (rect.intersects(rtRect))
            rangeHelper(node.rt, list, rect,
                        node.point.x(), ymin, xmax, ymax);
      }
      else {
         lbRect = new RectHV(xmin, ymin, xmax, node.point.y());
         rtRect = new RectHV(xmin, node.point.y(), xmax, ymax);

         if (rect.intersects(lbRect))
            rangeHelper(node.lb, list, rect,
                        xmin, ymin, xmax, node.point.y());

         if (rect.intersects(rtRect))
            rangeHelper(node.rt, list, rect,
                        xmin, node.point.y(), xmax, ymax);
      }
   }

   // a nearest neighbor in the set to point p; null if the set is empty
   public Point2D nearest(Point2D p) {
      if (p == null) throw new IllegalArgumentException();

      nearestPoint = null;
      minDistance = Double.POSITIVE_INFINITY;

      nearestHelper(p, root,
                    0, 0, 1, 1);

      return nearestPoint;
   }

   private void nearestHelper(Point2D p, Node node,
                              double xmin, double ymin, double xmax, double ymax)
   {
      if (node == null) return;

      double distance = p.distanceSquaredTo(node.point);

      if (distance < minDistance) {
         minDistance = distance;
         nearestPoint = node.point;
      }

      RectHV recAux;
      double recDistance;

      if (node.orientation == X) {
         if (p.x() < node.point.x()) {
            nearestHelper(p, node.lb,
                          xmin, ymin, node.point.x(), ymax);

            recAux = new RectHV(node.point.x(), ymin, xmax, ymax);
            recDistance = recAux.distanceSquaredTo(p);

            if (minDistance > recDistance)
               nearestHelper(p, node.rt,
                             node.point.x(), ymin, xmax, ymax);
         } else {
            nearestHelper(p, node.rt,
                          node.point.x(), ymin, xmax, ymax);

            recAux = new RectHV(xmin, ymin, node.point.x(), ymax);
            recDistance = recAux.distanceSquaredTo(p);

            if (minDistance > recDistance)
               nearestHelper(p, node.lb,
                             xmin, ymin, node.point.x(), ymax);
         }
      } else {
         if (p.y() < node.point.y()) {
            nearestHelper(p, node.lb,
                          xmin, ymin, xmax, node.point.y());

            recAux = new RectHV(xmin, node.point.y(), xmax, ymax);
            recDistance = recAux.distanceSquaredTo(p);

            if (minDistance > recDistance)
               nearestHelper(p, node.rt,
                             xmin, node.point.y(), xmax, ymax);
         } else {
            nearestHelper(p, node.rt,
                          xmin, node.point.y(), xmax, ymax);

            recAux = new RectHV(xmin, ymin, xmax, node.point.y());
            recDistance = recAux.distanceSquaredTo(p);

            if (minDistance > recDistance)
               nearestHelper(p, node.lb,
                             xmin, ymin, xmax, node.point.y());
         }
      }
   }

   // unit testing of the methods (optional)
   public static void main(String[] args) {
      String filename = args[0];
      In in = new In(filename);

      KdTree kdtree = new KdTree();

      while (!in.isEmpty()) {
         String label = in.readString();
         double x = in.readDouble();
         double y = in.readDouble();
         Point2D p = new Point2D(x, y);
         kdtree.insert(p);
      }

      Point2D query = new Point2D(0.03125, 0.8125);

      kdtree.nearest(query);
   }
}
