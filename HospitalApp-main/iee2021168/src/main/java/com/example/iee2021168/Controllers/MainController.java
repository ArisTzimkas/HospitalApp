package com.example.iee2021168.Controllers;

import com.example.iee2021168.Models.Model;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class MainController implements Initializable{
    @FXML
    BorderPane mainBorderPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getMenuSelectedView().addListener((observableValue, oldVal, newVal) ->{
            switch (newVal){
                case "Add" -> mainBorderPane.setCenter(Model.getInstance().getViewFactory().getAdd());
                case "Edit" ->mainBorderPane.setCenter(Model.getInstance().getViewFactory().getEdit());
                case "Search" -> mainBorderPane.setCenter(Model.getInstance().getViewFactory().getSearch());
                case "Home" -> mainBorderPane.setCenter(Model.getInstance().getViewFactory().getHome());
                case "Loading" -> mainBorderPane.setCenter(Model.getInstance().getViewFactory().getLoading());
            }
        } );


        Model.getInstance().getViewFactory().getAddSelectedView().addListener((observableValue, oldVal2, newVal2) ->{
            mainBorderPane.setCenter(null);
            switch (newVal2){
                case "addDoc" -> mainBorderPane.setCenter(Model.getInstance().getViewFactory().getAddDoc());
                case "addHis" -> mainBorderPane.setCenter(Model.getInstance().getViewFactory().getAddHis());
                case "addPat" -> mainBorderPane.setCenter(Model.getInstance().getViewFactory().getAddPat());
                case "addAppoint" -> mainBorderPane.setCenter(Model.getInstance().getViewFactory().getAddAppoint());
            }
        } );


        Model.getInstance().getViewFactory().getBackView().addListener((observableValue, oldVal3, newVal3) -> {
            mainBorderPane.setCenter(null);
            if (newVal3.equals("backToAdd")) {
                mainBorderPane.setCenter(Model.getInstance().getViewFactory().getAdd());
            } else if (newVal3.equals("backToSearch")) {
                mainBorderPane.setCenter(Model.getInstance().getViewFactory().getSearch());
            }
        });


        Model.getInstance().getViewFactory().getSearchSelectedView().addListener((observableValue, oldVal4, newVal4) ->{
            mainBorderPane.setCenter(null);
            switch (newVal4){
                case "searchDoc" -> mainBorderPane.setCenter(Model.getInstance().getViewFactory().getSearchDoc());
                case "searchPat" -> mainBorderPane.setCenter(Model.getInstance().getViewFactory().getSearchPat());
                case "searchAppoint" -> mainBorderPane.setCenter(Model.getInstance().getViewFactory().getSearchAppoint());

            }
        } );

    }
}
