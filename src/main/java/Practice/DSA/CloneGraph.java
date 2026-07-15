package Practice.DSA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Definition for a Node.
class Node {
    public int val;
    public List<Node> neighbors;
    public Node() {
        val = 0;
        neighbors = new ArrayList<Node>();
    }
    public Node(int _val) {
        val = _val;
        neighbors = new ArrayList<Node>();
    }
    public Node(int _val, ArrayList<Node> _neighbors) {
        val = _val;
        neighbors = _neighbors;
    }
}

public class CloneGraph {
    private Map<Node, Node> visited = new HashMap<> ();
    public Node cloneGraph(Node node) {
        // Base case: if the input node is null
        if (node == null) {
            return null;
        }
        
        // If the node has already been cloned, return the clone from the visited map
        if (visited.containsKey(node)) {
            return visited.get(node);
        }
        
        // Create a new node (clone) with the same value as the original node
        Node cloneNode = new Node(node.val);
        
        // Add the node to the visited map to avoid cycles
        visited.put(node, cloneNode);
        
        // Recursively clone the neighbors
        for (Node neighbor : node.neighbors) {
            cloneNode.neighbors.add(cloneGraph(neighbor));
        }
        
        return cloneNode;
    }
}
