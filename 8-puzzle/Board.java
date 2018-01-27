import java.util.ArrayList;

public class Board {
    private final int[][] blocks;
    private final int dimension;
    private final int size;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        int [][] copy = new int[blocks.length][blocks.length];

        for (int i = 0; i < blocks.length; i++)
            for (int j = 0; j < blocks.length; j++)
                copy[i][j] = blocks[i][j];

        this.blocks = copy;
        this.dimension = copy.length;
        this.size = dimension * dimension;
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    // number of blocks out of place
    public int hamming() {
        int count = 0;

        for (int i = 1; i < size; i++)
            if (isOutOfPlace(i)) count++;

        return count;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int count = 0;

        for (int i = 0; i < blocks.length; i++)
            for (int j = 0; j < blocks.length; j++) {
                int k = blocks[i][j];

                if (k > 0 && isOutOfPlace(k))
                    count += manhattanDistance(k, i, j);
            }

        return count;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 1; i < size; i++)
            if (isOutOfPlace(i)) return false;

        return true;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int i = 0;
        while (i < size && blocks[row(i)][col(i)] == 0)
            i++;

        int j = i + 1;
        while (j < size && blocks[row(j)][col(j)] == 0)
            j++;

        return exchangeValues(row(i), col(i), row(j), col(j));
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;

        Board that = (Board) y;

        if (that.blocks.length != blocks.length) return false;

        for (int i = 0; i < blocks.length; i++)
            for (int j = 0; j < blocks.length; j++)
                if (that.blocks[i][j] != blocks[i][j]) return false;

        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> list = new ArrayList<>();

        int position = findZeroPosition();
        int row = row(position);
        int col = col(position);

        if (row < dimension - 1) list.add(exchangeValues(row, col, row + 1, col));
        if (row > 0)             list.add(exchangeValues(row, col, row - 1, col));
        if (col < dimension - 1) list.add(exchangeValues(row, col, row, col + 1));
        if (col > 0)             list.add(exchangeValues(row, col, row, col - 1));

        return list;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append(dimension + "\n");

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }

            s.append("\n");
        }

        return s.toString();
    }

    private boolean isOutOfPlace(int i) {
        int row = row(i - 1);
        int column = col(i - 1);

        return blocks[row][column] != i;
    }

    private int manhattanDistance(int i, int row, int column) {
        int expectedRow = row(i - 1);
        int expectedColumn = col(i - 1);

        return Math.abs(expectedRow - row) + Math.abs(expectedColumn - column);
    }

    private int row(int i) {
        return i / dimension;
    }

    private int col(int i) {
        return i % dimension;
    }

    private int findZeroPosition() {
        int zero = 0;

        while (zero < size && blocks[row(zero)][col(zero)] != 0)
            zero++;

        return zero;
    }

    private int[][] createCopy() {
        int [][] copy = new int[dimension][dimension];

        for (int i = 0; i < blocks.length; i++)
            for (int j = 0; j < blocks.length; j++)
                copy[i][j] = blocks[i][j];

        return copy;
    }

    private Board exchangeValues(int i, int j, int k, int m) {
        int[][] copy = createCopy();

        int temp = copy[i][j];
        copy[i][j] = copy[k][m];
        copy[k][m] = temp;

        return new Board(copy);
    }

    // unit tests (not graded)
    public static void main(String[] args) { }

}
