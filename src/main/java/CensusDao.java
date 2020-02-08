import Model.UsCensusCSV;
import Model.IndiaCensusCSV;

public class CensusDao {
    public String state;
    public int population;
    public double totalArea;
    public double populationDensity;
    public String stateCode;

    public CensusDao(IndiaCensusCSV indiaCensusCSV){
        this.state=indiaCensusCSV.state;
        this.population=indiaCensusCSV.population;
        this.totalArea=indiaCensusCSV.areaInSqKm;
        this.populationDensity=indiaCensusCSV.densityPerSqKm;
    }

    public CensusDao(UsCensusCSV usCensusCSV)
    {
        this.state=usCensusCSV.state;
        this.stateCode=usCensusCSV.stateId;
        this.population=usCensusCSV.population;
        this.totalArea=usCensusCSV.totalArea;
        this.populationDensity=usCensusCSV.populationDensity;
    }

}
