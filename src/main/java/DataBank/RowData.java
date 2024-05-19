package DataBank;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RowData {
    private SimpleStringProperty index;
    private final ObservableList<StringProperty> values;

    public RowData(String index, int size) {
        this.index = new SimpleStringProperty(index);
        this.values = FXCollections.observableArrayList();
        for (int i = 0; i < size; i++) {
            this.values.add(new SimpleStringProperty(""));
        }
    }

    public StringProperty indexProperty() {
        return index;
    }

    public StringProperty valueProperty(int index) {
        if (index >= 0 && index < values.size()) {
            return values.get(index);
        }
        return null;
    }

    public void setValueAt(int index, String value) {
        if (index >= 0 && index < values.size()) {
            values.get(index).set(value);
        }
    }

    public String getValueAt(int index) {
        return values.get(index).get();
    }
}