package edu.boisestate.cs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.File;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BenchmarkSolveTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    // This method provides the parameters (file paths) for the test
    static Stream<String> fileProvider() {
        String benchmarksPath = "graphs/benchmarks/l2";
        File dir = new File(benchmarksPath);
        return Stream.of(dir.listFiles()).map(File::getAbsolutePath);
    }

    // This is the parameterized test
    @ParameterizedTest
    @MethodSource("fileProvider")
    public void testSolveMain(String filePath) {
        try {
            String[] args = {filePath, "-s", "inverse", "-v", "2", "-l", "2"};
            SolveMain.main(args);

            String[] output = outContent.toString().split("\n");
            String result = output[output.length - 5];
            boolean assertion = result.contains("[true,true]");
            assertTrue(assertion);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}