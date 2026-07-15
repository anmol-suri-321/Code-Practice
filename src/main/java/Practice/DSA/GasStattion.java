package Practice.DSA;

public class GasStattion {
    public int canCompleteCircuit(int[] gas, int[] cost) {
        int totalGas = 0; // Total gas available
        int totalCost = 0; // Total gas required
        int currentGas = 0; // Gas remaining during the trip
        int start = 0; // Starting gas station

        for (int i = 0; i < gas.length; i++) {
            totalGas += gas[i];
            totalCost += cost[i];
            currentGas += gas[i] - cost[i];

            // If at any point, currentGas is negative, we can't start from 'start'
            if (currentGas < 0) {
                start = i + 1; // Start from the next station
                currentGas = 0; // Reset currentGas for the new start
            }
        }

        // If total gas is greater than or equal to total cost, we can complete the
        // circuit
        return (totalGas >= totalCost) ? start : -1;
    }
}
