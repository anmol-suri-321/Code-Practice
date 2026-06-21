package DSA;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CheapestFlightsWithinKStops {
    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        // Early exit if source and destination are the same
        if (src == dst) {
            return 0;
        }
        List<List<Pair> > g = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            g.add(new ArrayList<>());
        }

        for(int[] arr : flights) {
            g.get(arr[0]).add(new Pair(arr[1], arr[2]));
        }

        // Create an array to store the minimum cost to reach each city
        int[] minCost = new int[n];
        for (int i = 0; i < n; i++) {
            minCost[i] = Integer.MAX_VALUE;  // Initially, set all costs to max
        }
        
        Queue<int[]> q = new LinkedList<>();
        // queue stores curr city, cost to reach, stops to reach it
        q.offer(new int[]{src, 0, 0});

        while(!q.isEmpty()) {
            int[] front = q.poll();
            int city = front[0];
            int cost = front[1]; 
            int stops = front[2];
            if(stops > k) continue;

            for(Pair p : g.get(city)) {
                int nextCity = p.dest;
                int nextCost = p.cost;

                // If the cost to reach the neighbor is less than already stores cost in the array
                if(cost + nextCost < minCost[nextCity]) {
                    minCost[nextCity] = cost + nextCost;
                    q.offer(new int[]{nextCity, cost + nextCost, stops+1});
                }
            }
        }

        return minCost[dst] == Integer.MAX_VALUE ? -1 : minCost[dst];
    }
}

class Pair {
    int dest;
    int cost;

    Pair(int dest, int cost) {
        this.dest = dest;
        this.cost = cost;
    }
}
