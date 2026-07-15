package Practice.DSA;

public class MaxPointsOnALine {
    public int maxPoints(int[][] p) {
        int n = p.length;
        if(n <= 2) return n;

        int maxPoints = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int points = 2;
                for (int k = 0; k < n; k++) {
                    if (k != i && k != j) {
                        if ((p[j][1] - p[k][1]) * (p[i][0] - p[k][0]) == 
                                (p[j][0] - p[k][0]) * (p[i][1] - p[k][1])) {
                            points++;
                        }
                    }
                }
                maxPoints = Math.max(maxPoints, points);
            }
        }

        return maxPoints;
    }
}