package edu.boisestate.cs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class SolveMainTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final String outPath = "src/test/resources/out/";
    private final String inPath = "src/test/resources/in/";

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testConcat() {
        String expectedOutput = "";
        try {
            expectedOutput = new String (Files.readAllBytes(Paths.get(outPath + "concat.txt")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] args = {inPath + "concat_isEmpty_equals_contains_l2_d2_bench.json", "-s", "inverse", "-v", "2", "-l", "2"};
        SolveMain.main(args);
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testDelete() {
        String expectedOutput = "";
        try {
            expectedOutput = new String (Files.readAllBytes(Paths.get(outPath + "delete.txt")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] args = {inPath + "delete_isEmpty_equals_contains_l2_d2_bench.json", "-s", "inverse", "-v", "2", "-l", "2"};
        SolveMain.main(args);
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testReplace() {
        String expectedOutput = "";
        try {
            expectedOutput = new String (Files.readAllBytes(Paths.get(outPath + "replace.txt")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] args = {inPath + "replace_isEmpty_equals_contains_l2_d2_bench.json", "-s", "inverse", "-v", "2", "-l", "2"};
        SolveMain.main(args);
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testSubstring() {
        String expectedOutput = "";
        try {
            expectedOutput = new String (Files.readAllBytes(Paths.get(outPath + "substring.txt")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] args = {inPath + "substring12_isEmpty_equals_contains_l2_d2_bench.json", "-s", "inverse", "-v", "2", "-l", "2"};
        SolveMain.main(args);
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testToLower() {
        String expectedOutput = "";
        try {
            expectedOutput = new String (Files.readAllBytes(Paths.get(outPath + "toLower.txt")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] args = {inPath + "toLowerCase_isEmpty_equals_contains_l2_d2_bench.json", "-s", "inverse", "-v", "2", "-l", "2"};
        SolveMain.main(args);
        assertEquals(expectedOutput, outContent.toString());
    }
}
