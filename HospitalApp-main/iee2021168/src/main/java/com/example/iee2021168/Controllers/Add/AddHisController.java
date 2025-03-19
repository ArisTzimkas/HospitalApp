package com.example.iee2021168.Controllers.Add;

import com.example.iee2021168.Database;
import com.example.iee2021168.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddHisController implements Initializable {
    public Button addHisBackB;
    public Button saveB;
    public TextField amkaField;
    public ChoiceBox<String> ChoiceDepar;
    public ChoiceBox<String> ChoiceRoom;
    public ChoiceBox<DoctorRecord> ChoiceDoc; // Change to store DoctorRecord objects
    public Label label;
    public Button verifyB;
    public Button goToPatB;

    private List<DoctorRecord> doctorRecords;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addHisBackB.setOnAction(event -> backToAdd());
        verifyB.setOnAction(event -> verifyAMKA());
        ChoiceDepar.setOnAction(event -> loadChoiceData());
        goToPatB.setOnAction(event -> goToAddPat());
        saveB.setOnAction(event -> save());

        resetForm();

        ChoiceDepar.getItems().addAll("Καρδιολογικό", "Πνευμονολογικό", "Παθολογικό", "Νευρολογικό");

        
        ChoiceDepar.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                ChoiceRoom.setDisable(false);
                ChoiceDoc.setDisable(false);
            } else {
                ChoiceRoom.setDisable(true);
                ChoiceDoc.setDisable(true);
            }
        });
    }

    private void backToAdd() {
        Model.getInstance().getViewFactory().getBackView().set("");
        Model.getInstance().getViewFactory().getBackView().set("backToAdd");
    }

    private void goToAddPat(){
        Model.getInstance().getViewFactory().getAddSelectedView().set("");
        Model.getInstance().getViewFactory().getAddSelectedView().set("addPat");
    }

    private void verifyAMKA() {
        try {
            String amkaText = amkaField.getText();
            if (amkaText.isEmpty()) {
                label.setText("Το πεδίο ΑΜΚΑ δεν μπορεί να είναι κενό!");
                resetForm();
                return;
            }

            label.setVisible(true);
            int amka = Integer.parseInt(amkaText);
            boolean exists = Database.checkAmkaPatients(amka);

            if (!exists) {
                resetForm();
                goToPatB.setVisible(true);
                label.setText("Το ΑΜΚΑ ασθενή δεν υπάρχει, πατήστε το κουμπί 'Δημιουργία'");
            } else {
                ChoiceDepar.setDisable(false);
                saveB.setDisable(false);
                label.setText("Το ΑΜΚΑ μπορεί να χρησιμοποιηθεί, συμπληρώστε τα επόμενα πεδία.");
            }
        } catch (NumberFormatException e) {
            resetForm();
            label.setText("Το πεδίο ΑΜΚΑ δέχεται μόνο 9 ψηφία!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadChoiceData() {
        String department = ChoiceDepar.getValue();
        if (department != null && !department.isEmpty()) {
            int departmentId = getDepartmentId(department);
            populateRoomChoiceBox(departmentId);
            populateDoctorChoiceBox(departmentId);

            ChoiceRoom.setDisable(false);
            ChoiceDoc.setDisable(false);
        } else {
            ChoiceRoom.setDisable(true);
            ChoiceDoc.setDisable(true);
        }
    }

    private void populateRoomChoiceBox(int departmentId) {
        ChoiceRoom.getItems().clear();
        switch (departmentId) {
            case 1 -> ChoiceRoom.getItems().addAll("1", "2", "3");
            case 2 -> ChoiceRoom.getItems().addAll("4", "5", "6");
            case 3 -> ChoiceRoom.getItems().addAll("7", "8", "9");
            case 4 -> ChoiceRoom.getItems().addAll("10", "11", "12");
        }
    }

    private void populateDoctorChoiceBox(int departmentId) {
        ChoiceDoc.getItems().clear();
        doctorRecords = Database.getDoctorsByDepartment(departmentId);
        for (DoctorRecord doctor : doctorRecords) {
            ChoiceDoc.getItems().add(doctor);
        }
    }

    private int getDepartmentId(String department) {
        return switch (department) {
            case "Καρδιολογικό" -> 1;
            case "Πνευμονολογικό" -> 2;
            case "Παθολογικό" -> 3;
            case "Νευρολογικό" -> 4;
            default -> -1;
        };
    }

    public record DoctorRecord(String doctorName, int doctorAmka) {
        @Override
        public String toString() {
            return doctorName;
        }
    }

    private void save() {
        try {
            int amka = Integer.parseInt(amkaField.getText());
            DoctorRecord selectedDoctor = ChoiceDoc.getValue();
            if (selectedDoctor == null) {
                label.setText("Επιλέξτε γιατρό!");
                return;
            }

            int docAmka = selectedDoctor.doctorAmka();
            System.out.println(""+docAmka);
            String selectedRoom = ChoiceRoom.getValue();
            if (selectedRoom == null || selectedRoom.isEmpty()) {
                label.setText("Επιλέξτε δωμάτιο!");
                return;
            }
            int roomId = Integer.parseInt(selectedRoom);
            int result = Database.insertHistRecord(amka, docAmka, roomId, selectedDoctor.doctorName);

            if (result > 0) {
                label.setText("Επιτυχής προσθήκη!");
                double rating = 0.5;
                Database.addRating(docAmka, rating);
                resetForm();
                clearFormFields();
            } else {
                label.setText("Σφάλμα!");
            }
        } catch (NumberFormatException e) {
            label.setText("Το πεδίο ΑΜΚΑ δέχεται μόνο 10 ψηφία!");
        } catch (Exception e) {
            System.err.println("Σφάλμα στην αποθήκευση: " + e.getMessage());
            e.printStackTrace();
            label.setText("Σφάλμα στην αποθήκευση.");
        }
    }

    private void resetForm() {
        ChoiceDepar.setDisable(true);
        ChoiceDepar.setValue(null);
        ChoiceRoom.setDisable(true);
        ChoiceRoom.setValue(null);
        ChoiceDoc.setDisable(true);
        ChoiceDoc.setValue(null);
        saveB.setDisable(true);
        goToPatB.setVisible(false);
        amkaField.clear();
    }

    private void clearFormFields() {
        amkaField.clear();
        ChoiceDoc.setValue(null);
        ChoiceRoom.setValue(null);
        ChoiceDepar.setValue(null);
    }
}
