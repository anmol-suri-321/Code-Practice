package DSA.MajorityElement;

import java.util.ArrayList;
import java.util.List;

public class MajorityElementII {
    // Boyer-Moore Voting Algorithm for More Than One Majority Element:
    public List<Integer> majorityElement(int[] nums) {
        int n = nums.length;
        int maj1 = 0; int maj2 = 0;
        int count1 = 0; int count2 = 0;
        for(int num : nums) {
            if(num == maj1) {
                // if it matches first maj element, increment its count
                count1++;
            } else if(num == maj2) {
                // if it matches second maj element, increment its count
                count2++;
            } else if(count1 == 0) {
                // set the first majority element
                maj1 = num;
                count1 = 1;
            } else if(count2 == 0) {
                // set the second majority element
                maj2 = num;
                count2 = 1;
            } else {
                // any other element came, decrement both counts
                count1--;
                count2--;
            }
        }

        count1 = 0; count2 = 0;
        for(int num : nums) {
            if(num == maj1) count1++;
            else if(num == maj2) count2++;
        }

        List<Integer> ans = new ArrayList<>();
        int thresh = n/3;

        if(count1 > thresh) ans.add(maj1);
        if(count2 > thresh) ans.add(maj2);

        return ans;
    }
}
