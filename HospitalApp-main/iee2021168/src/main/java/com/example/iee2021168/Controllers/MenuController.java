package com.example.iee2021168.Controllers;

import com.example.iee2021168.Models.Model;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {


    public Button homeMenuB;
    public Button addMenuB;
    public Button editMenuB;
    public Button searchMenuB;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
    }

    private void addListeners(){
        homeMenuB.setOnAction(event -> onHome());
        addMenuB.setOnAction(event -> onAdd());
        editMenuB.setOnAction(event -> onEdit());
        searchMenuB.setOnAction(event -> onSearch());
    }

    private void onHome(){
        Model.getInstance().getViewFactory().getMenuSelectedView().set("");
        Model.getInstance().getViewFactory().getMenuSelectedView().set("Home");
    }

    private void onAdd(){
        Model.getInstance().getViewFactory().getMenuSelectedView().set("");
        Model.getInstance().getViewFactory().getMenuSelectedView().set("Add");
    }

    private void onEdit() {
        Model.getInstance().getViewFactory().getMenuSelectedView().set("Loading");
        Task<Void> loadEditTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Model.getInstance().getViewFactory().getEdit();
                return null;
            }
        };
        loadEditTask.setOnSucceeded(e -> {
            Model.getInstance().getViewFactory().getMenuSelectedView().set("Edit");
        });
        new Thread(loadEditTask).start();
    }


    private void onSearch(){
        Model.getInstance().getViewFactory().getMenuSelectedView().set("");
        Model.getInstance().getViewFactory().getMenuSelectedView().set("Search");
    }
}
