package App;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import Backpack.Agent;
import Backpack.Project;
import DataBank.ActivePOJO;
import DataBank.Bank;
import DataBank.UnionAgents;
import DataBank.UnionString;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

import static javafx.scene.control.cell.TextFieldTableCell.forTableColumn;

/**
 * @author Evgeny Dybov
 */

public class ControllerResultOne {
    @FXML
    public Button addActiveToCouple;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button result;

    @FXML
    private TableView<ActivePOJO> tableActiv;

    @FXML
    private TableView<UnionString> tableRes;

    @FXML
    void initialize() {
        assert result != null : "fx:id=\"result\" was not injected: check your FXML file 'ResultOne.fxml'.";
        assert tableActiv != null : "fx:id=\"tableActiv\" was not injected: check your FXML file 'ResultOne.fxml'.";
        assert tableRes != null : "fx:id=\"tableRes\" was not injected: check your FXML file 'ResultOne.fxml'.";

        addColumnTable();
        addItemsTableRes();
        if (Bank.getMatrix() == null) {
            addItemsTable();
        } else {
            updateActivityColumnWithMatrix(Bank.getMatrix());
        }
        addActiveToCouple.setOnAction(actionEvent -> {
            openWindowTwo();
            Stage stage = (Stage) result.getScene().getWindow();
            stage.close();
        });
        result.setOnAction(actionEvent -> {
            if (checkPojo()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Введено некорректное значение");
                alert.showAndWait();
            } else {
                editPOJOS();
                Bank.setActivePOJOS(pojo);
                openWindow();
                Stage stage = (Stage) result.getScene().getWindow();
                stage.close();
            }
        });
    }

    private boolean checkPojo() {
        double count = 0;
        for (ActivePOJO pojo :
                pojo) {
            count += pojo.getActive();
            if (pojo.getHighBound() <= 0 || pojo.getHighBound() < pojo.getLowBound() || pojo.getLowBound() < 0 || pojo.getActive() < 0)
                return true;
        }
        return count == 0;
    }

    private void editPOJOS() {
        double sum = 0;
        for (ActivePOJO pojo :
                pojo) {
            sum += pojo.getActive();
        }
        for (ActivePOJO pojo :
                pojo) {
            pojo.setActive(pojo.getActive() / sum);
        }
    }

    private void openWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Objects.requireNonNull(Application.class.getResource("Res.fxml")));
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
        Stage stage = (Stage) result.getScene().getWindow();
        stage.close();
    }

    private void openWindowTwo() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Objects.requireNonNull(Application.class.getResource("CoupleActive.fxml")));
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
    }


    private void addColumnTable() {
        tableActiv.getColumns().clear();
        tableRes.getColumns().clear();
        tableActiv.setEditable(true);
        ControllerRes.makeTableResOne(tableRes);
        TableColumn<ActivePOJO, String> tableColumnNameA = new TableColumn<>("Агенты");
        TableColumn<ActivePOJO, Double> tableColumnActive = new TableColumn<>("Активность");
        TableColumn<ActivePOJO, Double> tableColumnHB = new TableColumn<>("ВГ");
        TableColumn<ActivePOJO, Double> tableColumnLB = new TableColumn<>("НГ");
        tableColumnNameA.setCellValueFactory(new PropertyValueFactory<>("nameAgent"));
        tableColumnNameA.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnNameA.setOnEditCommit((TableColumn.CellEditEvent<ActivePOJO, String> event) -> {
            TablePosition<ActivePOJO, String> pos = event.getTablePosition();
            String newFullName = event.getNewValue();
            int row = pos.getRow();
            ActivePOJO pojo = event.getTableView().getItems().get(row);
            pojo.setNameAgent(newFullName);
        });
        tableColumnActive.setCellValueFactory(new PropertyValueFactory<>("active"));
        tableColumnActive.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        tableColumnActive.setOnEditCommit((TableColumn.CellEditEvent<ActivePOJO, Double> event) -> {
            TablePosition<ActivePOJO, Double> pos = event.getTablePosition();
            double value = event.getNewValue();
            int row = pos.getRow();
            ActivePOJO pojo = event.getTableView().getItems().get(row);
            pojo.setActive(value);
        });
        tableColumnHB.setCellValueFactory(new PropertyValueFactory<>("highBound"));
        tableColumnHB.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        tableColumnHB.setOnEditCommit((TableColumn.CellEditEvent<ActivePOJO, Double> event) -> {
            TablePosition<ActivePOJO, Double> pos = event.getTablePosition();
            double value = event.getNewValue();
            int row = pos.getRow();
            ActivePOJO pojo = event.getTableView().getItems().get(row);
            pojo.setHighBound(value);
        });
        tableColumnLB.setCellValueFactory(new PropertyValueFactory<>("lowBound"));
        tableColumnLB.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        tableColumnLB.setOnEditCommit((TableColumn.CellEditEvent<ActivePOJO, Double> event) -> {
            TablePosition<ActivePOJO, Double> pos = event.getTablePosition();
            double value = event.getNewValue();
            int row = pos.getRow();
            ActivePOJO pojo = event.getTableView().getItems().get(row);
            pojo.setLowBound(value);
        });
        tableColumnNameA.prefWidthProperty().bind(tableActiv.widthProperty().multiply(0.4));
        tableColumnActive.prefWidthProperty().bind(tableActiv.widthProperty().multiply(0.3));
        tableColumnHB.prefWidthProperty().bind(tableActiv.widthProperty().multiply(0.15));
        tableColumnLB.prefWidthProperty().bind(tableActiv.widthProperty().multiply(0.15));

        tableActiv.getColumns().addAll(tableColumnNameA, tableColumnActive, tableColumnLB, tableColumnHB);
    }

    private void addItemsTableRes() {
        String tmpA = "";
        String tmpP = "";
        for (UnionAgents unionAgents : Bank.getResultBank().getUnions()) {
            tmpA = makeStringAgent(unionAgents);
            tmpP = makeStringProject(unionAgents);
            tableRes.getItems().add(new UnionString(tmpA.substring(0, tmpA.length() - 2), tmpP.substring(0, tmpP.length() - 2), unionAgents.getProfit()));
        }


    }

    private String makeStringAgent(UnionAgents unionAgents) {
        StringBuilder res = new StringBuilder();
        for (Agent agent :
                unionAgents.getAgents()) {
            res.append(agent.getName()).append(",").append(" ");
        }
        return res.toString();
    }

    private String makeStringProject(UnionAgents unionAgents) {
        StringBuilder res = new StringBuilder();
        for (Project project :
                unionAgents.getProjects()) {
            res.append(project.getName()).append(",").append(" ");
        }
        return res.toString();
    }

    private final ArrayList<ActivePOJO> pojo = new ArrayList<>();

    private void addItemsTable() {
        int count = 0;
        for (Agent agent :
                Bank.getAgents()) {
            pojo.add(new ActivePOJO(agent.getName(), 0, 0, 0));
            tableActiv.getItems().add(pojo.get(count));
            count++;

        }
    }

    private void updateActivityColumnWithMatrix(double[] matrix) {
        addItemsTable();
        ObservableList<ActivePOJO> items = tableActiv.getItems();
        if (matrix.length == items.size()) { // Проверка на соответствие размеров
            for (int i = 0; i < matrix.length; i++) {
                ActivePOJO pojo = items.get(i);
                pojo.setActive(matrix[i]); // Установка значения активности из массива
            }
            tableActiv.refresh(); // Обновление отображения таблицы
        } else {
            // Обработка ошибки: размер массива не соответствует количеству строк в таблице
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "The size of the matrix does not match the number of table rows.");
        }
    }
}

