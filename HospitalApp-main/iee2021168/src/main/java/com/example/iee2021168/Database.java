package com.example.iee2021168;

import com.example.iee2021168.Controllers.Add.AddHisController;
import com.example.iee2021168.Controllers.Edit.EditController;
import com.example.iee2021168.Controllers.HomeController;
import com.example.iee2021168.Controllers.Search.SearchAppointController;
import com.example.iee2021168.Controllers.Search.SearchDocController;
import com.example.iee2021168.Controllers.Search.SearchPatController;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Database {



    /*public static Connection connect() {
        try {
            Class.forName("org.postgresql.Driver");

            String url = "jdbc:postgresql://postgresql-database-aris.i.aivencloud.com:16093/Hospital-app?currentSchema=hospital";

            Properties props = new Properties();
            props.setProperty("user", USER);
            props.setProperty("password", PASSWORD);
            props.setProperty("sslmode", "require");
            Connection connection = DriverManager.getConnection(url, props);

            return connection;
        } catch (Exception e) {
            System.err.println(" Database connection error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }*/
    private static final String USER = "avnadmin";
    private static final String JDBC_URL =
            "jdbc:postgresql://postgresql-database-aris.i.aivencloud.com:16093/Hospital-app?currentSchema=hospital";

    private static HikariDataSource dataSource = null;
    private static String cachedPassword = null;

    private static String getPasswordFromGUI() {
        if (cachedPassword != null) {
            return cachedPassword;
        }

        JPasswordField passwordField = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(
                null,
                passwordField,
                "Κωδικός Βάσης Δεδομένων",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (option == JOptionPane.OK_OPTION) {
            cachedPassword = new String(passwordField.getPassword());
            return cachedPassword;
        } else {
            throw new RuntimeException("Ακυρώσατε την εισαγωγή κωδικού");
        }
    }

    public static void initDataSource() {
        if (dataSource != null) return;

        String password = getPasswordFromGUI();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(JDBC_URL);
        config.setUsername(USER);
        config.setPassword(password);
        config.addDataSourceProperty("sslmode", "require");
        config.setMaximumPoolSize(5); // adjust based on your app

        dataSource = new HikariDataSource(config);
    }

    public static Connection connect() throws SQLException {
        if (dataSource == null) {
            initDataSource();
        }
        return dataSource.getConnection();
    }

    public static void verifyConnectionOrExit() {
        try (Connection conn = connect()) {
            if (conn.isValid(5)) {
                System.out.println("✅ Database connection successful!");
            }
        } catch (RuntimeException e)  {
            String message = e.getMessage().toLowerCase();
            String errorMsg;

            if (message.contains("password") || message.contains("authentication")) {
                errorMsg = "ΛΑΘΟΣ ΚΩΔΙΚΟΣ";
            } else {
                errorMsg = "ΑΔΥΝΑΜΙΑ ΣΥΝΔΕΣΗΣ, ΕΛΕΓΞΤΕ ΤΗΝ ΣΥΝΔΕΣΗ ΣΤΟ ΔΙΑΔΙΚΤΥΟ ΚΑΙ ΠΡΟΣΠΑΘΗΣΤΕ ΑΡΓΟΤΕΡΑ";
            }


            JOptionPane.showMessageDialog(null, errorMsg, "ΑΔΥΝΑΜΙΑ ΣΥΝΔΕΣΗΣ ΒΑΣΗΣ ΔΕΔΟΜΕΝΩΝ", JOptionPane.ERROR_MESSAGE);
            System.exit(1); // stop the application
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }





    public static boolean checkAmkaDoctors(String amka) {
        String sql = "{? = CALL check_amka_doctors(?)}";
        try (Connection conn = connect()) {
            assert conn != null;
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.registerOutParameter(1, Types.BOOLEAN);
                stmt.setString(2, amka);
                stmt.execute();
                return stmt.getBoolean(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkAmkaPatients(String amka) {
        String sql = "{? = CALL check_amka_patients(?)}";
        try (Connection conn = connect()) {
            assert conn != null;
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.registerOutParameter(1, Types.BOOLEAN);
                stmt.setString(2, amka);
                stmt.execute();
                return stmt.getBoolean(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int insertDoc(String amka, String name, int deo, String date) {
        String sql = "{? = CALL insert_doc(?, ?, ?, ?)}";
        int result = -1;
        try (Connection conn = connect()) {
            assert conn != null;
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.registerOutParameter(1, Types.INTEGER);
                stmt.setString(2, amka);
                stmt.setString(3, name);
                stmt.setInt(4, deo);
                stmt.setString(5, date);

                stmt.execute();
                result = stmt.getInt(1);
                //System.out.println("insert result :" + result);
            }
        } catch (SQLException e) {
            System.err.println("sql exception : " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int insertPat(String amka, String name, String date, String phone) {
        String sql = "{? = CALL insert_pat(?, ?, ?, ?)}";
        int result = -1;
        try (Connection conn = connect()) {
            assert conn != null;
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.registerOutParameter(1, Types.INTEGER);
                stmt.setString(2, amka);
                stmt.setString(3, name);
                stmt.setString(4, date);
                stmt.setString(5, phone);

                stmt.execute();
                result = stmt.getInt(1);
                System.out.println(" result :" + result);

            }
        } catch (SQLException e) {
            System.err.println("sql exception : " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getRandomDoc(int dep_id){
        String sql = "{? = CALL get_random_doctor_amka(?)}";
        String result = null;
        try (Connection conn = connect()) {
            assert conn != null;
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.registerOutParameter(1,Types.VARCHAR);
                stmt.setInt(2,dep_id);

                stmt.execute();
                result=stmt.getString(1);
                System.out.println("get_random_doc result : " + result);
            }
        } catch (SQLException e) {
            System.err.println("sql exception : " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void insertAppoint(String pat_amka, String date, String doc_amka) {
        String sql = "{? = CALL insert_appoint(?, ?, ?)}";
        int result = -1;  // Default to -1, indicating failure
        try (Connection conn = connect()) {
            assert conn != null;
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.registerOutParameter(1, Types.INTEGER);

                stmt.setString(2, pat_amka);
                stmt.setString(3, date);
                stmt.setString(4, doc_amka);

                stmt.execute();
                result = stmt.getInt(1);
                System.out.println("insert result : " + result);

            }
        } catch (SQLException e) {
            System.err.println("sql exception : " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<AddHisController.DoctorRecord> getDoctorsByDepartment(int departmentId) {
        List<AddHisController.DoctorRecord> doctors = new ArrayList<>();
        String sql = "{CALL get_doctors_by_department(?)}";
        try (Connection conn = connect()) {
            assert conn != null;
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.setInt(1, departmentId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String name = rs.getString(1);
                        String amka = rs.getString(2);
                        doctors.add(new AddHisController.DoctorRecord(name, amka));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doctors;
    }

    public static int insertHistRecord(String patAmka, String docAmka, int roomId, String docName) {
        int result = -1;
        String sql = "{? = CALL insert_hist(?, ?, ?, ?)}";
        try (Connection conn = connect()) {
            assert conn != null;
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.registerOutParameter(1, Types.INTEGER);
                stmt.setString(2, patAmka);
                stmt.setString(3, docAmka);
                stmt.setInt(4, roomId);
                stmt.setString(5, docName);

                stmt.execute();
                result = stmt.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error during database insert operation: " + e.getSQLState() + " - " + e.getMessage());
        }
        return result;
    }


    public static ObservableList<EditController.Doctor> getDoctors() {
        ObservableList<EditController.Doctor> doctors = FXCollections.observableArrayList();
        String sql = "{call get_all_doctors()}";
        try (Connection conn = connect()) {
            assert conn != null;
            try (CallableStatement stmt = conn.prepareCall(sql);
                 ResultSet rs = stmt.executeQuery()) {
                 while (rs.next()) {
                     String amka = rs.getString(1);
                     String name = rs.getString(2);
                     int departmentId = rs.getInt(3);
                     String birthday = rs.getString(4);
                     double rating = rs.getDouble(5);

                     doctors.add(new EditController.Doctor(amka, name, departmentId, birthday, rating));
                 }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doctors;
    }

    public static boolean deleteDoctorByAmka(String doAmka) {
        String sql = "{CALL delete_doctor_by_amka(?)}";
        try (Connection conn = connect()) {
            assert conn != null;
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.setString(1, doAmka);

                stmt.execute();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean editDoc(String doAmka, double rating){
        String sql = "{CALL edit_doc(?, ?)}";
        try (Connection conn = connect()) {
            assert conn != null;
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.setString(1, doAmka);
                stmt.setDouble(2, rating);

                stmt.execute();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }




    public static ObservableList<EditController.Patient> getPatients() {
        ObservableList<EditController.Patient> patients = FXCollections.observableArrayList();
        String sql = "{call get_all_patients()}";
        try (Connection conn = connect()) {
            assert conn != null;
            try (CallableStatement stmt = conn.prepareCall(sql);
                 ResultSet rs = stmt.executeQuery()) {
                 while (rs.next()) {
                     String amka = rs.getString(1);
                     String name = rs.getString(2);
                     String birthday = rs.getString(3);
                     String phone = rs.getString(4);

                     patients.add(new EditController.Patient(amka, name, birthday, phone));
                 }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patients;
    }

    public static boolean editPat(String patAmka, String  phone){
        String sql = "{CALL edit_pat(?, ?)}";
        try (Connection conn = connect()) {
            assert conn != null;
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.setString(1, patAmka);
                stmt.setString(2, phone);

                stmt.execute();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static ObservableList<EditController.Appointments> getAppointments() {
        ObservableList<EditController.Appointments> appointments = FXCollections.observableArrayList();
        String sql = "{call get_all_appointments()}";
        try (Connection conn = connect()) {
            assert conn != null;
            try (CallableStatement stmt = conn.prepareCall(sql);
                 ResultSet rs = stmt.executeQuery()) {
                 while (rs.next()) {
                     int id = rs.getInt(1);
                     String date = rs.getString(2);
                     String amkaPat = rs.getString(3);
                     String amkaDoc = rs.getString(4);

                     appointments.add(new EditController.Appointments(id, date, amkaPat, amkaDoc));
                 }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }


    public static boolean deletePatientByAmka(String patAmka) {
        String sql = "{CALL delete_patient_by_amka(?)}";
        try (Connection conn = connect()) {
            assert conn != null;
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.setString(1, patAmka);

                stmt.execute();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteAppointmentById(int appointId) {
        String sql = "{CALL delete_appointment_by_id(?)}";
        try (Connection conn = connect()) {
            assert conn != null;
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.setInt(1, appointId);

                stmt.execute();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean editAppoint(int a_id, String  date){
        String sql = "{CALL edit_appoint(?, ?)}";
        try (Connection conn = connect()) {
            assert conn != null;
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.setInt(1, a_id);
                stmt.setString(2, date);

                stmt.execute();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }


    public static void addRating(String doAmka, double rating){
        String sql = "{CALL do_rating_add(?, ?)}";
        try (Connection conn = connect()) {
            assert conn != null;
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.setString(1, doAmka);
                stmt.setDouble(2, rating);
                stmt.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public static ObservableList<SearchDocController.Doctor> getTopRatedDoctors() {
        ObservableList<SearchDocController.Doctor> doctors = FXCollections.observableArrayList();
        String sql = "{CALL get_top_rated_doctors()}";
        try (Connection conn = connect()) {
            assert conn != null;
            try (CallableStatement stmt = conn.prepareCall(sql); ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString(1);
                    String amka = rs.getString(2);
                    double rating = rs.getDouble(3);
                    String departmentId = rs.getString(4);

                    doctors.add(new SearchDocController.Doctor(name, amka, rating, departmentId));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }



    public static ObservableList<SearchDocController.DepartmentDoctorCount> getDoctorCountPerDepartment() {
        ObservableList<SearchDocController.DepartmentDoctorCount> departmentCounts = FXCollections.observableArrayList();
        String sql = "{CALL get_doctor_count_by_department()}";
        try (Connection conn = connect()) {
            assert conn != null;
            try (CallableStatement stmt = conn.prepareCall(sql); ResultSet rs = stmt.executeQuery()) {
                 while (rs.next()) {
                     String depar = rs.getString(1);
                     int doctorCount = rs.getInt(2);

                     departmentCounts.add(new SearchDocController.DepartmentDoctorCount(depar, doctorCount));
                 }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departmentCounts;
    }

    public static ObservableList<SearchDocController.DoctorRecord> getDoctorRelatedRecords(String doAmka) {
        ObservableList<SearchDocController.DoctorRecord> doctorRecords = FXCollections.observableArrayList();
        String sql = "{call get_doctor_related_records(?)}";
        try (Connection conn = connect()) {
            assert conn != null;
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                 stmt.setString(1, doAmka);
                 try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String patAmka = rs.getString(1);
                        String source = rs.getString(2);
                        doctorRecords.add(new SearchDocController.DoctorRecord(patAmka, source));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctorRecords;
    }



    public static ObservableList<SearchPatController.Patient1> get_patient_with_most_history_entries() {
        ObservableList<SearchPatController.Patient1> pat1 = FXCollections.observableArrayList();
        String sql = "{call get_patient_with_most_history_entries()}";
        try (Connection conn = connect()) {
            assert conn != null;
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                 try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String  patAmka = rs.getString(1);
                        int count = rs.getInt(2);
                        pat1.add(new SearchPatController.Patient1(patAmka, count));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pat1;
    }



    public static ObservableList<SearchPatController.Patient2> getPatientAvgAgePerDepartment() {
        ObservableList<SearchPatController.Patient2> patientAgesCounts = FXCollections.observableArrayList();
        String sql = "{call get_patient_avg_age_per_department()}";
        try (Connection conn = connect()) {
            assert conn != null;
            try (CallableStatement stmt = conn.prepareCall(sql);
                 ResultSet rs = stmt.executeQuery()) {
                 while (rs.next()) {
                     String deparId = rs.getString(1);
                     double avgAge = rs.getDouble(2);
                     int patientCount = rs.getInt(3);
                     patientAgesCounts.add(new SearchPatController.Patient2(deparId, avgAge, patientCount));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patientAgesCounts;
    }

    public static ObservableList<SearchPatController.Patient3> getPatientSources(String patAmka) {
        ObservableList<SearchPatController.Patient3> sources = FXCollections.observableArrayList();
        String sql = "{call get_patient_sources(?)}";
        try (Connection conn = connect()) {
            assert conn != null;
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                 stmt.setString(1, patAmka);
                 try (ResultSet rs = stmt.executeQuery()) {
                     while (rs.next()) {
                         String source = rs.getString(1);
                         String doctorName = rs.getString(2);
                         String depName = rs.getString(3);
                         int floor = rs.getInt(4);
                         sources.add(new SearchPatController.Patient3(source, doctorName, depName, floor));
                     }
                 }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sources;
    }

    public static ObservableList<SearchAppointController.Appoint1> getNearAppointments() {
        ObservableList<SearchAppointController.Appoint1> patientAgesCounts = FXCollections.observableArrayList();
        String sql = "{call get_near_appointments()}";
        try (Connection conn = connect()) {
            assert conn != null;
            try (CallableStatement stmt = conn.prepareCall(sql);
                 ResultSet rs = stmt.executeQuery()) {
                 while (rs.next()) {
                     int id = rs.getInt(1);
                     String date = rs.getString(2);
                     String doName = rs.getString(3);
                     String patName = rs.getString(4);

                     patientAgesCounts.add(new SearchAppointController.Appoint1(id, date, doName,patName));
                 }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patientAgesCounts;
    }

    public static ObservableList<SearchAppointController.Appoint2> getCountAppointmentsPerDepartment() {
        ObservableList<SearchAppointController.Appoint2> patientAgesCounts = FXCollections.observableArrayList();
        String sql = "{call get_appointments_count_per_department()}";
        try (Connection conn = connect()) {
            assert conn != null;
            try (CallableStatement stmt = conn.prepareCall(sql);
                 ResultSet rs = stmt.executeQuery()) {
                 while (rs.next()) {
                     String depar = rs.getString(1);
                     int count = rs.getInt(2);


                    patientAgesCounts.add(new SearchAppointController.Appoint2(depar, count));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patientAgesCounts;
    }

    public static ObservableList<SearchAppointController.Appoint3> getAppointByAmka(String patAmka) {
        ObservableList<SearchAppointController.Appoint3> sources = FXCollections.observableArrayList();
        String sql = "{call get_appointments_by_pat_amka(?)}";
        try (Connection conn = connect()) {
            assert conn != null;
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.setString(1, patAmka);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String date = rs.getString(1);
                        String doctorName = rs.getString(2);
                        String depName = rs.getString(3);
                        sources.add(new SearchAppointController.Appoint3(date, doctorName, depName));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sources;
    }


    public static ObservableList<HomeController.Appoints> getAppointmentsLog() {
        ObservableList<HomeController.Appoints> appoints = FXCollections.observableArrayList();
        String sql = "{call get_appointments_log()}";
        try (Connection conn = connect()) {
            assert conn != null;
            try (CallableStatement stmt = conn.prepareCall(sql);
                 ResultSet rs = stmt.executeQuery()) {
                 while (rs.next()) {
                     String type = rs.getString(2);
                     String time = rs.getString(3);
                     int appoint = rs.getInt(4);

                     appoints.add(new HomeController.Appoints(type, time, appoint));
                 }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appoints;
    }

}