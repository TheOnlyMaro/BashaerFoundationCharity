module org.bashaer.bashaerfoundationcharity {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;

    opens org.bashaer.bashaerfoundationcharity to javafx.fxml;
    exports org.bashaer.bashaerfoundationcharity;
}