import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int gridSize;
    private int trials;
    private double[] results;
    
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();
        
        gridSize = n;
        this.trials = trials;
        results = new double[trials];      
        
        runExperiments();
    }
    
    public double mean() { 
        return StdStats.mean(results);
    }
    
    public double stddev() { 
        return StdStats.stddev(results); 
    }
    
    public double confidenceLo() { 
        return mean() - confidenceRange();
    }
    
    public double confidenceHi() { 
        return mean() + confidenceRange();
    }
    
    private double confidenceRange() {
        return (1.96 * stddev()) / Math.sqrt(trials);
    }
    
    private void runExperiments() {
        for (int i = 0; i < trials; i++) {
            results[i] = runExperiment();
        }
    }
    
    private double runExperiment() {
        Percolation percolation = new Percolation(gridSize);
        
        do {
            int row = StdRandom.uniform(gridSize) + 1;
            int col = StdRandom.uniform(gridSize) + 1;

            percolation.open(row, col);
        } while (!percolation.percolates()) ;
        
        return percolation.numberOfOpenSites() / (double) (gridSize * gridSize);
    }
    
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        
        PercolationStats stats = new PercolationStats(n, trials);
        
        StdOut.printf("mean                    = %f\n", stats.mean());
        StdOut.printf("stddev                  = %f\n", stats.stddev());
        StdOut.printf("95%% confidence interval = [%f, %f]\n", 
                      stats.confidenceLo(), 
                      stats.confidenceHi());
    }
}