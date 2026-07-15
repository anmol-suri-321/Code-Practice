package Practice.DSA.NextGreaterElement;

import java.util.Arrays;

public class NextGreaterElementIII {
    public int nextGreaterElement(int n) {
        // Convert integer to a character array
        char[] digits = String.valueOf(n).toCharArray();
        
        int index = -1;
        
        // Step 1: Find the first digit that is smaller than the digit to its right
        for (int i = digits.length - 1; i > 0; i--) {
            if (digits[i] > digits[i - 1]) {
                index = i - 1;
                break;
            }
        }

        // If no such digit is found, the number is already the largest permutation
        if (index == -1) return -1;

        // Step 2: Find the smallest digit on the right side that is larger than digits[index]
        int minGreaterIndex = -1;
        for (int i = index + 1; i < digits.length; i++) {
            if (digits[i] > digits[index]) {
                if (minGreaterIndex == -1 || digits[i] < digits[minGreaterIndex]) {
                    minGreaterIndex = i;
                }
            }
        }

        // Step 3: Swap digits[index] and digits[minGreaterIndex]
        swap(digits, index, minGreaterIndex);

        // Step 4: Sort the digits after the swap position (index + 1) in ascending order
        Arrays.sort(digits, index + 1, digits.length);

        // Convert the character array back to a number
        long result = Long.parseLong(new String(digits));

        // If the result is greater than the maximum value for an int, return -1
        return (result > Integer.MAX_VALUE) ? -1 : (int) result;
    }

    // Helper method to swap two characters in the char array
    private void swap(char[] digits, int i, int j) {
        char temp = digits[i];
        digits[i] = digits[j];
        digits[j] = temp;
    }
}
