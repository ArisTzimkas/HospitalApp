package com.example.iee2021168.Controllers.Add;

import com.example.iee2021168.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;


import java.net.URL;
import java.util.ResourceBundle;

public class AddController implements Initializable {
    public Button addDocB;
    public Button addHisB;
    public Button addPatB;
    public Button addAppointB;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
    }


    private void addListeners(){
        addDocB.setOnAction(event -> onAddDoc());
        addHisB.setOnAction(event -> onAddHis());
        addPatB.setOnAction(event -> onAddPat());
        addAppointB.setOnAction(event -> onAddAppoint());
    }

    private void onAddDoc(){
        Model.getInstance().getViewFactory().getAddSelectedView().set("");
        Model.getInstance().getViewFactory().getAddSelectedView().set("addDoc");
    }

    private void onAddHis(){
        Model.getInstance().getViewFactory().getAddSelectedView().set("");
        Model.getInstance().getViewFactory().getAddSelectedView().set("addHis");
    }

    private void onAddPat(){
        Model.getInstance().getViewFactory().getAddSelectedView().set("");
        Model.getInstance().getViewFactory().getAddSelectedView().set("addPat");
    }

    private void onAddAppoint(){
        Model.getInstance().getViewFactory().getAddSelectedView().set("");
        Model.getInstance().getViewFactory().getAddSelectedView().set("addAppoint");
    }
}
