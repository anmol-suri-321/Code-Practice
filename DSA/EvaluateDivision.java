package DSA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EvaluateDivision {
    double dfs(
        Map<String, List<PairElement>> G, String u, String target, Set<String> vis, double ans)
    {
        // This means the target is found, so return the 'ans' accumulated
        if(u.equals(target)) {
            return ans;
        }
        // mark current node as visited, so it will not process again in DFS
        vis.add(u);
        for(PairElement pair : G.get(u)) {
            if(!vis.contains(pair.variable)) {
                double result = dfs(G, pair.variable, target, vis, ans*pair.value);
                // This means that valid path to the target found, so return and end the DFS
                if(result != -1.0) {
                    return result;
                }
            }
        }
        // If no valid path found, then return
        return -1.0;
    }

    public double[] calcEquation(List<List<String>> eq, double[] val, List<List<String>> que) {
        Map<String, List<PairElement>> G = new HashMap<>();
        // Build graph using the equations and values
        for (int i = 0; i < eq.size(); i++) {
            String u = eq.get(i).get(0);
            String v = eq.get(i).get(1);

            // Add u -> v
            G.putIfAbsent(u, new ArrayList<>());
            G.get(u).add(new PairElement(v, val[i]));

            // Add v -> u
            G.putIfAbsent(v, new ArrayList<>());
            G.get(v).add(new PairElement(u, 1.0 / val[i]));
        }

        double[] result = new double[que.size()];

        for (int i = 0; i < que.size(); i++) {
            String u = que.get(i).get(0);
            String v = que.get(i).get(1);
            if (!G.containsKey(u) || !G.containsKey(v)) {
                // This means, there is no edge containing the query pair
                result[i] = -1.0;
            } else {
                Set<String> vis = new HashSet<>();
                double ans = dfs(G, u, v, vis, 1.0);
                // Save the ans value returned by DFS in result array
                result[i] = ans;
            }
        }
        return result;
    }
}

class PairElement {
    String variable;
    double value;

    PairElement(String variable, double value) {
        this.variable = variable;
        this.value = value;
    }
}
