module com.example.iee2021168 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires com.zaxxer.hikari;


    opens com.example.iee2021168 to javafx.fxml;
    exports com.example.iee2021168;
    exports com.example.iee2021168.Controllers;
    opens com.example.iee2021168.Controllers to javafx.fxml;
    exports com.example.iee2021168.Controllers.Add;
    opens com.example.iee2021168.Controllers.Add to javafx.fxml;
    exports com.example.iee2021168.Controllers.Edit;
    opens com.example.iee2021168.Controllers.Edit to javafx.fxml;
    exports com.example.iee2021168.Controllers.Search;
    opens com.example.iee2021168.Controllers.Search to javafx.fxml;
}