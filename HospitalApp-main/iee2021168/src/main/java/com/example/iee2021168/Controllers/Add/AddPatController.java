package com.example.iee2021168.Controllers.Add;

import com.example.iee2021168.Database;
import com.example.iee2021168.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AddPatController implements Initializable {
    public Button addPatBackB;
    public TextField amkaField;
    public TextField nameField;
    public DatePicker dateField;
    public TextField phoneField;
    public Button verifyB;
    public Button saveB;
    public Label label;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addPatBackB.setOnAction(event -> backToAdd());
        verifyB.setOnAction(event -> verifyAMKA());
        saveB.setOnAction(event -> save());


        nameField.setDisable(true);
        dateField.setDisable(true);
        phoneField.setDisable(true);
        saveB.setDisable(true);

        dateField.getEditor().setDisable(true);
        dateField.getEditor().setStyle("-fx-opacity: 1;");

        dateField.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(LocalDate.now()));
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

                nameField.setDisable(true);
                dateField.setDisable(true);
                phoneField.setDisable(true);
                saveB.setDisable(true);

                dateField.getEditor().setDisable(true);
                dateField.getEditor().setStyle("-fx-opacity: 1;");

                return;
            }

            boolean exists = Database.checkAmkaPatients(amkaText);


            if (!exists) {
                nameField.setDisable(false);
                dateField.setDisable(false);
                phoneField.setDisable(false);
                saveB.setDisable(false);

                label.setText("Το ΑΜΚΑ μπορεί να χρησιμοποιηθεί, συμπληρώστε τα επόμενα πεδία.");

            } else {
                nameField.setDisable(true);
                dateField.setDisable(true);
                dateField.getEditor().setStyle("-fx-opacity: 1;");
                phoneField.setDisable(true);
                saveB.setDisable(true);
                label.setText("Το ΑΜΚΑ υπάρχει ήδη!");
            }
        } catch (NumberFormatException e) {
            label.setText("Το πεδίο ΑΜΚΑ δέχεται μόνο 9 ψηφία!");
            nameField.setDisable(true);
            dateField.setDisable(true);
            phoneField.setDisable(true);
            saveB.setDisable(true);

            dateField.getEditor().setDisable(true);
            dateField.getEditor().setStyle("-fx-opacity: 1;");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void save() {
        try {
            // Ensure that all fields are filled
            if (amkaField.getText().isEmpty() || nameField.getText().isEmpty() || dateField.getValue() == null || phoneField.getText().isEmpty()) {
                label.setText("Συμπληρώστε όλα τα πεδία!");
                return;
            }

            String amka =amkaField.getText();
            String name = nameField.getText();
            String phone = phoneField.getText();
            LocalDate selectedDate = dateField.getValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = selectedDate.format(formatter);
            int result = Database.insertPat(amka, name, formattedDate, phone);

            if (result == 1) {
                label.setText("Επιτυχής προσθήκη!");
                amkaField.clear();
                nameField.clear();
                phoneField.clear();
                dateField.setValue(null);
                dateField.setDisable(true);
                nameField.setDisable(true);
                phoneField.setDisable(true);
            } else {
                label.setText("Προέκυψε κάποιο σφάλμα, ελέγξτε αν υπάρχει ήδη το ΑΜΚΑ.");
            }

        } catch (NumberFormatException e) {
            label.setText("Το πεδίο ΑΜΚΑ δέχεται μόνο 9 ψηφία!");
        } catch (Exception e) {
            System.err.println("Σφάλμα στην αποθήκευση: " + e.getMessage());
            e.printStackTrace();
            label.setText("Σφάλμα στην αποθήκευση.");
        }
    }
}

