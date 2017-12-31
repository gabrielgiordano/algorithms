import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int gridSize;
    private int firstVirtualSiteIndex;
    private int lastVirtualSiteIndex;
    private WeightedQuickUnionUF unionFind;
    private boolean[] sitesOpen;
    private int numberOfOpenSites;
    
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        
        int numberOfSites = n * n;
        
        gridSize = n;        
        firstVirtualSiteIndex = numberOfSites;
        lastVirtualSiteIndex = numberOfSites + 1;
        unionFind = new WeightedQuickUnionUF(numberOfSites + 2);
        sitesOpen = new boolean[numberOfSites];
        numberOfOpenSites = 0;
                
        for (int i = 1; i <= n; i++) {
            int firstRowIndex = rowColToInt(1, i);
            int lastRowIndex = rowColToInt(gridSize, i);
            
            unionFind.union(firstVirtualSiteIndex, firstRowIndex);
            unionFind.union(lastVirtualSiteIndex, lastRowIndex);
        }
    }
    
    public void open(int row, int col) {
        if (!isIndexValid(row, col)) throw new IllegalArgumentException();
        if (isOpen(row, col)) return;
        
        int index = rowColToInt(row, col);
        
        sitesOpen[index] = true;
        numberOfOpenSites += 1;
        
        connectNeighbor(index, row - 1, col);
        connectNeighbor(index, row + 1, col);
        connectNeighbor(index, row, col - 1);
        connectNeighbor(index, row, col + 1);
    }
    
    private void connectNeighbor(int index, int row, int col) {
        if (isIndexValid(row, col) && isOpen(row, col))
            unionFind.union(index, rowColToInt(row, col));
    }
    
    public boolean isOpen(int row, int col) {
        if (!isIndexValid(row, col)) throw new IllegalArgumentException();
        
        return sitesOpen[rowColToInt(row, col)];
    }
    
    public boolean isFull(int row, int col) {
        if (!isIndexValid(row, col)) throw new IllegalArgumentException();
        
        int index = rowColToInt(row, col);
        return isOpen(row, col) && 
            unionFind.connected(index, firstVirtualSiteIndex);
    }
    
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }
    
    public boolean percolates() {
        return unionFind.connected(firstVirtualSiteIndex, lastVirtualSiteIndex);
    }
    
    private int rowColToInt(int row, int col) {
        int rowIndex = (row - 1) * gridSize;
        int colIndex = col - 1;
        
        return rowIndex + colIndex;
    }
    
    private boolean isIndexValid(int row, int col) {
        return isBetween1AndN(row) && isBetween1AndN(col) ? true : false;
    }

    private boolean isBetween1AndN(int index) {
        return index >= 1 && index <= gridSize ? true : false;
    }
    
    public static void main(String [] args) {
    }
}