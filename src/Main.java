import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        /** Sample Data Set 1 **/
        int[] sampleData1Weights = {10, 20, 30};
        int[] sampleData1Values = {60, 100, 120};
        int sampleData1Max= 50;

        /** Sample Data Set 2 **/
        int[] sampleData2Weights = {1, 2, 3, 5};
        int[] sampleData2Values = {6, 11, 12, 14};
        int sampleData2Max= 10;

        /** Randomly Generated Items **/
        int[] randomItemWeights = new int[100];
        int[] randomItemValues= new int[100];
        int randomItemMax = 500;
        for (int i = 0; i < randomItemWeights.length; i++) {
            randomItemWeights[i] = i + 1; // set weights of items to 1-100
            randomItemValues[i] = (int) (Math.random() * 5 + 1) + (i * 5); //set item values to proportionally random values
        }
        System.out.println("Randomly Generated Data:");
        System.out.println("weights: " + Arrays.toString(randomItemWeights));
        System.out.println("values: " + Arrays.toString(randomItemValues));


        Algorithms algos = new Algorithms();
        Object[] DP1 =  algos.pickAlgorithm(0, sampleData1Values, sampleData1Weights, sampleData1Max);
        Object[] GREEDY1 = algos.pickAlgorithm(1, sampleData1Values, sampleData1Weights, sampleData1Max);
        Object[] DP2 =  algos.pickAlgorithm(0, sampleData2Values, sampleData2Weights, sampleData2Max);
        Object[] GREEDY2 = algos.pickAlgorithm(1, sampleData2Values, sampleData2Weights, sampleData2Max);
        Object[] DP3 =  algos.pickAlgorithm(0, randomItemValues, randomItemWeights, randomItemMax);
        Object[] GREEDY3 = algos.pickAlgorithm(1, randomItemValues, randomItemWeights, randomItemMax);
        System.out.printf("%-10s%-10s%-88s%-10s\n", "", "0/1 Knapsack", "", "Fractional Knapsack");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-10s|%-10s|%-55s|%-10s|%-10s     *|*     %-10s|%-55s|%-10s|%-10s\n", "", "Result", "Included Items", "Time Comp.", "Exec. Time", "Result", "Included Items", "Time Comp.", "Exec. Time");
        System.out.printf("%-10s|%-10s|%-55s|%-10s|%-10s     *|*     %-10s|%-55s|%-10s|%-10s\n", "DataSet 1", DP1[0], DP1[3], "O(n * W)", DP1[1], GREEDY1[0], GREEDY1[2], "O(nlogn)", GREEDY1[1]);
        System.out.printf("%-10s|%-10s|%-55s|%-10s|%-10s     *|*     %-10s|%-55s|%-10s|%-10s\n", "DataSet 2", DP2[0], DP2[3], "O(n * W)", DP2[1], GREEDY2[0], GREEDY2[2], "O(nlogn)", GREEDY2[1]);
        System.out.printf("%-10s|%-10s|%-55s|%-10s|%-10s     *|*     %-10s|%-55s|%-10s|%-10s\n", "Rndm Vals", DP3[0], DP3[3], "O(n * W)", DP3[1], GREEDY3[0], GREEDY3[2], "O(nlogn)", GREEDY3[1]);

    }
}