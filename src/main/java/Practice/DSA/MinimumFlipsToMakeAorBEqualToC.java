package Practice.DSA;

public class MinimumFlipsToMakeAorBEqualToC {
// In this problem, learn how to extract the bit at ith index in 32 bit integer and traverse
    public int minFlips(int a, int b, int c) {

        int flipCount = 0;
        for(int i = 0; i < 32; i++) {
            int A = (a >> i) & 1;
            int B = (b >> i) & 1;
            int C = (c >> i) & 1;
            // if ith bit of c is 0
            if(C == 0) {
                // both bits in a and b must be 0
                flipCount += A + B;
            } else {
                // either of the bit in a and b, will make OR operation to 1
                flipCount += A == 1 || B == 1 ? 0 : 1;
            }
        }

        return flipCount;
    }
}