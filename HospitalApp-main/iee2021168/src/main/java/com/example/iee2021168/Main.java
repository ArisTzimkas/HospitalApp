package com.example.iee2021168;

import com.example.iee2021168.Views.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage stage) {
        ViewFactory viewFactory = new ViewFactory();
        viewFactory.showMainView();
        if (Database.connect() != null) {
            System.out.println("Database connected.");//hello
        } else {
            System.out.println("Failed to connect.");
        }
    }
}
