# Assignment 4

Algorithms (CSCI 3343) - Julian Spindola

## Overview

Below, I will be using a Large Language Model (LLM) to generate solutions to two common algorithmic problems – 0/1 Knapsack and the Fractional Knapsack problems. I will be using the provided test cases and conduct an analysis of the LLM’s performance and code.

The LLM that I have chosen for this assignment is Gemini 2.5 Pro.

## Prompts

For the 0/1 Knapsack problem, I used the following prompt:

“Generate a solution to the 0/1 Knapsack Problem using Dynamic Programming in Java”

For the Fractional Knapsack problem, I used the following prompt:

“Generate a solution to the Fractional Knapsack Problem using a Greedy Algorithm in Java”

## Results

For the 0/1 Knapsack problem, Gemini produced the following code:

```java
import java.util.Arrays;

/**
 * Solves the 0/1 Knapsack Problem using Dynamic Programming.
 *
 * The problem: Given a set of items, each with a weight and a value,
 * determine the subset of items to include in a collection so that the
 * total weight is less than or equal to a given limit (knapsack capacity)
 * and the total value is as large as possible. You can either take an
 * item entirely or leave it (0/1 property).
 */
public class KnapsackDP {

    /**
     * Solves the 0/1 Knapsack problem.
     *
     * @param values  Array containing the values of the items.
     * @param weights Array containing the weights of the items.
     * @param W       The maximum capacity of the knapsack.
     * @return The maximum value that can be obtained.
     * @throws IllegalArgumentException if input arrays are null, have different lengths,
     * or if W is negative.
     */
    public int solveKnapsack(int[] values, int[] weights, int W) {
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
            return 0;
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
        return dp[n][W];
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
```

<table><tbody><tr><th colspan="2"><p>Similarities and Differences from Pseudocode</p></th></tr><tr><td><p>Similarities</p></td><td><p>Differences</p></td></tr><tr><td><ul><li>Both programs build a two-dimensional matrix (# items x max capacity)</li></ul></td><td><ul><li>The generated code uses two for-loops while the pseudocode uses two while-loops</li><li>The pseudocode has a condition that checks whether the current weight, <em>weight[i]</em>, is greater than the maximum capacity. In the generated code, this check is present, but sets a variable initialized to 0 to the current item’s value + previous max value, opposed to the pseudocode just returning the previous max value</li><li>The generated code provides a method for finding the indices of the elements that add up to the calculated max value</li></ul></td></tr></tbody></table>

For the Fractional Knapsack problem, Gemini produced the following code:

```java
import java.util.Arrays;
import java.util.Comparator;

// Represents an item with weight and value
class Item {
    int weight;
    int value;
    double ratio; // Value-to-weight ratio

    public Item(int weight, int value) {
        this.weight = weight;
        this.value = value;
        // Calculate ratio, handle potential division by zero (though weight should > 0)
        if (weight > 0) {
            this.ratio = (double) value / weight;
        } else {
            // Assign a very high ratio if value > 0 and weight is 0 (infinite density)
            // Assign 0 if both are 0. Adjust logic as needed for specific constraints.
            this.ratio = (value > 0) ? Double.POSITIVE_INFINITY : 0.0;
        }
    }

    @Override
    public String toString() {
        return "Item [weight=" + weight + ", value=" + value + ", ratio=" + String.format("%.2f", ratio) + "]";
    }
}

// Comparator to sort items by ratio in descending order
class ItemRatioComparator implements Comparator<Item> {
    @Override
    public int compare(Item item1, Item item2) {
        // Sort in descending order of ratio
        // Double.compare is safer than subtraction for floating points
        return Double.compare(item2.ratio, item1.ratio);
    }
}

public class FractionalKnapsack {

    /**
     * Solves the Fractional Knapsack problem using a Greedy approach.
     *
     * @param items    Array of items available.
     * @param capacity The maximum weight capacity of the knapsack.
     * @return The maximum value that can be obtained.
     */
    public static double getMaxValue(Item[] items, int capacity) {
        if (items == null || items.length == 0 || capacity <= 0) {
            return 0.0;
        }

        // Sort items by value-to-weight ratio in descending order
        Arrays.sort(items, new ItemRatioComparator());

        double totalValue = 0.0;
        double currentCapacity = capacity;

        System.out.println("Sorted Items (by ratio descending):");
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.println("-----------------------------------");
        System.out.println("Filling Knapsack (Capacity = " + capacity + ")");


        for (Item item : items) {
            if (currentCapacity == 0) {
                break; // Knapsack is full
            }

            if (item.weight <= currentCapacity) {
                // Take the whole item
                currentCapacity -= item.weight;
                totalValue += item.value;
                System.out.println("Took full item: " + item + " | Remaining Capacity: " + currentCapacity);

            } else {
                // Take a fraction of the item to fill the remaining capacity
                double fraction = currentCapacity / item.weight;
                double fractionalValue = fraction * item.value;
                totalValue += fractionalValue;
                System.out.println("Took fraction (" + String.format("%.2f", fraction * 100) + "%) of item: " + item +
                                   " | Value added: " + String.format("%.2f", fractionalValue));
                currentCapacity = 0; // Knapsack is now full
                System.out.println("Knapsack full.");
            }
        }

        return totalValue;
    }
```

<table><tbody><tr><th colspan="2"><p>Similarities and Differences from Pseudocode</p></th></tr><tr><td><p>Similarities</p></td><td><p>Differences</p></td></tr><tr><td><ul><li>Both programs follow the same general procedure, going through each item, adding it into the knapsack until it reaches capacity.</li></ul></td><td><ul><li>The generated code provides a class for the Item and the required ItemComparator class for sorting</li><li>The generated code uses a for-loop to iterate through the items</li><li>The psuedocode’s <em>do if </em>is replaced by an early break statement in the generated code</li></ul></td></tr></tbody></table>

## Tests

Below is a tabular representation of my results completing the three tests outlined in the assignment’s requirements.

| Data Set      | 0/1 Knapsack Result | Included Item Indices           | Time Complexity | Exec. Time | Fractional Knapsack Result | Included Items Indices                                    | Time Complexity | Exec. Time |
|---------------|---------------------|----------------------------------|------------------|-------------|-----------------------------|------------------------------------------------------------|------------------|-------------|
| Data Set 1    | 220                 | [1, 2]                           | O(n * W)         | 22100       | 240                         | [0, 1, 2]                                                  | O(n log n)       | 785900      |
| Data Set 2    | 37                  | [1, 2, 3]                        | O(n * W)         | 8800        | 40.2                        | [0, 1, 2, 3]                                               | O(n log n)       | 5600        |
| Random Data   | 2500                | [4, 8, 17, 22, 37, 43, 44, 46, 58, 63, 72, 74] | O(n * W)         | 2095100     | 2500                        | [4, 8, 17, 18, 22, 37, 43, 44, 46, 51, 58, 63, 72, 74]     | O(n log n)       | 267400      |


_\*Random Data Weights: \[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100\]_

_\*Random Data Values: \[2, 8, 12, 16, 25, 26, 34, 37, 45, 47, 51, 57, 61, 69, 73, 77, 83, 90, 95, 97, 101, 109, 115, 117, 121, 127, 133, 136, 141, 147, 153, 159, 164, 168, 173, 178, 182, 190, 192, 197, 203, 208, 212, 220, 225, 227, 235, 236, 243, 247, 252, 260, 262, 268, 273, 277, 283, 288, 295, 298, 302, 308, 314, 320, 321, 327, 331, 338, 342, 348, 351, 357, 365, 368, 375, 377, 382, 389, 394, 396, 404, 406, 413, 417, 423, 426, 433, 438, 445, 448, 455, 457, 462, 469, 472, 476, 482, 489, 491, 498\]_

## Conclusion

It seems that Gemini 2.5 Pro was successfully able to produce correct and working implementations of the two variations of the Knapsack problem. The generated code contained helpful comments explaining the logic behind each algorithm. I would go as far as to say that the generated code is easier to understand than the provided pseudocode and enhanced my comprehension of this algorithm. It should be noted, however, that the generated code can be inconsistent in its implementation. For example, the LLM created an Item class for the Greedy solution but not the Dynamic Programming approach. For this reason, it is best to use LLMs with caution while coding, especially in existing code environments.
