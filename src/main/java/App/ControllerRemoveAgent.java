package App;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import Backpack.Agent;
import Backpack.Project;
import DataBank.Bank;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

/**
 * @author Evgeny Dybov
 */

public class ControllerRemoveAgent {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> box;

    @FXML
    private Button remove;

    @FXML
    void initialize() {
        assert box != null : "fx:id=\"box\" was not injected: check your FXML file 'RemoveProject.fxml'.";
        assert remove != null : "fx:id=\"remove\" was not injected: check your FXML file 'RemoveProject.fxml'.";
        for (Agent agent : Bank.getAgents())
            box.getItems().add(agent.getName());
        remove.setOnAction(actionEvent -> {
            Bank.getAgents().removeIf(agent -> Objects.equals(agent.getName(), box.getValue()));
            fillTable();
            Stage stage = (Stage) remove.getScene().getWindow();
            stage.close();
        });
    }
    private void fillTable() {
        Bank.getAgentTableView().getItems().clear();
        Bank.getProjectTableView().getItems().clear();
        if (Bank.getAgents().size() > 0){
            for (Agent agent:
                    Bank.getAgents()) {
                Bank.getAgentTableView().getItems().add(agent);
                for (Project project:
                        agent.getProjects() ) {
                    Bank.getProjectTableView().getItems().add(project);
                }
            }
        }
    }

}
