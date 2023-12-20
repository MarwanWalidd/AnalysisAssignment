public class GeneAlignment {

    public static double[][] calculateAlignment(String x, String y, double[][] scoringMatrix) {
        int m = x.length();
        int n = y.length();

        double[][] dp = new double[m + 1][n + 1];

        // Initialize the first row and column
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i * scoringMatrix[4][0]; // Gap penalty
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j * scoringMatrix[0][4]; // Gap penalty
        }

        // Fill in the dynamic programming table
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                double match = dp[i - 1][j - 1] + scoringMatrix[getIndex(x.charAt(i - 1))][getIndex(y.charAt(j - 1))];
                double delete = dp[i - 1][j] + scoringMatrix[getIndex(x.charAt(i - 1))][4]; // Gap penalty
                double insert = dp[i][j - 1] + scoringMatrix[4][getIndex(y.charAt(j - 1))]; // Gap penalty

                dp[i][j] = Math.max(Math.max(match, delete), insert);
            }
        }

        return dp;
    }

    public static String[] getAlignment(String x, String y, double[][] dp, double[][] scoringMatrix) {
        int i = x.length();
        int j = y.length();
        StringBuilder alignedX = new StringBuilder();
        StringBuilder alignedY = new StringBuilder();

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + scoringMatrix[getIndex(x.charAt(i - 1))][getIndex(y.charAt(j - 1))]) {
                alignedX.insert(0, x.charAt(i - 1));
                alignedY.insert(0, y.charAt(j - 1));
                i--;
                j--;
            } else if (i > 0 && dp[i][j] == dp[i - 1][j] + scoringMatrix[getIndex(x.charAt(i - 1))][4]) {
                alignedX.insert(0, x.charAt(i - 1));
                alignedY.insert(0, '-');
                i--;
            } else {
                alignedX.insert(0, '-');
                alignedY.insert(0, y.charAt(j - 1));
                j--;
            }
        }

        return new String[]{alignedX.toString(), alignedY.toString()};
    }

    private static int getIndex(char nucleotide) {
        switch (nucleotide) {
            case 'A':
                return 0;
            case 'G':
                return 1;
            case 'T':
                return 2;
            case 'C':
                return 3;
            default:
                return 4; // Gap
        }
    }

    public static double calculateAlignmentScore(String alignedX, String alignedY, double[][] scoringMatrix) {
        double score = 0.0;

        for (int i = 0; i < alignedX.length(); i++) {
            char charX = alignedX.charAt(i);
            char charY = alignedY.charAt(i);

            int indexX = getIndex(charX);
            int indexY = getIndex(charY);

            score += scoringMatrix[indexX][indexY];
        }

        return score;
    }

    public static void main(String[] args) {
        String x = "TCCCAGTTATGTCAGGGGACACGAGCATGCAGAGAC";
        String y = "AATTGCCGCCGTCGTTTTCAGCAGTTATGTCAGATC";

        double[][] scoringMatrix = {
                {1, -0.8, -0.2, -2.3, -0.6}, // A
                {-0.8, 1.0, -1.1, -0.7, -1.5}, // G
                {-0.2, -1.1, 1.0, -0.5, -0.9}, // T
                {-2.3, -0.7, -0.5, 1.0, -1.0}, // C
                {-0.6, -1.5, -0.9, -1.0, -10000000.0}, // Gap
        };

        double[][] dp = calculateAlignment(x, y, scoringMatrix);

        String[] alignment = getAlignment(x, y, dp, scoringMatrix);

        System.out.println("Aligned X: " + alignment[0]);
        System.out.println("Aligned Y: " + alignment[1]);

        double alignmentScore = calculateAlignmentScore(alignment[0], alignment[1], scoringMatrix);
        System.out.println("Alignment Score: " + alignmentScore);
    }
}
