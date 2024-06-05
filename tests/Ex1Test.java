package tests;

import org.junit.jupiter.api.Test;
import src.Ex1;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import java.nio.file.Paths;
import java.net.URISyntaxException;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class Ex1Test {

    private void runTest(int testNumber) {
        try {
            // Construct paths to input and expected output files
            String inputFileName = "tests/inputs/input" + testNumber + ".txt";
            String outputFileName = "tests/outputs/Actual/outputFile" + (testNumber) + ".txt";
            String expectedOutputFileName = "tests/outputs/output" + (testNumber) + ".txt";

            String[] args = {inputFileName, outputFileName};

            Ex1.main(args);

            String outputFile = new String(Files.readAllBytes(Paths.get(outputFileName)), StandardCharsets.UTF_8);
            String expectedOutput = new String(Files.readAllBytes(Paths.get(expectedOutputFileName)), StandardCharsets.UTF_8);


            // Assert that the actual output matches the expected output
            assertEquals(expectedOutput, outputFile);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void test1() throws IOException, URISyntaxException {

        runTest(1);
    }

    @Test
    public void test2() throws IOException, URISyntaxException {

        runTest(2);
    }
    @Test
    public void test3() throws IOException, URISyntaxException {

        runTest(3);
    }
    @Test
    public void test4() throws IOException, URISyntaxException {

        runTest(4);
    }
    @Test
    public void test5() throws IOException, URISyntaxException {

        runTest(5);
    }
    @Test
    public void test6() throws IOException, URISyntaxException {

        runTest(6);
    }
    @Test
    public void test7() throws IOException, URISyntaxException {

        runTest(7);
    }


}
