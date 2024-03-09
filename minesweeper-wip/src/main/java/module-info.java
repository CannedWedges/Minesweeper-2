module org.example.minesweeper {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.almasb.fxgl.all;

    opens org.example.minesweeper to javafx.fxml;
    exports org.example.minesweeper;
}