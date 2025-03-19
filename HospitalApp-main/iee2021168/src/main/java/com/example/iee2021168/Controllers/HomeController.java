package com.example.iee2021168.Controllers;

import com.example.iee2021168.Database;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeController {


    public Label timeLabel;
    @FXML
    public TableView<Appoints> tableView;
    @FXML
    public TableColumn<Appoints,String> operTypeColumn;
    @FXML
    public TableColumn<Appoints,String> operTimeColumn;
    @FXML
    public TableColumn<Appoints,Integer> appointIdColumn;
    public Label label;


    public static class Appoints {
        private String type;
        private String time;
        private int appointId;
        public Appoints(String type, String time, int appointId) {
            this.type = type;
            this.time = time;
            this.appointId = appointId;
        }
        public String getType() {
            return type;
        }
        public String getTime() {
            return time;
        }
        public int getAppointId() {
            return appointId;
        }
    }


    @FXML
    public void initialize() {
        TimeNow();
        operTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        operTimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        appointIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointId"));
        loadAppointLog();
    }




    private void TimeNow() {
        Thread timer = new Thread(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat("d MMMMM yyyy \nHH:mm:ss a");
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final String timenow = sdf.format(new Date());
                Platform.runLater(() -> {
                    timeLabel.setText(timenow);
                });
            }
        });
        timer.setDaemon(true); //stamataei to thread otan kleisei to app
        timer.start();
    }



    private void loadAppointLog() {
        ObservableList<Appoints> appoints = Database.getAppointmentsLog();
        if(appoints.isEmpty()){
            tableView.setVisible(false);
            label.setVisible(true);
        }
        else{
            tableView.setVisible(true);
            label.setVisible(false);
            tableView.setItems(appoints);
        }

    }




}
