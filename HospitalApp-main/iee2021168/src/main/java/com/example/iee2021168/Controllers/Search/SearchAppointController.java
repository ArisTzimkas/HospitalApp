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

public class SearchAppointController implements Initializable {
    public Button searchAppointBackB;
    public Label resultLabel;
    public Button SearchAMKAB;
    public TextField amkaField;
    public Text textAMKA;
    public ChoiceBox<String> ChoiceAppointType;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchAppointBackB.setOnAction(event -> backToSearch());

        ChoiceAppointType.getItems().addAll("Ραντεβού των επόμενων 3 ημερών","Πλήθος ραντεβού ανα τμήμα","Ραντεβού ασθενή");
        ChoiceAppointType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> { handleChoiceSelection(newValue); });


        textAMKA.setVisible(false);
        amkaField.setVisible(false);
        SearchAMKAB.setVisible(false);

        SearchAMKAB.setOnAction(event -> search());


    }

    public void backToSearch() {
        Model.getInstance().getViewFactory().getBackView().set("");
        Model.getInstance().getViewFactory().getBackView().set("backToSearch");
    }

    public record Appoint1(int id,String date,String doc,String pat){}
    public record Appoint2(String depar,int count){}
    public record Appoint3(String date,String doc,String pat){}



    private void handleChoiceSelection(String newValue) {
        if (newValue != null) {
            StringBuilder sb;
            switch (newValue) {
                case "Ραντεβού των επόμενων 3 ημερών":
                    textAMKA.setVisible(false);
                    amkaField.setVisible(false);
                    SearchAMKAB.setVisible(false);
                    amkaField.setText("");
                    ObservableList<SearchAppointController.Appoint1> a1 = Database.getNearAppointments();
                    sb = new StringBuilder();
                    for (SearchAppointController.Appoint1 p : a1) {
                        sb.append("⫸ Κωδικός : ").append(p.id())
                                .append(" | Ημερομηνία : ").append(p.date())
                                .append(" | Γιατρός : ").append(p.doc())
                                .append(" | Ασθενής : ").append(p.pat())
                                .append("\n\n");
                    }
                    if(sb.toString().isEmpty()){
                        resultLabel.setText("Δεν βρέθηκαν αποτελέσματα");
                    }else {
                        resultLabel.setText(sb.toString());
                    }
                    break;

                case "Πλήθος ραντεβού ανα τμήμα":
                    textAMKA.setVisible(false);
                    amkaField.setVisible(false);
                    SearchAMKAB.setVisible(false);
                    amkaField.setText("");
                    ObservableList<SearchAppointController.Appoint2> a2 = Database.getCountAppointmentsPerDepartment();
                    sb = new StringBuilder();
                    for (SearchAppointController.Appoint2 a : a2) {
                        sb.append("⫸ Τμήμα : ").append(a.depar())
                                .append(" | Πλήθος ραντεβού : ").append(a.count())
                                .append("\n\n");
                    }
                    if(sb.toString().isEmpty()){
                        resultLabel.setText("Δεν βρέθηκαν αποτελέσματα");
                    }else {
                        resultLabel.setText(sb.toString());
                    }
                    break;

                case "Ραντεβού ασθενή":
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
        try{
            if (amkaField.getText().isEmpty()) {
                resultLabel.setText("Το πεδίο ΑΜΚΑ δεν μπορεί να είναι κενό!");
            }else {
                int amka = Integer.parseInt(amkaField.getText());
                boolean exists = Database.checkAmkaPatients(amka);
                if (exists) {
                    ObservableList<SearchAppointController.Appoint3> a3 = Database.getAppointByAmka(amka);
                    StringBuilder sb = new StringBuilder();
                    for (SearchAppointController.Appoint3 a : a3) {
                        sb.append("⫸ Ημερομηνία : ").append(a.date())
                                .append(" | Γιατρός : ").append(a.doc())
                                .append(" | Τμήμα : ").append(a.pat())
                                .append("\n\n");
                    }
                    if(sb.toString().isEmpty()){
                        resultLabel.setText("Δεν βρέθηκαν αποτελέσματα");
                    }else {
                        resultLabel.setText(sb.toString());
                    }
                } else {
                    resultLabel.setText("Το ΑΜΚΑ δέν υπάρχει!");
                }
            }
        } catch (NumberFormatException e) {
                resultLabel.setText("Το πεδίο ΑΜΚΑ δέχεται μόνο 9 ψηφία!");
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
