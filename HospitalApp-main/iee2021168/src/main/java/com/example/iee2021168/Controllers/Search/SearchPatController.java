package com.example.iee2021168.Controllers.Search;

import com.example.iee2021168.Database;
import com.example.iee2021168.Models.Model;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ResourceBundle;

public class SearchPatController implements Initializable {
    public Button searchPatBackB;
    public Text textAMKA;
    public TextField amkaField;
    public Button SearchAMKAB;
    public ChoiceBox<String> ChoicePatType;
    public Label resultLabel;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchPatBackB.setOnAction(event -> backToSearch());

        ChoicePatType.getItems().addAll("Ασθενής με τις περισσότερες καταχωρίσεις ιστορικού","Πλήθος ασθενών και μέση ηλικία ανα τμήμα(με ιστορικό)","Ιστορικό ασθενή");
        ChoicePatType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> { handleChoiceSelection(newValue); });

        textAMKA.setVisible(false);
        amkaField.setVisible(false);
        SearchAMKAB.setVisible(false);

        SearchAMKAB.setOnAction(event -> search());
    }

    public void backToSearch() {
        Model.getInstance().getViewFactory().getBackView().set("");
        Model.getInstance().getViewFactory().getBackView().set("backToSearch");
    }


    public record Patient1(String name, int count){}
    public record Patient2(String depar, double avgAge,int count){}
    public record Patient3(String source, String doctorName, String depName, int floor){}


    private void handleChoiceSelection(String newValue) {
        if (newValue != null) {
            StringBuilder sb;
            switch (newValue) {
                case "Ασθενής με τις περισσότερες καταχωρίσεις ιστορικού":
                    textAMKA.setVisible(false);
                    amkaField.setVisible(false);
                    SearchAMKAB.setVisible(false);
                    amkaField.setText("");
                    ObservableList<SearchPatController.Patient1> pat1 = Database.get_patient_with_most_history_entries();
                    sb = new StringBuilder();
                    for (SearchPatController.Patient1 p : pat1) {
                        sb.append("⫸ Όνομα ασθενή : ").append(p.name())
                                .append(" | Πλήθος καταχωρίσεων ιστορικού : ").append(p.count())
                                .append("\n\n");
                    }
                    resultLabel.setText(sb.toString());
                    break;

                case "Πλήθος ασθενών και μέση ηλικία ανα τμήμα(με ιστορικό)":
                    textAMKA.setVisible(false);
                    amkaField.setVisible(false);
                    SearchAMKAB.setVisible(false);
                    amkaField.setText("");
                    ObservableList<SearchPatController.Patient2> pat2 = Database.getPatientAvgAgePerDepartment();
                    sb = new StringBuilder();
                    for (SearchPatController.Patient2 count : pat2) {
                        sb.append("⫸ Τμήμα : ").append(count.depar())
                                .append(" | Μέση ηλικία : ").append(count.avgAge())
                                .append(" | Πλήθος ασθενών στο τμήμα : ").append(count.count())
                                .append("\n\n");
                    }
                    resultLabel.setText(sb.toString());
                    break;

                case "Ιστορικό ασθενή":
                    textAMKA.setVisible(true);
                    amkaField.setVisible(true);
                    SearchAMKAB.setVisible(true);
                    break;

                default:
                    break;
            }
        }
    }

    public void search() {
        try {
            if(amkaField.getText().isEmpty()){
                resultLabel.setText("Το πεδίο ΑΜΚΑ δεν μπορεί να είναι κενό!");
            }else {
                int amka = Integer.parseInt(amkaField.getText());
                boolean exists = Database.checkAmkaPatients(amka);
                if (exists) {
                    ObservableList<SearchPatController.Patient3> pat3 = Database.getPatientSources(amka);
                    StringBuilder sb = new StringBuilder();
                    for (SearchPatController.Patient3 count : pat3) {
                        sb.append("⫸ Κατηγορία : ").append(count.source())
                                .append(" | Γιατρός : ").append(count.doctorName())
                                .append(" | Τμήμα : ").append(count.depName())
                                .append(" | Όροφος :").append(count.floor)
                                .append("\n\n");
                    }
                    if (sb.toString().isEmpty()){
                        resultLabel.setText("Δεν βρέθηκαν αποτελέσματα");
                    }else {
                        resultLabel.setText(sb.toString());
                    }
                } else {
                    resultLabel.setText("Το ΑΜΚΑ δέν υπάρχει!");
                }
            }
        }catch(NumberFormatException e){
            resultLabel.setText("Το πεδίο ΑΜΚΑ δέχεται μόνο 9 ψηφία!");
        }
    }
}
