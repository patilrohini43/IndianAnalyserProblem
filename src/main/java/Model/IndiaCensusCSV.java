package Model;

import com.opencsv.bean.CsvBindByName;

public class IndiaCensusCSV {

    @CsvBindByName(column = "State", required = true)
    public String state;

    @CsvBindByName(column = "Population", required = true)
    public int population;

    @CsvBindByName(column = "AreaInSqKm", required = true)
    public int areaInSqKm;

    @CsvBindByName(column = "DensityPerSqKm", required = true)
    public int densityPerSqKm;

//    public IndiaCensusCSV(String state, String stateCode, int population, int populationDensity, int totalArea) {
//        this.state=state;
//        this.population=population;
//        this.areaInSqKm=totalArea;
//        this.densityPerSqKm=populationDensity;
//    }
}
