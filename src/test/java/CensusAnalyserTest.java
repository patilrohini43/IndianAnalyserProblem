import Model.IndiaCensusCSV;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CensusAnalyserTest {

    private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
    private static final String WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.csv";
    private static final String INDIA_STATE_PATH = "./src/test/resources/IndiaStateCode.csv";
    private static final String US_CSV_FILE_PATH = "./src/test/resources/USCensusData.csv";

    @Test
    public void givenIndianCensusCSVFileReturnsCorrectRecords() throws CensusAnalyserException {
            StateCensusAnalyser censusAnalyser = new StateCensusAnalyser();
            int numOfRecords = censusAnalyser.loadIndiaStateCensusData(INDIA_CENSUS_CSV_FILE_PATH,INDIA_STATE_PATH);
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
                Assert.assertEquals(WRONG_CSV_FILE_PATH,e.getMessage());
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
            censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
        }catch (CensusAnalyserException e){
            Assert.assertEquals(CensusAnalyserException.ExceptionType.HEADERNOTFOUND,e.type);
        }
    }

    @Test
    public void  givenIndianStateCsvFileData_AplhabeticalOrder() throws CensusAnalyserException {
        StateCensusAnalyser stateCensusAnalyser=new StateCensusAnalyser();
        stateCensusAnalyser.loadIndiaStateCensusData(INDIA_CENSUS_CSV_FILE_PATH,INDIA_STATE_PATH);
        String sortedData=stateCensusAnalyser.getAlphabeticalOrderData(SortField.population);
        IndiaCensusCSV[] indiaCensusCSVList = new Gson().fromJson(sortedData,IndiaCensusCSV[].class);
        Assert.assertEquals(199812341,indiaCensusCSVList[0].population);
    }

    @Test
    public void givenUsCsvFile_ShouldReturnExactCount() throws CensusAnalyserException {
        StateCensusAnalyser stateCensusAnalyser=new StateCensusAnalyser();
        int usCount = stateCensusAnalyser.loadUsCsvCensusData( US_CSV_FILE_PATH );
        Assert.assertEquals(51,usCount);
    }
}




