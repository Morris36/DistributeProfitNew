package App;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.commons.math.optimization.OptimizationException;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * @author Evgeny Dybov
 */

public class Application extends javafx.application.Application {
    @Override
    /**
     * Метод создания нового окна
     */
    public void start(Stage stage) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(Objects.requireNonNull(Application.class.getResource("Start.fxml")));
        stage.setTitle("Distribute profit");
        stage.getIcons().add(new Image(Objects.requireNonNull(Application.class.getResourceAsStream("DPIcon.png"))));
        stage.setScene(new Scene(fxmlLoader));
        stage.show();
    }

    public static void main(String[] args) throws OptimizationException, IOException {
        launch();
    }
    public static double parseAndCalculateFraction(String fraction) {
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
