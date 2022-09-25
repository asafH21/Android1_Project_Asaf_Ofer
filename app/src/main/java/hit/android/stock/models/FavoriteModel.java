package hit.android.stock.models;

public class FavoriteModel {

    private String name;

    public String getName() {
        return name;
    }

    public String getLastValue() {
        return lastValue;
    }

    public String getPrecentage() {
        return precentage;
    }

    private String lastValue;
    private String precentage;

    public FavoriteModel(String name, String lastValue, String precentage) {
        this.name = name;
        this.lastValue = lastValue;
        this.precentage = precentage;
    }
}
