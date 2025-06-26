package com.example.iee2021168.Controllers.Add;

import com.example.iee2021168.Database;
import com.example.iee2021168.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AddDocController implements Initializable {
    public Button addDocBackB;
    public Button verifyB;
    public Button saveB;
    public TextField amkaField;
    public TextField nameField;
    public DatePicker dateField;
    public Label resultLabel;
    public ChoiceBox<String> ChoiceDepar;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addDocBackB.setOnAction(event -> backToAdd());
        verifyB.setOnAction(event -> verifyAMKA());
        saveB.setOnAction(event -> save());


        saveB.setDisable(true);
        nameField.setDisable(true);
        dateField.setDisable(true);
        ChoiceDepar.setDisable(true);


        dateField.getEditor().setDisable(true);
        dateField.getEditor().setStyle("-fx-opacity: 1;");

        ChoiceDepar.getItems().addAll("Καρδιολογικό", "Πνευμονολογικό", "Παθολογικό", "Νευρολογικό");


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
                resultLabel.setText("Το πεδίο ΑΜΚΑ δεν μπορεί να είναι κενό!");
                saveB.setDisable(true);
                nameField.setDisable(true);
                dateField.setDisable(true);
                ChoiceDepar.setDisable(true);
                return;
            }

            boolean exists = Database.checkAmkaDoctors(amkaText);


            if (!exists) {
                nameField.setDisable(false);
                dateField.setDisable(false);
                saveB.setDisable(false);
                ChoiceDepar.setDisable(false);
                resultLabel.setText("Το ΑΜΚΑ μπορεί να χρησιμοποιηθεί, συμπληρώστε τα επόμενα πεδία.");

            } else {
                saveB.setDisable(true);
                nameField.setDisable(true);
                dateField.setDisable(true);
                ChoiceDepar.setDisable(true);
                resultLabel.setText("Το ΑΜΚΑ υπάρχει ήδη!");
            }
        } catch (NumberFormatException e) {
            saveB.setDisable(true);
            nameField.setDisable(true);
            dateField.setDisable(true);
            ChoiceDepar.setDisable(true);
            resultLabel.setText("Το πεδίο ΑΜΚΑ δέχεται μόνο 9 ψηφία!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void save() {
        try {
            if (amkaField.getText().isEmpty() || nameField.getText().isEmpty() || dateField.getValue() == null || ChoiceDepar.getValue() == null) {
                resultLabel.setText("Συμπληρώστε όλα τα πεδία!");
                return;
            }
            String amka =amkaField.getText();
            String name = nameField.getText();
            LocalDate selectedDate = dateField.getValue();
            String department = ChoiceDepar.getValue();
            int departmentId = getDepartmentId(department);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = selectedDate.format(formatter);
            int result = Database.insertDoc(amka, name, departmentId, formattedDate);
            if (result == 1) {
                resultLabel.setText("Επιτυχής προσθήκη!");
                amkaField.clear();
                nameField.clear();
                ChoiceDepar.setValue(null);
                dateField.setValue(null);

                ChoiceDepar.setDisable(true);
                dateField.setDisable(true);
                nameField.setDisable(true);
                saveB.setDisable(true);
            } else {
                resultLabel.setText("Προέκυψε κάποιο σφάλμα, ελέγξτε αν υπάρχει ήδη το ΑΜΚΑ.");
            }

        } catch (NumberFormatException e) {
            resultLabel.setText("Το πεδίο ΑΜΚΑ δέχεται μόνο 9 ψηφία!");
        } catch (Exception e) {
            System.err.println("Σφάλμα στην αποθήκευση: " + e.getMessage());
            e.printStackTrace();
            resultLabel.setText("Σφάλμα στην αποθήκευση.");
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
