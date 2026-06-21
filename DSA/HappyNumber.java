package DSA;

import java.util.HashSet;
import java.util.Set;

public class HappyNumber {
    public boolean isHappy(int n) {
        Set<Integer> hash = new HashSet<>();
        while(!hash.contains(n)) {
            hash.add(n);
            n = sumOfSquares(n);
            if(n == 1) {
                return true;
            }
        }
        return false;
    }

    int sumOfSquares(int n) {
        int sum = 0;
        while(n > 0) {
            int rem = n % 10;
            sum += rem*rem;
            n /= 10;
        }
        return sum;
    }
}
