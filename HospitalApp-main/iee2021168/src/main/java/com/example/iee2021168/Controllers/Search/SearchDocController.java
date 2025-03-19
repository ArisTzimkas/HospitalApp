package com.example.iee2021168.Controllers.Search;

import com.example.iee2021168.Database;
import com.example.iee2021168.Models.Model;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class SearchDocController implements Initializable {
    public Button searchDocBackB;
    public ChoiceBox<String> ChoiceDocType;
    public Label resultsLabel;
    public TextField amkaField;
    public Text textAMKA;
    public Button SearchAMKAB;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchDocBackB.setOnAction(event -> backToSearch());
        ChoiceDocType.getItems().addAll("Υψηλότερη βαθμολογία γιατρού ανα τμήμα","Πλήθος γιατρών ανα τμήμα","Ιστορικό γιατρού");
        ChoiceDocType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> { handleChoiceSelection(newValue); });

        textAMKA.setVisible(false);
        amkaField.setVisible(false);
        SearchAMKAB.setVisible(false);

        SearchAMKAB.setOnAction(event -> search());
    }

    public void backToSearch() {
        Model.getInstance().getViewFactory().getBackView().set("");
        Model.getInstance().getViewFactory().getBackView().set("backToSearch");
    }



    public record Doctor(String name, int amka, double rating, String departmentId) {}
    public record DepartmentDoctorCount(String depar, int doctorCount) {}
    public record DoctorRecord(int patAmka, String source) {}



    private void handleChoiceSelection(String newValue) {
        if (newValue != null) {
            StringBuilder sb;
            switch (newValue) {
                case "Υψηλότερη βαθμολογία γιατρού ανα τμήμα":

                    textAMKA.setVisible(false);
                    amkaField.setVisible(false);
                    SearchAMKAB.setVisible(false);
                    amkaField.setText("");

                    ObservableList<Doctor> doctors = Database.getTopRatedDoctors();
                    sb = new StringBuilder();
                    for (Doctor doc : doctors) {
                        sb.append("⫸ Όνομα: ").append(doc.name())
                                .append(" | AMKA: ").append(doc.amka())
                                .append(" | Βαθμολογία: ").append(doc.rating())
                                .append(" | Τμήμα: ").append(doc.departmentId())
                                .append("\n\n");
                    }
                    resultsLabel.setText(sb.toString());
                    break;
                case "Πλήθος γιατρών ανα τμήμα":

                    textAMKA.setVisible(false);
                    amkaField.setVisible(false);
                    SearchAMKAB.setVisible(false);
                    amkaField.setText("");

                    ObservableList<DepartmentDoctorCount> departmentCounts = Database.getDoctorCountPerDepartment();
                    sb = new StringBuilder();
                    for (DepartmentDoctorCount count : departmentCounts) {
                        sb.append("⫸ Τμήμα: ").append(count.depar())
                                .append(" | Πλήθος γιατρών: ").append(count.doctorCount())
                                .append("\n\n");
                    }
                    resultsLabel.setText(sb.toString());
                    break;
                case "Ιστορικό γιατρού":
                    textAMKA.setVisible(true);
                    amkaField.setVisible(true);
                    SearchAMKAB.setVisible(true);
                    break;
                default:
                    break;
            }
        }
    }

    public void search(){
        try {
            if(amkaField.getText().isEmpty()) {
                resultsLabel.setText("Το πεδίο ΑΜΚΑ δεν μπορεί να είναι κενό!");
            }else {
                int amka = Integer.parseInt(amkaField.getText());
                boolean exists = Database.checkAmkaDoctors(amka);
                if (exists) {
                    ObservableList<DoctorRecord> docRec = Database.getDoctorRelatedRecords(amka);
                    StringBuilder sb = new StringBuilder();
                    for (DoctorRecord count : docRec) {
                        sb.append("⫸ ΑΜΚΑ Ασθενή : ").append(count.patAmka())
                                .append(" | Κατηγορία : ").append(count.source())
                                .append("\n\n");
                    }
                    if(sb.toString().isEmpty()) {
                        resultsLabel.setText("Δεν βρέθηκαν αποτελέσματα");
                    }else{
                        resultsLabel.setText(sb.toString());
                    }
                } else {
                    resultsLabel.setText("Το ΑΜΚΑ δέν υπάρχει!");
                }
            }
        } catch (NumberFormatException e) {
            resultsLabel.setText("Το πεδίο ΑΜΚΑ δέχεται μόνο 9 ψηφία!");
        }
    }
}







