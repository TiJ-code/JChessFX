module dk.tij.jchessfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens dk.tij.jchessfx to javafx.fxml;
    exports dk.tij.jchessfx;
    exports dk.tij.jchessfx.dialogs;
    opens dk.tij.jchessfx.dialogs to javafx.fxml;
}