package com.example.iee2021168;

import com.example.iee2021168.Views.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;


public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Database.verifyConnectionOrExit();
        ViewFactory viewFactory = new ViewFactory();
        viewFactory.showMainView();

    }
}
