import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.IOException;

public class CensusAnalyserTest {

    private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
    private static final String WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.csv";
    private static final String INDIA_STATE_PATH = "./src/test/resources/IndiaStateCode.csv";

    @Test
    public void givenIndianCensusCSVFileReturnsCorrectRecords() throws IOException, CensusAnalyserException {
            StateCensusAnalyser censusAnalyser = new StateCensusAnalyser();
            int numOfRecords = censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
            Assert.assertEquals(29,numOfRecords);
    }

    @Test
    public void givenIndiaCensusData_WithWrongFile_ShouldThrowException() {
        try {
                StateCensusAnalyser censusAnalyser = new StateCensusAnalyser();
                ExpectedException exceptionRule = ExpectedException.none();
                exceptionRule.expect(CensusAnalyserException.class);
                censusAnalyser.loadIndiaCensusData(WRONG_CSV_FILE_PATH);
             }
        catch (CensusAnalyserException e) {
                Assert.assertEquals(INDIA_CENSUS_CSV_FILE_PATH,e.getMessage());
             }
    }

    @Test
    public void  givenIndianCensusData_WhenFileCorrect_butTypeIncorrect() {
        try {
            StateCensusAnalyser censusAnalyser = new StateCensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
        }
        catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM,e.type);
        }
    }

    @Test
    public void givenIndianStateCsvFile_CorrectButHeaderIncorrect() {
        try{
            StateCensusAnalyser censusAnalyser = new StateCensusAnalyser();
            censusAnalyser.loadIndiaCensusData(INDIA_STATE_PATH);
        }catch (CensusAnalyserException e){
            Assert.assertEquals(CensusAnalyserException.ExceptionType.HEADERNOTFOUND,e.type);
        }
    }

    @Test
    public void givenIndianStateCsvFile_ShouldReturnExactCount() {
        try {
            StateCensusAnalyser censusAnalyser = new StateCensusAnalyser();
           int numCount= censusAnalyser.loadIndiaStateCensusData(INDIA_STATE_PATH);
            Assert.assertEquals(37,numCount);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }
}

