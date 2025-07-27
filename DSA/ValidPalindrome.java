public class ValidPalindrome {
    public boolean isPalindrome(String s) {
        String s_lower = s.toLowerCase();
        int n = s.length();
        int i = 0; int j = n - 1;
        while(i < j) {
            while(i < j && !Character.isLetterOrDigit(s_lower.charAt(i))) {
                i++;
            }
            while(i < j && !Character.isLetterOrDigit(s_lower.charAt(j))) {
                j--;
            }
            if(s_lower.charAt(i) != s_lower.charAt(j)) {
                return false;
            }
            i++;
            j--;
        }
        return true;
    }
}
