module com.himanshu.clientapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.himanshu.clientapp to javafx.fxml;
    exports com.himanshu.clientapp;
}