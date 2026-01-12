module fr.projet.factures {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires atlantafx.base;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires com.github.librepdf.openpdf;
    requires java.desktop;

    opens fr.projet.factures to javafx.fxml;
    opens fr.projet.factures.view to javafx.fxml;
    opens fr.projet.factures.controller to javafx.fxml;
    // Open model package to Hibernate for reflection
    opens fr.projet.factures.model to org.hibernate.orm.core, javafx.base;

    exports fr.projet.factures;
}