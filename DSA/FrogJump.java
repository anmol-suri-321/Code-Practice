package DSA;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FrogJump {
    public boolean canCross(int[] stones) {
        // Base case: if the second stone is not at position 1, frog cannot jump
        if (stones[1] != 1) return false;

        // Map to store possible jump sizes at each stone
        // key: stores the stone
        // value: stores the possible jump sizes to reach that index
        Map<Integer, Set<Integer>> dp = new HashMap<>();
        for (int stone : stones) {
            dp.put(stone, new HashSet<>());
        }
        dp.get(0).add(0); // The first stone starts with a jump of 0

        for (int stone : stones) {
            for (int jump : dp.get(stone)) {
                // Try jumps of size jump-1, jump, jump+1
                for (int step = jump - 1; step <= jump + 1; step++) {
                    if (step > 0 && dp.containsKey(stone + step)) {
                        dp.get(stone + step).add(step);
                    }
                }
            }
        }

        // Check if the last stone has any reachable jumps
        return !dp.get(stones[stones.length - 1]).isEmpty();
    }
}
