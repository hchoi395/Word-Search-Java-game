import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class WordSearchTest {
    private WordSearch wordSearch;

    @BeforeEach
    public void setUp() {
        wordSearch = new WordSearch();
        WordSearch.clearHiddenWords();
    }

    @Test
    public void testLoadInputFileWithValidFile() {
        Set<String> hiddenWords = new HashSet<>();
        char[][] grid = wordSearch.loadInputFile("src/main/resources/sample_input.txt");
        assertNotNull(grid);
        assertNotNull(hiddenWords);
    }

    @Test
    public void testLoadInputFileWithLowerCaseGrid() {
        Set<String> hiddenWords = new HashSet<>();
        char[][] grid = wordSearch.loadInputFile("src/main/resources/lower_case_grid.txt");
        assertNotNull(grid);
        assertNotNull(hiddenWords);
        char[][] expectedGrid = {
                {'H', 'A', 'S', 'D', 'F'},
                {'G', 'E', 'Y', 'B', 'H'},
                {'J', 'K', 'L', 'Z', 'X'},
                {'C', 'V', 'B', 'L', 'N'},
                {'G', 'O', 'O', 'D', 'O'}
        };

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                assertEquals(Character.toUpperCase(expectedGrid[i][j]), grid[i][j]);
            }
        }
    }

    @Test
    public void testLoadInputFileWithInvalidRows() {
        assertThrows(IllegalArgumentException.class, () -> {
            wordSearch.loadInputFile("src/main/resources/invalid_rows.txt");
        });
    }

    @Test
    public void testLoadInputFileWithInvalidColumns() {
        assertThrows(IllegalArgumentException.class, () -> {
            wordSearch.loadInputFile("src/main/resources/invalid_columns.txt");
        });
    }

    @Test
    public void testLoadInputFileWithNullGrid() {
        assertThrows(IllegalArgumentException.class, () -> {
            wordSearch.loadInputFile("src/main/resources/null_grid.txt");
        });
    }

    @Test
    public void testLoadInputFileWithInvalidFile() throws IOException {
        wordSearch.loadInputFile("src/main/resources/nonexistent_file.txt");
    }

    @Test
    public void testLoadInputFileWithNonNumericalGridSizes() throws NumberFormatException {
        wordSearch.loadInputFile("src/main/resources/non-numerical_grid_size.txt");
    }

    @Test
    public void testLoadInputFileWithNoHiddenWords() {
        assertThrows(IllegalArgumentException.class, () -> {
            wordSearch.loadInputFile("src/main/resources/no_hidden_words.txt");
        });
    }

    @Test
    public void testSearchWordWithLongerHiddenWordLength() {
        char[][] grid = {
                {'B', 'U', 'R', 'G', 'E'},
                {'Q', 'O', 'R', 'B', 'R'},
                {'E', 'E', 'Y', 'L', 'O'},
                {'X', 'E', 'R', 'Z', 'O'},
                {'Y', 'E', 'O', 'J', 'G'}
        };
        String word = ("BURGER");
        assertThrows(IllegalArgumentException.class, () -> {
           wordSearch.searchWord(grid, word);
        });
    }

    @Test
    public void testSearchWordWithValidNonOverlappingCharacters() {
        char[][] grid = {
                {'H', 'E', 'L', 'L', 'O'},
                {'Q', 'O', 'R', 'B', 'D'},
                {'E', 'E', 'Y', 'L', 'O'},
                {'X', 'E', 'R', 'Z', 'O'},
                {'Y', 'E', 'O', 'J', 'G'}
        };
        Set<String> hiddenWords = new HashSet<>();
        hiddenWords.add("HELLO");
        hiddenWords.add("GOOD");
        hiddenWords.add("BYE");

        ArrayList<String> outputs = new ArrayList<>();

        for (String word : hiddenWords) {
            outputs.add(wordSearch.searchWord(grid, word));
        }
        assertEquals("HELLO 0:0 0:4", outputs.get(0));
        assertEquals("GOOD 4:4 1:4", outputs.get(1));
        assertEquals("BYE 1:3 3:1", outputs.get(2));
    }

    @Test
    public void testSearchWordWithValidOverlappingCharacters() {
        char[][] grid = {
                {'G', 'U', 'S', 'L', 'O'},
                {'O', 'O', 'R', 'L', 'A'},
                {'O', 'E', 'L', 'L', 'O'},
                {'D', 'E', 'Y', 'B', 'F'},
                {'H', 'E', 'O', 'J', 'G'}
        };
        Set<String> hiddenWords = new HashSet<>();
        hiddenWords.add("HELLO");
        hiddenWords.add("GOOD");
        hiddenWords.add("BYE");

        ArrayList<String> outputs = new ArrayList<>();

        for (String word : hiddenWords) {
            outputs.add(wordSearch.searchWord(grid, word));
        }
        assertEquals("HELLO 4:0 0:4", outputs.get(0));
        assertEquals("GOOD 0:0 3:0", outputs.get(1));
        assertEquals("BYE 3:3 3:1", outputs.get(2));
    }

    @Test
    public void testSearchWordWithValidRepeatingHiddenWord() {
        char[][] grid = {
                {'H', 'D', 'S', 'L', 'O'},
                {'E', 'B', 'O', 'X', 'A'},
                {'L', 'E', 'Y', 'O', 'O'},
                {'L', 'F', 'Y', 'E', 'G'},
                {'O', 'G', 'O', 'O', 'D'}
        };
        Set<String> hiddenWords = new HashSet<>();
        hiddenWords.add("HELLO");
        hiddenWords.add("GOOD");
        hiddenWords.add("BYE");

        ArrayList<String> outputs = new ArrayList<>();

        for (String word : hiddenWords) {
            outputs.add(wordSearch.searchWord(grid, word));
        }
        assertEquals("HELLO 0:0 4:0", outputs.get(0));
        assertEquals("GOOD 3:4 0:1", outputs.get(1));
        assertEquals("BYE 1:1 3:3", outputs.get(2));
    }

    @Test
    public void testSearchWordWithValidLowerCaseCharacters() {
        char[][] grid = {
                {'g', 'u', 's', 'l', 'o'},
                {'o', 'o', 'r', 'l', 'a'},
                {'o', 'e', 'l', 'l', 'o'},
                {'d', 'e', 'y', 'b', 'f'},
                {'h', 'e', 'o', 'j', 'g'}
        };
        Set<String> hiddenWords = new HashSet<>();
        hiddenWords.add("HELLO");
        hiddenWords.add("GOOD");
        hiddenWords.add("BYE");

        ArrayList<String> outputs = new ArrayList<>();

        for (String word : hiddenWords) {
            outputs.add(wordSearch.searchWord(grid, word));
        }
        assertEquals("HELLO doesn't exist in the grid", outputs.get(0));
        assertEquals("GOOD doesn't exist in the grid", outputs.get(1));
        assertEquals("BYE doesn't exist in the grid", outputs.get(2));
    }

    @Test
    public void testSearchWordWithMissingHiddenWord() {
        char[][] grid = {
                {'I', 'D', 'S', 'L', 'H'},
                {'E', 'B', 'Y', 'E', 'A'},
                {'D', 'E', 'L', 'O', 'O'},
                {'L', 'L', 'Y', 'E', 'C'},
                {'O', 'Q', 'Y', 'O', 'J'}
        };
        Set<String> hiddenWords = new HashSet<>();
        hiddenWords.add("HELLO");
        hiddenWords.add("GOOD");
        hiddenWords.add("BYE");

        ArrayList<String> outputs = new ArrayList<>();

        for (String word : hiddenWords) {
            outputs.add(wordSearch.searchWord(grid, word));
        }
        assertEquals("HELLO 0:4 4:0", outputs.get(0));
        assertEquals("GOOD doesn't exist in the grid", outputs.get(1));
        assertEquals("BYE 1:1 1:3", outputs.get(2));
    }

    @Test
    public void testSearchWordWithLowerCaseHiddenWords() {
        char[][] grid = {
                {'H', 'A', 'S', 'D', 'F'},
                {'G', 'E', 'Y', 'B', 'H'},
                {'J', 'K', 'L', 'Z', 'X'},
                {'C', 'V', 'B', 'L', 'N'},
                {'G', 'O', 'O', 'D', 'O'}
        };
        Set<String> hiddenWords = new HashSet<>();
        hiddenWords.add("hello");
        hiddenWords.add("good");
        hiddenWords.add("bye");

        ArrayList<String> outputs = new ArrayList<>();

        for (String word : hiddenWords) {
            outputs.add(wordSearch.searchWord(grid, word));
        }
        assertEquals("hello 0:0 4:4", outputs.get(0));
        assertEquals("good 4:0 4:3", outputs.get(1));
        assertEquals("bye 1:3 1:1", outputs.get(2));
    }

    @Test
    public void testSearchWordWithMissingAllHiddenWords() {
        char[][] grid = {
                {'I', 'D', 'S', 'L', 'H'},
                {'D', 'B', 'N', 'E', 'A'},
                {'F', 'Y', 'L', 'O', 'O'},
                {'L', 'X', 'Y', 'M', 'C'},
                {'O', 'Q', 'Y', 'O', 'J'}
        };
        Set<String> hiddenWords = new HashSet<>();
        hiddenWords.add("HELLO");
        hiddenWords.add("GOOD");
        hiddenWords.add("BYE");

        ArrayList<String> outputs = new ArrayList<>();

        for (String word : hiddenWords) {
            outputs.add(wordSearch.searchWord(grid, word));
        }
        assertEquals("HELLO doesn't exist in the grid", outputs.get(0));
        assertEquals("GOOD doesn't exist in the grid", outputs.get(1));
        assertEquals("BYE doesn't exist in the grid", outputs.get(2));
    }

    @Test
    public void testSearchWordWithOneXTenDimensions() {
        char[][] grid = {
                {'O', 'L', 'L', 'E', 'H', 'Y', 'E', 'S', 'I', 'R'}
        };
        Set<String> hiddenWords = new HashSet<>();
        hiddenWords.add("HELLO");
        hiddenWords.add("YES");
        hiddenWords.add("SIR");

        ArrayList<String> outputs = new ArrayList<>();

        for (String word : hiddenWords) {
            outputs.add(wordSearch.searchWord(grid, word));
        }
        assertEquals("HELLO 0:4 0:0", outputs.get(0));
        assertEquals("YES 0:5 0:7", outputs.get(1));
        assertEquals("SIR 0:7 0:9", outputs.get(2));
    }

    @Test
    public void testSearchWordWithSevenXThreeDimensions() {
        char[][] grid = {
                {'Y', 'D', 'R'},
                {'L', 'E', 'X'},
                {'I', 'V', 'S'},
                {'P', 'O', 'P'},
                {'P', 'W', 'A'},
                {'A', 'D', 'N'},
                {'H', 'E', 'R'},
        };
        Set<String> hiddenWords = new HashSet<>();
        hiddenWords.add("YES");
        hiddenWords.add("HAPPILY");
        hiddenWords.add("HER");

        ArrayList<String> outputs = new ArrayList<>();

        for (String word : hiddenWords) {
            outputs.add(wordSearch.searchWord(grid, word));
        }
        assertEquals("HER 6:0 6:2", outputs.get(0));
        assertEquals("HAPPILY 6:0 0:0", outputs.get(1));
        assertEquals("YES 0:0 2:2", outputs.get(2));
    }

    @Test
    public void testSearchWordWithTenXTenDimensions() {
        char[][] grid = {
                {'Y','A','D','W','B','G','X','K','T','Z'},
                {'W','H','C','V','F','N','O','I','P','J'},
                {'E','R','T','I','A','S','G','H','T','Q'},
                {'R','L','G','R','E','T','H','G','I','F'},
                {'T','H','X','N','O','L','M','W','I','C'},
                {'T','U','Y','T','R','W','Q','G','O','I'},
                {'U','K','M','K','T','L','Z','Z','P','B'},
                {'I','G','C','V','R','T','H','Z','X','S'},
                {'O','I','K','Y','E','A','Z','O','U','M'},
                {'P','C','D','R','Q','P','B','W','G','B'}
        };
        Set<String> hiddenWords = new HashSet<>();
        hiddenWords.add("BUZZWORTHY");
        hiddenWords.add("BUZZ");
        hiddenWords.add("FIGHT");
        hiddenWords.add("PICK");
        hiddenWords.add("FIGHTER");

        ArrayList<String> outputs = new ArrayList<>();

        for (String word : hiddenWords) {
            outputs.add(wordSearch.searchWord(grid, word));
        }
        assertEquals("PICK 9:0 6:3", outputs.get(0));
        assertEquals("BUZZWORTHY 9:9 0:0", outputs.get(1));
        assertEquals("BUZZ 9:9 6:6", outputs.get(2));
        assertEquals("FIGHT 1:4 5:0", outputs.get(3));
        assertEquals("FIGHTER 3:9 3:3", outputs.get(4));
    }
}