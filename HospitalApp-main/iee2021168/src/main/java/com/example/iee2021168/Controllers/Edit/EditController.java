package com.example.iee2021168.Controllers.Edit;


import com.example.iee2021168.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EditController {


    public Button editDocB;
    public Button deleteDocB;
    public TextField new_rating;
    public Button editPatB;
    public Button deletePatB;
    public TextField new_phone;
    public DatePicker new_date;
    public Button editAppointB;
    public Button deleteAppointB;
    public Label labelPat;
    public Label labelDoc;
    public Label labelAppoint;


    public static class Doctor {
        private final String amka;
        private final String name;
        private final int departmentId;
        private final String birthday;
        private double rating;

        public Doctor(String amka, String name, int departmentId, String birthday, double rating) {
            this.amka = amka;
            this.name = name;
            this.departmentId = departmentId;
            this.birthday = birthday;
            this.rating = rating;
        }

        public String getAmka() {
            return amka;
        }

        public String getName() {
            return name;
        }

        public int getDepartmentId() {
            return departmentId;
        }

        public String getBirthday() {
            return birthday;
        }

        public double getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }


    }

    @FXML
    private TableView<Doctor> doctorTable;
    @FXML
    private TableColumn<Doctor, String> doAmkaColumn;
    @FXML
    private TableColumn<Doctor, String> doNameColumn;
    @FXML
    private TableColumn<Doctor, Integer> doDepartmentColumn;
    @FXML
    private TableColumn<Doctor, String> doBirthdayColumn;
    @FXML
    private TableColumn<Doctor, Double> doRatingColumn;




    public static class Patient {
        private final String amka;
        private final String name;
        private final String birthday;
        private String phone;


        public Patient(String amka, String name, String birthday, String phone) {
            this.amka = amka;
            this.name = name;
            this.birthday = birthday;
            this.phone = phone;
        }

        public String getAmka() {
            return amka;
        }
        public String getName() {
            return name;
        }
        public String getBirthday() {
            return birthday;
        }
        public String getPhone() {
            return phone;
        }
        public void setPhone(String phone) {
            this.phone=phone;
        }

    }
    @FXML
    private TableView<Patient> patientTable;
    @FXML
    private TableColumn<Patient , String> patAmkaColumn;
    @FXML
    private TableColumn<Patient , String> patNameColumn;
    @FXML
    private TableColumn<Patient , String> patBirthdayColumn;
    @FXML
    private TableColumn<Patient , String> patPhoneColumn;


    public static class Appointments {
        private final int id;
        private  String date;
        private final String amkaPat;
        private final String  amkaDoc;


        public Appointments(int id, String date, String amkaPat, String amkaDoc) {
            this.id = id;
            this.amkaPat = amkaPat;
            this.amkaDoc = amkaDoc;
            this.date=date;
        }

        public int getId() {
            return id;
        }
        public String getDate() {
            return date;
        }
        public String getAmkaPat() {
            return amkaPat;
        }
        public String getAmkaDoc() {
            return amkaDoc;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
    @FXML
    public TableView<Appointments> appointmentTable;
    @FXML
    public TableColumn<Appointments, Integer> appointIdColumn;
    @FXML
    public TableColumn<Appointments, String> appointDateColumn;
    @FXML
    public TableColumn<Appointments, String> appointDocColumn;
    @FXML
    public TableColumn<Appointments, String> appointPatColumn;


    @FXML
    public void initialize() {
        //Doctor
        doAmkaColumn.setCellValueFactory(new PropertyValueFactory<>("amka"));
        doNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        doDepartmentColumn.setCellValueFactory(new PropertyValueFactory<>("departmentId"));
        doBirthdayColumn.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        doRatingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));

        //Patient
        patAmkaColumn.setCellValueFactory(new PropertyValueFactory<>("amka"));
        patNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        patBirthdayColumn.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        patPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        //Appointment
        appointIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        appointDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        appointPatColumn.setCellValueFactory(new PropertyValueFactory<>("amkaPat"));
        appointDocColumn.setCellValueFactory(new PropertyValueFactory<>("amkaDoc"));


        loadDoctorData();
        loadPatientData();
        loadAppointmentData();

        editDocB.setOnAction(event -> editDoc());
        deleteDocB.setOnAction(event -> deleteDoc());

        editPatB.setOnAction(event -> editPat());
        deletePatB.setOnAction(event -> deletePat());

        editAppointB.setOnAction(event -> editAppoint());
        deleteAppointB.setOnAction(event -> deleteAppoint());

        new_date.getEditor().setDisable(true);
        new_date.getEditor().setStyle("-fx-opacity: 1;");



        new_date.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });


    }

    private void loadDoctorData() {
        ObservableList<Doctor> doctors = Database.getDoctors();
        doctorTable.setItems(doctors);
    }

    private void loadPatientData(){
        ObservableList<Patient> patients=Database.getPatients();
        patientTable.setItems(patients);
    }

    private void loadAppointmentData(){
        ObservableList<Appointments> appointments=Database.getAppointments();
        appointmentTable.setItems(appointments);
    }

    private void editDoc() {
        Doctor selectedDoctor = doctorTable.getSelectionModel().getSelectedItem();
        if (selectedDoctor != null) {
            String doctorAMKA = selectedDoctor.getAmka();
            if (!new_rating.getText().isEmpty()) {
                if (new_rating.getText().matches("\\d+")) {
                    try {
                        double editedRating = Double.parseDouble(new_rating.getText());
                        boolean result = Database.editDoc(doctorAMKA, editedRating);
                        if (result) {
                            ObservableList<Doctor> updatedDoctors = Database.getDoctors();
                            doctorTable.setItems(FXCollections.observableArrayList(updatedDoctors));
                            labelDoc.setText("Επιτυχης ενημέρωση");
                            new_rating.setText("");
                        }
                    } catch (NumberFormatException e) {
                        labelDoc.setText("Το πεδίο δέχεται μόνο ακέραιους αριθμούς!");
                    }
                }
            } else {
                labelDoc.setText("Το πεδίο είναι κενό!");
            }
        } else {
            labelDoc.setText("Δεν έχει επιλεγεί γιατρός!");
        }
    }

    private void editPat() {
        Patient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            String patAmka = selectedPatient.getAmka();
            if (!new_phone.getText().isEmpty()) {
                if (new_phone.getText().matches("[0-9]+")) {
                    boolean result = Database.editPat(patAmka, new_phone.getText());
                    if (result) {
                        ObservableList<Patient> updatedPatients = Database.getPatients();
                        patientTable.setItems(FXCollections.observableArrayList(updatedPatients));
                        labelPat.setText("Επιτυχης ενημέρωση");
                        new_phone.setText("");
                    }
                }
                else{
                    labelPat.setText("Το πεδίο δέχεται μόνο αριθμούς!");
                }
            } else {
                labelPat.setText("Το πεδίο είναι κενό!");
            }
        } else {
            labelPat.setText("Δεν έχει επιλεγεί ασθενής!");
        }
    }

    private void editAppoint() {
        Appointments selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            int appointId = selectedAppointment.getId();
            if (new_date.getValue() != null) {
                LocalDate selectedDate = new_date.getValue();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String formattedDate = selectedDate.format(formatter);
                boolean result = Database.editAppoint(appointId, formattedDate);
                if (result) {
                    ObservableList<Appointments> updatedAppointments = Database.getAppointments();
                    appointmentTable.setItems(FXCollections.observableArrayList(updatedAppointments));
                    labelAppoint.setText("Επιτυχης ενημέρωση");
                    new_date.setValue(null);
                }
            } else {
                labelAppoint.setText("Το πεδίο είναι κενό!");
            }
        } else {
            labelAppoint.setText("Δεν έχει επιλεγεί ραντεβού!");
        }
    }



    private void deleteDoc() {
        Doctor selectedDoctor = doctorTable.getSelectionModel().getSelectedItem();
        if (selectedDoctor != null) {
            String doctorAMKA = selectedDoctor.getAmka();
            boolean result = Database.deleteDoctorByAmka(doctorAMKA);
            if (result) {
                ObservableList<Doctor> updatedDoctors = Database.getDoctors();
                ObservableList<Doctor> updatedList = FXCollections.observableArrayList(updatedDoctors);
                doctorTable.setItems(updatedList);
                labelDoc.setText("Επιτυχής διαγραφή");
            } else {
                labelDoc.setText("Αποτυχία διαγραφής ");
            }
        } else {
            labelDoc.setText("Δεν έχει επιλεγεί γιατρός");
        }
    }

    private void deletePat() {
        Patient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            String patientAmka = selectedPatient.getAmka();
            boolean result = Database.deletePatientByAmka(patientAmka);
            if (result) {

                ObservableList<Patient> updatedPatients = Database.getPatients();
                ObservableList<Patient> updatedList = FXCollections.observableArrayList(updatedPatients);

                patientTable.setItems(updatedList);
                labelPat.setText("Επιτυχής διαγραφή");
            } else {
                labelPat.setText("Αποτυχία διαγραφής");
            }
        } else {
            labelPat.setText("Δεν έχει επιλεγεί ασθενής");
        }
    }


    private void deleteAppoint() {
        Appointments selectedAppoint = appointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppoint != null) {
            int appointId = selectedAppoint.getId();
            boolean result = Database.deleteAppointmentById(appointId);
            if (result) {

                ObservableList<Appointments> updatedAppointments = Database.getAppointments();
                ObservableList<Appointments> updatedList = FXCollections.observableArrayList(updatedAppointments);

                appointmentTable.setItems(updatedList);
                labelAppoint.setText("Επιτυχής διαγραφή");
            } else {
                labelAppoint.setText("Αποτυχία διαγραφής ");
            }
        } else {

            labelAppoint.setText("Δεν έχει επιλεγεί ραντεβού");
        }
    }

}

