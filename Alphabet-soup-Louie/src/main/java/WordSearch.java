import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;

/**
 * This class accepts a file input and loads in a grid with scrambled words embedded within it and a word list of words
 * to find. Then, it outputs the starting and ending locations of each word found in the grid.
 */
public class WordSearch {
    private static final String sampleTextFile = "src/main/resources/helper.txt";
    private static final Set<String> hiddenWords = new HashSet<>();

    /**
     * This is the main method of the program that calls the loadInputFile method and the searchWord method
     * for each word to be found.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        char[][] grid = loadInputFile(sampleTextFile);
        if (grid == null) {
            return;
        }
        for (String word : hiddenWords) {
            String output = searchWord(grid, word);
            System.out.println(output);
        }
        clearHiddenWords();
    }

    /**
     * Reads the input file and loads the grid of the specified size and lists the words to be found.
     *
     * @param textFile The input file that will be read.
     * @return The 2D character grid with scrambled words embedded within it that was created from the input.
     * @throws IllegalArgumentException If the input file contains invalid grid dimensions or no words to find.
     */
    public static char[][] loadInputFile(String textFile) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(textFile));
            String gridSizeInput = br.readLine();
            String[] gridSize = gridSizeInput.split("x");
            int rows = Integer.parseInt(gridSize[0]);
            int columns = Integer.parseInt(gridSize[1]);
//            System.out.println(rows + "x" + columns);     //tests row and column lengths

            if (rows <= 0 || columns <= 0) {
                throw new IllegalArgumentException("Grid size is not valid");
            }

            char[][] grid = new char[rows][columns];

            for (int i = 0; i < rows; i++) {
                String rowInput = br.readLine();
                String[] rowChars = rowInput.split(" ");
                for (int j = 0; j < columns; j++) {
                    if (!Character.isLetter(rowChars[j].charAt(0))) {
                        throw new IllegalArgumentException("The grid inputs should only contain alphabetical characters");
                    }
                    grid[i][j] = Character.toUpperCase(rowChars[j].charAt(0));
//                    System.out.print(grid[i][j]);     //tests grid inputs
                }
//                System.out.print("\n");     //simulates each row
            }

            String wordInput;
            while ((wordInput = br.readLine()) != null) {
                hiddenWords.add(wordInput);
//                System.out.println(wordInput);      //tests word inputs
            }
            if (hiddenWords.contains("")) {
                hiddenWords.remove("");
            }
            if (hiddenWords.isEmpty()) {
                throw new IllegalArgumentException("There are no hidden words to search for");
            }
            return grid;

        } catch (IOException e) {
            e.printStackTrace();

        } catch (NumberFormatException e) {
            System.out.println("The input file contains invalid row and column dimensions");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Searches the grid for the specified word in various directions: vertically, horizontally, diagonally,
     * forwards or backwards.
     *
     * @param grid The grid with scrambled words embedded within it.
     * @param word Word that is being searched for.
     * @return String that indicates the starting and ending indices of the word found within the grid.
     * @throws IllegalArgumentException If it is not possible for the word to exist within the grid.
     */
    public static String searchWord(char[][] grid, String word) {
        int length = word.length();
        int rows = grid.length;
        int columns = grid[0].length;
        if (length > rows && length > columns) {
            throw new IllegalArgumentException("Not possible for " + word + " to exist in grid");
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (grid[i][j] != Character.toUpperCase(word.charAt(0))) {
                    continue;
                }
//                System.out.println(grid[i][j]);

                if (length <= j + 1) {
//                    System.out.println("left");
                    if (wordExists(grid, word, i, j, 0, -1, length, 0)) {
                        return word + " " + i + ":" + j + " " + i + ":" + (j - (length - 1));
                    }
                }
                if (length <= columns - j) {
//                    System.out.println("right");
                    if (wordExists(grid, word, i, j, 0, 1, length, 0)) {
                        return word + " " + i + ":" + j + " " + i + ":" + (j + (length - 1));
                    }
                }
                if (length <= i + 1) {
//                    System.out.println("up");
                    if (wordExists(grid, word, i, j, -1, 0, length, 0)) {
                        return word + " " + i + ":" + j + " " + (i - (length - 1)) + ":" + j;
                    }
                }
                if (length <= rows - i) {
//                    System.out.println("down");
                    if (wordExists(grid, word, i, j, 1, 0, length, 0)) {
                        return word + " " + i + ":" + j + " " + (i + (length - 1)) + ":" + j;
                    }
                }
                if (length <= j + 1 && length <= i + 1) {
//                    System.out.println("up-left");
                    if (wordExists(grid, word, i, j, -1, -1, length, 0)) {
                        return word + " " + i + ":" + j + " " + (i - (length - 1)) + ":" + (j - (length - 1));
                    }
                }
                if (length <= columns - j && length <= i + 1) {
//                    System.out.println("up-right");
                    if (wordExists(grid, word, i, j, -1, 1, length, 0)) {
                        return word + " " + i + ":" + j + " " + (i - (length - 1)) + ":" + (j + (length - 1));
                    }
                }
                if (length <= j + 1 && length <= rows - i) {
//                    System.out.println("down-left");
                    if (wordExists(grid, word, i, j, 1, -1, length, 0)) {
                        return word + " " + i + ":" + j + " " + (i + (length - 1)) + ":" + (j - (length - 1));
                    }
                }
                if (length <= columns - j && length <= rows - i) {
//                    System.out.println("down-right");
                    if (wordExists(grid, word, i, j, 1, 1, length, 0)) {
                        return word + " " + i + ":" + j + " " + (i + (length - 1)) + ":" + (j + (length - 1));
                    }
                }

            }
        }
        return word + " doesn't exist in the grid";
    }

    /**
     * Recursive method that uses offsets to search if a word exists in the grid in the specific direction.
     *
     * @param grid 2D grid containing embedded words to search for.
     * @param word Word to search for.
     * @param row Current row that is being checked.
     * @param column Current column that is being checked.
     * @param rowOffset Row offset that will be used to keep checking in the specific direction.
     * @param columnOffset Column offset that will be used to keep checking in the specific direction.
     * @param length Length of the word being searched for.
     * @param count Index of the current character of the word being searched for.
     * @return Boolean of whether the word was found in the grid.
     */
    public static boolean wordExists(char[][] grid, String word, int row, int column, int rowOffset, int columnOffset, int length, int count) {
        if (count == length) {
            return true;
        }

        if (row < 0 || row >= grid.length || column < 0 || column >= grid[0].length || grid[row][column] != Character.toUpperCase(word.charAt(count))) {
            return false;
        }

        return wordExists(grid, word, (row + rowOffset), (column + columnOffset), rowOffset, columnOffset, length, count + 1);
    }

    /**
     * Used for testing. Since hiddenWords is static, it persists throughout the test cases.
     */
    public static void clearHiddenWords() {
        hiddenWords.clear();
    }
}
