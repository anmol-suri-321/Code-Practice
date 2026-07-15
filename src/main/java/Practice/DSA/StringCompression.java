package Practice.DSA;

public class StringCompression {
    public int compress(char[] chars) {
        int n = chars.length;
        String s = "";
        int count = 1;
        for(int i = 0; i < n; i++) {
            if(i != n-1 && chars[i] == chars[i+1]) {
                count++;
                continue;
            } else {
                s += chars[i];
                if(count > 1) {
                    String c = Integer.toString(count);
                    for(int j = 0; j < c.length(); j++) {
                        s += c.charAt(j);
                    }
                    count = 1;
                }
            }
        }
        System.out.println("String: " + s);
        for(int i = 0; i < s.length(); i++) {
            chars[i] = s.charAt(i);
        }
        return s.length();
    }
}
