/***************************************************
 * Title: Assignment-2 (Main file)
 * Name: Jeel Patel
 * Sub: OOP-1
 * Date: 14th February 2024
 * Description: Dart Game
 ****************************************************/

// Start of the code

// Importing library for additional use
import java.util.Random;

public class DartGame {
    public static void main(String[] args) {
        playDartGame();
    }

    // Method for playing Game
    public static void playDartGame() {
        // Display welcome message
        System.out.println("******************************************");
        System.out.println("******** Welcome to the DART Game ********");
        System.out.println("******************************************");

        // Get names of players from the user
        String[] players = new String[2];
        players[0] = Tools.getStringFromConsole("Enter the name of the first player: ");
        players[1] = Tools.getStringFromConsole("Enter the name of the second player: ");

        // Arrays to keep track of rounds won and round scores
        int[] roundsWon = {0, 0};
        int[][] roundScores = new int[3][2]; // 3 rounds, 2 players

        // Loop through 3 rounds
        for (int round = 1; round <= 3; round++) {
            System.out.println("*************** Round " + round + " ******************");

            // Shuffle players at the start of each round
            Random random = new Random();
            if (random.nextBoolean()) {
                // Swap players
                String temp = players[0];
                players[0] = players[1];
                players[1] = temp;
            }

            System.out.println(players[0] + " vs " + players[1]);

            // Initial total scores for each player
            int[] totalScores = {501, 501};

            // Continue the round until one player reaches 0
            while (totalScores[0] > 0 && totalScores[1] > 0) {
                // Player 1's turn
                totalScores[0] = playTurn(players[0], round, totalScores[0]);

                // Player 2's turn
                totalScores[1] = playTurn(players[1], round, totalScores[1]);
            }

            // Determine the winner of the round
            if (totalScores[0] == 0) {
                System.out.println(players[0] + " wins Round " + round + "!");
                roundsWon[0]++;
                displayGameStats(players[0], roundScores, 0);
            } else if (totalScores[1] == 0) {
                System.out.println(players[1] + " wins Round " + round + "!");
                roundsWon[1]++;
                displayGameStats(players[1], roundScores, 1);
            } else {
                System.out.println("Round " + round + " is a tie!");
            }

            // Store scores for each player in the 2D array
            roundScores[round - 1][0] = 501 - totalScores[0];
            roundScores[round - 1][1] = 501 - totalScores[1];
        }

        // Determine the overall winner
        determineOverallWinner(players[0], roundsWon[0], players[1], roundsWon[1], roundScores);
    }

    // Method to change the turn of the player
    public static int playTurn(String currentPlayer, int round, int currentScore) {
        System.out.println("******************************************");
        System.out.println(currentPlayer + "'s turn for Round " + round + ":");

        int throwTotal = 0;
        boolean roundWon = false;

        // Loop through 3 throws per turn
        for (int throwNumber = 1; throwNumber <= 3 && !roundWon; throwNumber++) {
            int throwScore;
            do {
                // Get the score for the throw
                throwScore = getScore(currentPlayer, round, throwNumber, throwTotal, currentScore);
                System.out.printf("%s, throws dart %d, score: %d%n", currentPlayer, throwNumber, throwScore);
            } while (throwScore > 60 || throwScore < 0);

            throwTotal += throwScore;

            // Check if the total would go below 0, and skip the throw if so
            if (throwTotal > 180 || currentScore - throwScore < 0) {
                System.out.println("Skipping this throw. Total would go below 0.");
                continue;  // Skip subtracting from total and move to the next throw
            }

            // Subtract the throw score from the total score
            currentScore -= throwScore;
            System.out.printf("%s's total score: %d%n", currentPlayer, currentScore);

            // Check if the player reaches 0 and wins the round
            if (currentScore == 0) {
                roundWon = true;
                System.out.println(currentPlayer + " reaches 0! Exiting the round.");
            }
        }

        return currentScore;
    }

    // Method to ask the user to enter the score
    public static int getScore(String playerName, int round, int throwNumber, int throwTotal, int currentScore) {
        int score;
        do {
            // Get the score for the throw from the user
            System.out.printf("%s, enter the score for Round %d, Dart Throw %d (Remaining Total: %d): ", playerName, round, throwNumber, currentScore);
            score = Tools.getIntFromConsole(" ");

            // Validate the input score
            if (score < 0 || score > 60 || throwTotal + score > 180) {
                System.out.println("Invalid input. Score must be between 0 and 60, and the total for the round must not exceed 180.");
            }
        } while (score < 0 || score > 60 || throwTotal + score > 180);

        return score;
    }

    // Method to determine the overall winner
    public static void determineOverallWinner(String player1, int roundsWon1, String player2, int roundsWon2, int[][] roundScores) {
        System.out.println("******************************************");
        System.out.println("*************** Game Results **************");
        System.out.println("******************************************");
        System.out.println(player1 + " won " + roundsWon1 + " rounds.");
        System.out.println(player2 + " won " + roundsWon2 + " rounds.");

        // Determine the overall winner
        if (roundsWon1 > roundsWon2) {
            System.out.println(player1 + " wins the game!");
            displayGameStats(player1, roundScores, 0);
        } else if (roundsWon1 < roundsWon2) {
            System.out.println(player2 + " wins the game!");
            displayGameStats(player2, roundScores, 1);
        } else {
            System.out.println("It's a tie!");
        }
    }

    // Method to display the game states
    public static void displayGameStats(String playerName, int[][] roundScores, int playerIndex) {
        int totalScore = 0;
        int totalThrows = 0;
        int roundsWon = 0;

        // Loop through rounds to calculate total score and throws
        for (int roundIndex = 0; roundIndex < roundScores.length; roundIndex++) {
            int[] roundScore = roundScores[roundIndex];
            int roundScorePlayerIndex = playerIndex == 0 ? 0 : 1;

            // Check if the player won the round
            if (roundScore[roundScorePlayerIndex] == 0) {
                roundsWon++;
                totalScore += 501 - roundScore[1 - roundScorePlayerIndex];
            } else {
                totalScore += roundScore[roundScorePlayerIndex];
            }
            totalThrows += 3;
        }

        // Calculate the final average score per 3 darts
        double finalAverage = totalThrows > 0 ? totalScore / (double) (roundsWon * 3) : 0;

        // Display game stats
        System.out.println("************ Game Stats for " + playerName + " *************");
        System.out.println(playerName + " won " + roundsWon + " rounds.");
        System.out.println(playerName + " took " + totalThrows + " throws to win the game.");
        System.out.printf("%s's average score per 3 dart: %.2f%n", playerName, finalAverage);
    }
}

// End of the code
