package com.example.iee2021168.Controllers.Search;

import com.example.iee2021168.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class SearchController implements Initializable {
    public Button searchDocB;
    public Button searchPatB;
    public Button searchAppointB;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
    }

    public void addListeners() {
        searchDocB.setOnAction(event -> searchDoc());
        searchPatB.setOnAction(event -> searchPat());
        searchAppointB.setOnAction(event -> searchAppoint());
    }

    private void searchDoc(){
        Model.getInstance().getViewFactory().getSearchSelectedView().set("");
        Model.getInstance().getViewFactory().getSearchSelectedView().set("searchDoc");
    }

    private void searchPat(){
        Model.getInstance().getViewFactory().getSearchSelectedView().set("");
        Model.getInstance().getViewFactory().getSearchSelectedView().set("searchPat");
    }

    private void searchAppoint(){
        Model.getInstance().getViewFactory().getSearchSelectedView().set("");
        Model.getInstance().getViewFactory().getSearchSelectedView().set("searchAppoint");
    }
}
