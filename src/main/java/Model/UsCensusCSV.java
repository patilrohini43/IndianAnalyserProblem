package Model;
import com.opencsv.bean.CsvBindByName;

public class UsCensusCSV {

    @CsvBindByName(column = "StateId", required = true)
    public String stateId;

    @CsvBindByName(column = "State", required = true)
    public String state;

    @CsvBindByName(column = "Population", required = true)
    public int population;

    @CsvBindByName(column = "TotalArea", required = true)
    public double totalArea;

    @CsvBindByName(column = "PopulationDensity", required = true)
    public double populationDensity;

//    public UsCensusCSV(String state, String stateCode, int population, double totalArea, double populationDensity) {
//        this.state=state;
//        this.stateId=stateCode;
//        this.population=population;
//        this.totalArea=totalArea;
//        this.populationDensity=populationDensity;
//    }
}
