import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.ResizingArrayStack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private Node goal;

    private class Node implements Comparable<Node> {
        public final Board board;
        public final Node previous;
        public final int movesMade;
        private final int heuristic;

        public Node(Board board, Node previous) {
            this.board = board;
            this.previous = previous;

            if (previous != null)
                this.movesMade = previous.movesMade + 1;
            else
                this.movesMade = 0;

            this.heuristic = this.board.manhattan() + this.movesMade;
        }

        public int compareTo(Node that) {
            return this.heuristic - that.heuristic;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        MinPQ<Node> pq1 = new MinPQ<>();
        MinPQ<Node> pq2 = new MinPQ<>();

        pq1.insert(new Node(initial, null));
        pq2.insert(new Node(initial.twin(), null));

        while (!pq1.isEmpty() && !pq2.isEmpty()) {
            goal = step(pq1);

            if (goal != null) break;
            if (step(pq2) != null) break;
        }
    }

    private Node step(MinPQ<Node> priorityQueue) {
        Node node = priorityQueue.delMin();

        if (node.board.isGoal()) return node;

        for (Board neighbor : node.board.neighbors())
            if (node.previous == null || !neighbor.equals(node.previous.board))
                priorityQueue.insert(new Node(neighbor, node));

        return null;
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return goal != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable()) return goal.movesMade;
        else return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;

        ResizingArrayStack<Board> stack = new ResizingArrayStack<>();
        Node aux = goal;

        while (aux != null) {
            stack.push(aux.board);
            aux = aux.previous;
        }

        return stack;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
