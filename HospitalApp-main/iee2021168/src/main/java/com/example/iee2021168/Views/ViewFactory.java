package com.example.iee2021168.Views;

import com.example.iee2021168.Controllers.MainController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ViewFactory {

    private final StringProperty menuSelectedView;
    private final StringProperty addSelectedView;
    private final StringProperty backView;
    private final StringProperty searchSelectedView;

    public ViewFactory() {
        this.menuSelectedView = new SimpleStringProperty("");
        this.addSelectedView = new SimpleStringProperty("");
        this.backView = new SimpleStringProperty("");
        this.searchSelectedView = new SimpleStringProperty("");
    }

    public StringProperty getMenuSelectedView() {
        return menuSelectedView;
    }

    public StringProperty getAddSelectedView() {
        return addSelectedView;
    }

    public StringProperty getBackView() {
        return backView;
    }

    public StringProperty getSearchSelectedView() {
        return searchSelectedView;
    }

    public void showMainView(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/iee2021168/main-view.fxml"));
        MainController mainController = new MainController();
        loader.setController(mainController);
        Scene scene = null;
        try {
            scene = new Scene(loader.load(), 1200, 650);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Πληροφοριακό σύστημα μηχανογράφησης νοσοκομείου.");
        stage.setResizable(true);////////////////////////////////////          RESIZABLE OR NOT           ///////////////////////////////
        stage.setMinHeight(650);
        stage.setMinWidth(1200);
        Image logo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/hospital-sign.png")));
        stage.getIcons().add(logo);


        stage.show();
    }


    public Parent getHome() {
        return loadFXML("/com/example/iee2021168/home.fxml");
    }

    public Parent getAdd() {
        return loadFXML("/com/example/iee2021168/add.fxml");
    }

    public Parent getEdit() {
        return loadFXML("/com/example/iee2021168/edit.fxml");
    }

    public Parent getSearch() {
        return loadFXML("/com/example/iee2021168/search.fxml");
    }

    public Parent getAddDoc() {
        return loadFXML("/com/example/iee2021168/add-doc.fxml");
    }

    public Parent getAddPat() {
        return loadFXML("/com/example/iee2021168/add-pat.fxml");
    }

    public Parent getAddHis() {
        return loadFXML("/com/example/iee2021168/add-his.fxml");
    }

    public Parent getAddAppoint() {
        return loadFXML("/com/example/iee2021168/add-appoint.fxml");
    }

    public Parent getSearchDoc() {
        return loadFXML("/com/example/iee2021168/search-doc.fxml");
    }

    public Parent getSearchPat() {
        return loadFXML("/com/example/iee2021168/search-pat.fxml");
    }

    public Parent getSearchAppoint() {
        return loadFXML("/com/example/iee2021168/search-appoint.fxml");
    }

    public Parent getLoading() {
        return loadFXML("/com/example/iee2021168/loading.fxml");
    }


    private Parent loadFXML(String fxmlPath) {
        try {
            System.out.println("Loading FXML: " + fxmlPath);
            return new FXMLLoader(getClass().getResource(fxmlPath)).load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}

