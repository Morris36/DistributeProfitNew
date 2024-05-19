package App;

import Backpack.Agent;
import DataBank.ActivePOJO;
import DataBank.Bank;
import DataBank.RowData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;

import java.util.Arrays;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControllerCouple {
    @FXML
    public TableView<RowData> tableActiv;
    @FXML
    public Button result;
    private ObservableList<RowData> data = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        initTable();
        result.setOnAction(actionEvent -> {
            System.out.println("result");
        });
        result.setOnAction(actionEvent -> {
            int tableSize = Bank.getAgents().size();
            double[][] matrix = new double[data.size()][tableSize];
            for (int i = 0; i < data.size(); i++) {
                RowData rowData = data.get(i);

                for (int j = 0; j < tableSize; j++) {
                    String value = rowData.getValueAt(j);
                    try {
                        if (value.equals("") || value.equals("0")) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Матрица парных сравнений заполнена некорректно");
                            alert.showAndWait();
                            break;
                        }
                        if (value.contains("/")) {
                            matrix[i][j] = parseAndCalculateFraction(value);
                        } else {
                            if (Double.parseDouble(value) != 0)
                                matrix[i][j] = parseAndCalculateFraction(value);
                        }

                    } catch (NumberFormatException e) {
                        matrix[i][j] = 1; // Обрабатываем некорректный ввод
                    }
                }
            }
            // Пример вывода для проверки
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    if (matrix[i][j] != 1 / matrix[j][i] || matrix[i][j] < 0) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Элементы не симметричны");
                        alert.showAndWait();
                        break;
                    }

                }
            }
            Bank.setMatrix(EigenExample.addMatrix(matrix));
            openWindow();
        });
    }

    private void initTable() {
        tableActiv.getColumns().clear();
        tableActiv.getItems().clear();
        int tableSize = Bank.getAgents().size(); // Предполагается, что этот метод возвращает количество агентов.
        tableActiv.setEditable(true);

        TableColumn<RowData, String> firstColumn = new TableColumn<>("");
        firstColumn.setCellValueFactory(cellData -> cellData.getValue().indexProperty());
        tableActiv.getColumns().add(firstColumn);

        for (int i = 1; i <= tableSize; i++) {
            final int colIdx = i - 1;
            TableColumn<RowData, String> column = new TableColumn<>(Bank.getAgents().get(colIdx).name);
            column.setCellValueFactory(cellData -> cellData.getValue().valueProperty(colIdx));

            if (i > 0) {
                if (colIdx == i - 1) { // Условие для диагональных колонок
                    column.setCellFactory(tc -> new TextFieldTableCell<RowData, String>(new DefaultStringConverter()) {
                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (!empty && getIndex() == colIdx) {
                                setText("1");  // Установка значения "1" для диагональных элементов
                                setEditable(false); // Сделать ячейку нередактируемой
                            } else {
                                setText(item);
                                setEditable(true);
                            }
                        }
                    });
                } else { // Условие для всех остальных колонок
                    column.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
                    column.setOnEditCommit(event -> {
                        RowData rowData = event.getRowValue();
                        System.out.println(event.getNewValue());
                    });
                }
                tableActiv.getColumns().add(column);
            }
        }

        for (int i = 0; i < tableSize; i++) {
            RowData rowData = new RowData(Bank.getAgents().get(i).name, tableSize);
            for (int j = 0; j < tableSize; j++) {
                if (i == j) {
                    rowData.setValueAt(j, "1"); // Установка значения "1" при инициализации
                } else {
                    rowData.setValueAt(j, ""); // Инициализация остальных значений как пустые
                }
            }
            data.add(rowData);
        }

        tableActiv.setItems(data);
    }

    private void openWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Objects.requireNonNull(Application.class.getResource("ResultOne.fxml")));
            Scene scene = new Scene(fxmlLoader.load(), -1, -1);
            Stage stage = new Stage();
            stage.getIcons().add(new Image(Objects.requireNonNull(Application.class.getResourceAsStream("DPIcon.png"))));
            stage.setTitle("Distribute Profit");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
        Stage stage = (Stage) tableActiv.getScene().getWindow();
        stage.close();
    }

    public double parseAndCalculateFraction(String fraction) {
        try {
            if (fraction.contains("/")) {
                String[] parts = fraction.split("/");
                double numerator = Double.parseDouble(parts[0]);
                double denominator = Double.parseDouble(parts[1]);
                return numerator / denominator;
            } else {
                return Double.parseDouble(fraction);  // В случае, если это не дробь, а просто число
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Invalid input format: " + fraction);
            return 0;  // Возвращает 0 или другое значение по умолчанию в случае ошибки
        }
    }
}
