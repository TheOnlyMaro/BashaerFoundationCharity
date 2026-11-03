module org.bashaer.bashaerfoundationcharity {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;

    opens org.bashaer.bashaerfoundationcharity.model to javafx.base;
    opens org.bashaer.bashaerfoundationcharity to javafx.fxml;

    exports org.bashaer.bashaerfoundationcharity.dao;
    opens org.bashaer.bashaerfoundationcharity.controller to javafx.fxml;
    opens org.bashaer.bashaerfoundationcharity.pattern.factory to javafx.base;
    opens org.bashaer.bashaerfoundationcharity.pattern.decorator to javafx.base;
    opens org.bashaer.bashaerfoundationcharity.pattern.strategy to javafx.base;
    opens org.bashaer.bashaerfoundationcharity.pattern.observer to javafx.base;
    exports org.bashaer.bashaerfoundationcharity;
}