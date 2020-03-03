package View;

import Controller.MainController;
import Model.MainModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Slider;
import javafx.stage.*;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;

public class MainView extends Application {

    private Stage stage;
    private Parent root;
    private Alert waitingBox;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        //root = FXMLLoader.load(getClass().getResource("/View/sample.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/sample.fxml"));
        root = fxmlLoader.load();
        MainController controller = fxmlLoader.getController();
        controller.setView(this);
        controller.setModel(new MainModel());

        stage = primaryStage;
        setUpTimeGranulationSlider(controller);

        primaryStage.setTitle("Dashboard");
        root.getStylesheets().add("/View/styles.css");
        primaryStage.setScene(new Scene(root, 300, 275));

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        primaryStage.setMaximized(true);
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(900);

        primaryStage.show();
    }

    private void setUpTimeGranulationSlider(MainController controller){
        Slider slider = (Slider) root.lookup("#timeGranulationSlider");
        slider.setLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Double d) {
                switch (d.intValue()){
                    case MainController.SLIDER_DAY:
                        return "Days";
                    case MainController.SLIDER_WEEK:
                        return "Weeks";
                    case MainController.SLIDER_MONTH:
                        return "Months";
                    case MainController.SLIDER_YEAR:
                        return "Years";
                }
                return "";
            }

            @Override
            public Double fromString(String s) {
                return null;
            }
        });
        slider.setOnMouseReleased(mouseEvent -> {
            controller.recreateGraph((int) slider.getValue());
        });
        controller.recreateGraph(0);
    }

    public File showFileChooser(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        return fileChooser.showOpenDialog(stage);
    }

    public Window getWindow(){
        return root.getScene().getWindow();
    }

    public void showErrorMessage(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    public void showLoadingDialog(){
        waitingBox = new Alert(Alert.AlertType.NONE,"Please wait...");
        waitingBox.show();
    }

    public void hideLoadingDialog(){
        waitingBox.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
        waitingBox.hide();
    }

}
