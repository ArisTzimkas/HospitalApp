package com.example.iee2021168.Controllers.Add;

import com.example.iee2021168.Database;
import com.example.iee2021168.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AddAppointController implements Initializable {
    public TextField amkaField;
    public Button saveB;
    public Button verifyB;
    public ChoiceBox<String> ChoiceDepar;
    public Button goToPatB;
    public DatePicker dateField;
    public Button addAppointBackB;
    public Label label;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addAppointBackB.setOnAction(event -> backToAdd());
        verifyB.setOnAction(event -> verifyAMKA());
        goToPatB.setOnAction(event -> goToAddPat());
        saveB.setOnAction(event -> save());

        ChoiceDepar.setDisable(true);
        dateField.setDisable(true);
        saveB.setDisable(true);
        goToPatB.setVisible(false);

        dateField.getEditor().setDisable(true);
        dateField.getEditor().setStyle("-fx-opacity: 1;");

        ChoiceDepar.getItems().addAll("Καρδιολογικό", "Πνευμονολογικό", "Παθολογικό", "Νευρολογικό");


        dateField.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
    }

    private void backToAdd() {
        Model.getInstance().getViewFactory().getBackView().set("");
        Model.getInstance().getViewFactory().getBackView().set("backToAdd");
    }

    private void verifyAMKA() {
        try {
            String amkaText = amkaField.getText();
            if (amkaText.isEmpty()) {
                label.setText("Το πεδίο ΑΜΚΑ δεν μπορεί να είναι κενό!");
                ChoiceDepar.setDisable(true);
                dateField.setDisable(true);
                saveB.setDisable(true);
                goToPatB.setVisible(false);
                amkaField.clear();
                ChoiceDepar.setValue(null);
                dateField.setValue(null);
                return;
            }
            if (!amkaText.matches("\\d{11}")) {
                label.setText("Το ΑΜΚΑ πρέπει να περιέχει ακριβώς 11 ψηφία!");
                return;
            }

            label.setVisible(true);
            boolean exists = Database.checkAmkaPatients(amkaText);


            if (!exists) {
                ChoiceDepar.setDisable(true);
                dateField.setDisable(true);
                saveB.setDisable(true);
                goToPatB.setVisible(true);
                amkaField.clear();
                ChoiceDepar.setValue(null);
                dateField.setValue(null);
                label.setText("Το ΑΜΚΑ ασθενή δεν υπάρχει, πατήστε το κουμπι 'Δημιουργία'");

            } else {
                ChoiceDepar.setDisable(false);
                dateField.setDisable(false);
                saveB.setDisable(false);
                goToPatB.setVisible(false);
                label.setText("Το ΑΜΚΑ μπορεί να χρησιμοποιηθεί, συμπληρώστε τα επόμενα πεδία.");
            }
        } catch (NumberFormatException e) {
            label.setText("Το πεδίο ΑΜΚΑ δέχεται μόνο 9 ψηφία!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goToAddPat(){
        Model.getInstance().getViewFactory().getAddSelectedView().set("");
        Model.getInstance().getViewFactory().getAddSelectedView().set("addPat");
    }


    private void save() {
        try {
            if (amkaField.getText().isEmpty() || dateField.getValue() == null || ChoiceDepar.getValue() == null) {
                label.setText("Συμπληρώστε όλα τα πεδία!");
                return;
            }

            String amka =amkaField.getText();
            LocalDate selectedDate = dateField.getValue();
            String department = ChoiceDepar.getValue();

            if (!amka.matches("\\d{11}")) {
                label.setText("Το ΑΜΚΑ πρέπει να περιέχει ακριβώς 11 ψηφία!");
                return;
            }

            int departmentId = getDepartmentId(department);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = selectedDate.format(formatter);
            String docAmka = Database.getRandomDoc(departmentId);

            if(docAmka==null){
                throw new Exception("Δεν υπάρχει γιατρός σε αυτό το τμήμα.");
            }
            Database.insertAppoint(amka, formattedDate, docAmka);
            label.setText("Επιτυχής προσθήκη!");
            double rating=0.3;
            Database.addRating(docAmka,rating);

            ChoiceDepar.setDisable(true);
            dateField.setDisable(true);
            saveB.setDisable(true);
            goToPatB.setVisible(false);
            amkaField.clear();
            ChoiceDepar.setValue(null);
            dateField.setValue(null);




        } catch (NumberFormatException e) {
            label.setText("Το πεδίο ΑΜΚΑ δέχεται μόνο 10 ψηφία!");
        }
        catch (Exception e) {
            label.setText(e.getMessage());
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
}
