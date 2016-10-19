package eps;

import java.io.IOException;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Chukwuka Odina
 */
public class EPS extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        try{
            Parent loginView = FXMLLoader.load(getClass().getResource("LoginView.fxml"));
            Scene loginScene = new Scene(loginView);
            stage.setScene(loginScene);
            stage.show();
            stage.setResizable(false);
            stage.setTitle("Login");
            stage.centerOnScreen();
        }catch(IOException e){
        }

    }

    /**
     * The main method that is run
     * @param args
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws SQLException {
        launch(args);
    }
}
