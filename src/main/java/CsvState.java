import com.opencsv.bean.CsvBindByName;
public class CsvState {

    @CsvBindByName(column = "StateName", required = true)
    public String state;

    @CsvBindByName(column = "StateCode", required = true)
    public String stateCode;

    @Override
    public String toString() {
        return "CsvState{" +
                "stateName='" + state + '\'' +
                ", stateCode='" + stateCode + '\'' +
                '}';
    }
}

