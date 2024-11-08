import java.io.*;

/**
 * You just finished day one of your alchemy class! For your alchemy homework,
 * you have been given a string of lowercase letters and wish to make it a
 * palindrome. You’re only a beginner at alchemy though, so your powers are
 * limited. In a single operation, you may choose exactly two adjacent letters
 * and change each of them into a different lowercase letter. The resulting
 * characters may be the same as or different from one another, so long as they
 * were both changed by the operation.
 * 
 * Formally, if the string before the operation is s
 * and you chose to change characters s_i and s_{i+1}
 * to produce string t, then s_i != t_i and s_{i+1} != t_{i+1}
 * must be true, but t_i = t_{i+1} is permitted.
 * 
 * Compute the minimum number of operations needed to make the string a
 * palindrome.
 * 
 * Input:
 * The single line of input contains a string of lowercase letters, the string
 * you are converting into a palindrome.
 * 
 * Output:
 * Output a single integer, which is the minimum number of operations needed to
 * make the string a palindrome.
 * 
 * Link:
 * https://open.kattis.com/problems/alchemy2
 */
public class Alchemy {

    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        String word = reader.readLine().trim();
        int n = word.length();
        if (n == 1) {
            System.out.println(0);
        }

        // If I'm at index i in the left half, and I start off matching my palindromatic
        // position l-i-1 in the right, what's the minimum number of operations to make
        // s[i:l-i] a palindrome?
        int[] minMovesGivenMatch = new int[n / 2];
        minMovesGivenMatch[minMovesGivenMatch.length - 1] = 0;
        for (int i = 0; i < minMovesGivenMatch.length - 1; i++) {
            minMovesGivenMatch[i] = -1;
        }

        // If I'm at index i in the left half, and I start off NOT matching my
        // palindromatic position l-i-1 in the right, what's the minimum number of
        // operations to make s[i:l-i] a palindrome?
        int[] minMovesGivenMismatch = new int[n / 2];
        minMovesGivenMismatch[minMovesGivenMatch.length - 1] = 1;
        for (int i = 0; i < minMovesGivenMismatch.length - 1; i++) {
            minMovesGivenMismatch[i] = -1;
        }

        System.out.println((word.charAt(0) == word.charAt(n - 1))
                ? topDownMatch(0, minMovesGivenMatch, minMovesGivenMismatch, word)
                : topDownMismatch(0, minMovesGivenMatch, minMovesGivenMismatch, word));
    }

    /**
     * Helper method to answer the question of the minimum number of moves given a
     * match on the character
     * 
     * @param idx
     * @param minMovesGivenMatch
     * @param minMovesGivenMismatch
     * @param word
     * @return int
     */
    static int topDownMatch(int idx, int[] minMovesGivenMatch, int[] minMovesGivenMismatch, String word) {
        if (minMovesGivenMatch[idx] == -1) {
            // Need to solve this problem
            // We need to see if word[idx+1] matches word[length - (idx + 1) - 1], its
            // palindromatic twin
            int record = Integer.MAX_VALUE;
            if (word.charAt(idx + 1) == word.charAt(word.length() - (idx + 1) - 1)) {
                // NEXT IS MATCH
                // MX..XM (DONE)
                record = Math.min(record,
                        topDownMatch(idx + 1, minMovesGivenMatch, minMovesGivenMismatch, word));
                // MX..XM -> AB..XM -> AB..CA (DONE)
                record = Math.min(record,
                        2 + topDownMismatch(idx + 1, minMovesGivenMatch, minMovesGivenMismatch, word));
            } else {
                // NEXT IS MISMATCH
                // MX..YM (DONE)
                record = Math.min(record, topDownMismatch(idx + 1, minMovesGivenMatch, minMovesGivenMismatch, word));
                // MX..YM -> AB..YM -> AB..BA (DONE)
                record = Math.min(record,
                        2 + topDownMatch(idx + 1, minMovesGivenMatch, minMovesGivenMismatch, word));
            }
            minMovesGivenMatch[idx] = record;
        }
        return minMovesGivenMatch[idx];
    }

    /**
     * Helper method to answer the question of the minimum number of moves given a
     * mismatch on the character
     * 
     * @param idx
     * @param minMovesGivenMatch
     * @param minMovesGivenMismatch
     * @param word
     * @return int
     */
    static int topDownMismatch(int idx, int[] minMovesGivenMatch, int[] minMovesGivenMismatch, String word) {
        if (minMovesGivenMismatch[idx] == -1) {
            // Need to solve this problem
            // We need to see if word[idx+1] matches word[length - (idx + 1) - 1], its
            // palindromatic twin
            int record = Integer.MAX_VALUE;
            if (word.charAt(idx + 1) == word.charAt(word.length() - (idx + 1) - 1)) {
                // NEXT IS MATCH
                // NX..XM -> MY..XM (DONE)
                record = Math.min(record,
                        1 + topDownMismatch(idx + 1, minMovesGivenMatch, minMovesGivenMismatch, word));
                // NX..XM -> AB..XM -> MX..XM (DONE)
                record = Math.min(record,
                        2 + topDownMatch(idx + 1, minMovesGivenMatch, minMovesGivenMismatch, word));
            } else {
                // NEXT IS MISMATCH
                // NX..YM -> MY..YM (DONE)
                record = Math.min(record,
                        1 + topDownMatch(idx + 1, minMovesGivenMatch, minMovesGivenMismatch, word));
                // NX..YM -> MA..YM (DONE)
                record = Math.min(record,
                        1 + topDownMismatch(idx + 1, minMovesGivenMatch, minMovesGivenMismatch, word));
            }
            minMovesGivenMismatch[idx] = record;
        }
        return minMovesGivenMismatch[idx];
    }

}