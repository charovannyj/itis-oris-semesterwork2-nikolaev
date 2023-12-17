module ru.kpfu.itis.itisorissemesterwork2nikolaev {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens ru.kpfu.itis.itisorissemesterwork2nikolaev to javafx.fxml;
    exports ru.kpfu.itis.itisorissemesterwork2nikolaev;
}