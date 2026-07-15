package Practice.DSA.MajorityElement;

public class MajorityElement {
    // Boyer-Moore Voting Algorithm
    public int majorityElement(int[] nums) {
        int ans = 0;
        int count = 0;
        for(int num : nums) {
            if(count == 0) {
                // set the majority element
                ans = num;
            }

            if(num == ans) {
                // we found the majority ele, increase the count
                count++;
            } else {
                // If any other element, decrease the count
                count--;
            }
        }

        return ans;
    }
}
