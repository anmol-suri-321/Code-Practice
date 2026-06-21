package DSA;

public class ValidIPAddress {
    public String validIPAddress(String str) {
        String[] partsIPV4 = str.split("\\.", -1);
        String[] partsIPV6 = str.split("\\:", -1);

        if (partsIPV4.length == 4) {
            if (isIPV4(partsIPV4))
                return "IPv4";
        } else if (partsIPV6.length == 8) {
            if (isIPV6(partsIPV6))
                return "IPv6";
        }
        return "Neither";
    }

    boolean isIPV4(String[] parts) {
        for (String s : parts) {
            // Check if the part is empty or has leading zeros
            if (s.isEmpty() || (s.length() > 1 && s.startsWith("0"))) {
                return false;
            }
            
            // Try to parse the part to an integer
            try {
                int val = Integer.parseInt(s);
                if (val < 0 || val > 255) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false; // Not a valid integer
            }
        }
        return true;
    }

    boolean isIPV6(String[] parts) {
        for (String s : parts) {
            // Each part should have a length of 1 to 4
            if (s.length() < 1 || s.length() > 4) {
                return false;
            }
            
            // Check if the part contains only valid hexadecimal characters
            for (char c : s.toCharArray()) {
                if (!isHexChar(c)) {
                    return false;
                }
            }
        }
        return true;
    }

    // Helper function to check if a character is a valid hexadecimal character
    boolean isHexChar(char c) {
        return (c >= '0' && c <= '9') || 
               (c >= 'a' && c <= 'f') || 
               (c >= 'A' && c <= 'F');
    }
}
