import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Algorithms {

    public Algorithms(){

    }

    public Object[] pickAlgorithm(int selector, int[] values, int[] weights, int W){
        if (selector == 0){
            ArrayList<Object> res = new ArrayList<>(Arrays.asList(solveKnapsack(values, weights, W)));
            res.add(getSelectedItems((int[][]) res.get(2),values, weights, W));
            return res.toArray(new Object[0]);
        }
        else{
            Item[] items = new Item[values.length];
            for (int i = 0; i < values.length; i++) {
                items[i] = new Item(weights[i], values[i], i);
            }
            return getMaxValue(items, W);
        }
    }

    public Object[] solveKnapsack(int[] values, int[] weights, int W) {
        long start = System.nanoTime();
        // --- Input Validation ---
        if (values == null || weights == null || values.length != weights.length) {
            throw new IllegalArgumentException("Values and weights arrays must be non-null and have the same length.");
        }
        if (W < 0) {
            throw new IllegalArgumentException("Knapsack capacity (W) cannot be negative.");
        }

        int n = values.length; // Number of items

        // --- Base Case: No items or zero capacity ---
        if (n == 0 || W == 0) {
            return new Object[] {0, (int)( System.nanoTime() - start)};
        }

        // --- DP Table Initialization ---
        // dp[i][w] will store the maximum value using items up to index i-1
        // (i.e., first i items) with a maximum weight capacity of w.
        // We use n+1 rows and W+1 columns to handle base cases easily (0 items or 0 capacity).
        int[][] dp = new int[n + 1][W + 1];

        // --- Build the DP Table ---
        // Iterate through items (rows)
        for (int i = 1; i <= n; i++) {
            // Current item's index in the original arrays (0-based)
            int currentItemIndex = i - 1;
            int currentValue = values[currentItemIndex];
            int currentWeight = weights[currentItemIndex];

            // Iterate through possible capacities (columns)
            for (int w = 1; w <= W; w++) {
                // Option 1: Don't include the current item (i)
                // The value is the same as the max value using previous items (i-1) with the same capacity (w).
                int valueWithoutCurrent = dp[i - 1][w];

                // Option 2: Include the current item (i), if its weight allows
                int valueWithCurrent = 0;
                if (currentWeight <= w) {
                    // Value is the current item's value plus the max value using previous items (i-1)
                    // with the remaining capacity (w - currentWeight).
                    valueWithCurrent = currentValue + dp[i - 1][w - currentWeight];
                }

                // dp[i][w] is the maximum of the two options
                dp[i][w] = Math.max(valueWithoutCurrent, valueWithCurrent);
            }
        }

        // --- Result ---
        // The final answer is in the bottom-right corner of the table,
        // representing the max value using all n items with capacity W.
        return new Object[]{dp[n][W], (int)( System.nanoTime() - start), dp};
    }

    // --- Optional: Method to reconstruct the selected items (requires the DP table) ---
    /**
     * Reconstructs the list of items included in the optimal solution.
     * This method should be called AFTER solveKnapsack has populated the dp table.
     *
     * @param dp      The DP table computed by solveKnapsack.
     * @param values  Array containing the values of the items.
     * @param weights Array containing the weights of the items.
     * @param W       The maximum capacity of the knapsack.
     * @return An array or list containing the indices (0-based) of the selected items.
     */
    public java.util.List<Integer> getSelectedItems(int[][] dp, int[] values, int[] weights, int W) {
        if (dp == null || values == null || weights == null || values.length != weights.length) {
            throw new IllegalArgumentException("Invalid input for reconstructing items.");
        }
        int n = values.length;
        java.util.List<Integer> selectedItems = new java.util.ArrayList<>();
        int currentW = W;

        // Start from the bottom-right corner and backtrack
        for (int i = n; i > 0 && currentW > 0; i--) {
            // Compare dp[i][currentW] with dp[i-1][currentW]
            // If they are different, it means item i-1 (0-based index) was included in the optimal solution for dp[i][currentW]
            if (dp[i][currentW] != dp[i - 1][currentW]) {
                int itemIndex = i - 1;
                selectedItems.add(itemIndex); // Add the index of the included item
                currentW -= weights[itemIndex]; // Reduce the capacity
            }
            // If they are the same, item i-1 was not included, so just move up to the previous item.
        }
        java.util.Collections.reverse(selectedItems); // Items are added in reverse order
        return selectedItems;
    }

    /**
     * Solves the Fractional Knapsack problem using a Greedy approach.
     *
     * @param items    Array of items available.
     * @param capacity The maximum weight capacity of the knapsack.
     * @return The maximum value that can be obtained.
     */
    public static Object[] getMaxValue(Item[] items, int capacity) {
        long start = System.nanoTime();
        ArrayList<Item> selectedItems = new ArrayList<Item>();

        if (items == null || items.length == 0 || capacity <= 0) {
            return new Object[] {0.0, (int)( System.nanoTime() - start), selectedItems};
        }

        // Sort items by value-to-weight ratio in descending order
        Arrays.sort(items, new ItemRatioComparator());

        double totalValue = 0.0;
        double currentCapacity = capacity;

//        System.out.println("Sorted Items (by ratio descending):");
        for (Item item : items) {
//            System.out.println(item);
        }
//        System.out.println("-----------------------------------");
//        System.out.println("Filling Knapsack (Capacity = " + capacity + ")");


        for (Item item : items) {
            if (currentCapacity == 0) {
                break; // Knapsack is full
            }

            if (item.weight <= currentCapacity) {
                // Take the whole item
                currentCapacity -= item.weight;
                totalValue += item.value;
//                System.out.println("Took full item: " + item + " | Remaining Capacity: " + currentCapacity);
                selectedItems.add(item);

            } else {
                // Take a fraction of the item to fill the remaining capacity
                double fraction = currentCapacity / item.weight;
                double fractionalValue = fraction * item.value;
                totalValue += fractionalValue;
//                System.out.println("Took fraction (" + String.format("%.2f", fraction * 100) + "%) of item: " + item +
//                        " | Value added: " + String.format("%.2f", fractionalValue));
                selectedItems.add(item);
                currentCapacity = 0; // Knapsack is now full
//                System.out.println("Knapsack full.");
            }
        }

        return new Object[] {totalValue, (int)( System.nanoTime() - start), selectedItems};
    }
}
