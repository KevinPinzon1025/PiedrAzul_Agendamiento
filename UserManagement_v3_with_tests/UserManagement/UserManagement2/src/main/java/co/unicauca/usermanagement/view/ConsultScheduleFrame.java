package co.unicauca.usermanagement.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConsultScheduleFrame {

    private Stage stage;

    //  CONTENEDOR DINÁMICO (aquí cambia todo)
    private VBox contentContainer;

    public ConsultScheduleFrame(Stage owner) {
        stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Consultar horarios");

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: rgba(0,0,0,0.25);");

        VBox modal = createModal();

        root.getChildren().add(modal);

        Scene scene = new Scene(root, 950, 600);
        stage.setScene(scene);
    }

    public void show() {
        stage.showAndWait();
    }

    private VBox createModal() {

        VBox container = new VBox(20);
        container.setPadding(new Insets(25));
        container.setMaxWidth(800);
        container.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 20,0,0,4);"
        );

        Label title = new Label("Consultar horarios");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        HBox tabButtons = createTabButtons();

        contentContainer = new VBox();
        contentContainer.setSpacing(15);

        //  por defecto mostramos horarios
        showAvailableSchedules();

        container.getChildren().addAll(title, tabButtons, contentContainer);

        return container;
    }

    private HBox createTabButtons() {

        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);

        Button btnSchedules = new Button("Horarios disponibles");
        Button btnAppointments = new Button("Mis citas");

        styleTabButton(btnSchedules, true);
        styleTabButton(btnAppointments, false);

        btnSchedules.setOnAction(e -> {
            styleTabButton(btnSchedules, true);
            styleTabButton(btnAppointments, false);
            showAvailableSchedules();
        });

        btnAppointments.setOnAction(e -> {
            styleTabButton(btnSchedules, false);
            styleTabButton(btnAppointments, true);
            showMyAppointments();
        });

        box.getChildren().addAll(btnSchedules, btnAppointments);
        return box;
    }

    private void styleTabButton(Button btn, boolean active) {
        if (active) {
            btn.setStyle(
                    "-fx-background-color: #3b86df;" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 8;"
            );
        } else {
            btn.setStyle(
                    "-fx-background-color: #e5e7eb;" +
                    "-fx-text-fill: #333;" +
                    "-fx-background-radius: 8;"
            );
        }
    }

    // =========================================================
    //  VISTA 1: HORARIOS DISPONIBLES
    // =========================================================
    private void showAvailableSchedules() {

        contentContainer.getChildren().clear();

        HBox filters = new HBox(10);

        ComboBox<String> cbDoctor = new ComboBox<>();
        cbDoctor.setPromptText("Seleccione profesional");
        cbDoctor.setPrefWidth(200);

        DatePicker datePicker = new DatePicker();
        datePicker.setPrefWidth(150);

        Button btnSearch = new Button("Consultar");
        btnSearch.setStyle("-fx-background-color: #3b86df; -fx-text-fill: white;");

        filters.getChildren().addAll(cbDoctor, datePicker, btnSearch);

        TableView<String> table = new TableView<>();
        table.setPlaceholder(new Label("Sin datos"));

        TableColumn<String, String> colHour = new TableColumn<>("Hora disponible");
        table.getColumns().add(colHour);

        btnSearch.setOnAction(e -> {
            // =========================================================
            // TODO AQUÍ VA EL BACKEND
            // service.getAvailableSchedules(...)
            // =========================================================

            table.getItems().clear();
            table.getItems().add("08:00 AM");
            table.getItems().add("09:00 AM");
        });

        contentContainer.getChildren().addAll(filters, table);
    }

    // =========================================================
    // 🔹 VISTA 2: MIS CITAS
    // =========================================================
    private void showMyAppointments() {

        contentContainer.getChildren().clear();

        TableView<String> table = new TableView<>();

        TableColumn<String, String> colDoctor = new TableColumn<>("Médico");
        TableColumn<String, String> colDate = new TableColumn<>("Fecha");
        TableColumn<String, String> colTime = new TableColumn<>("Hora");

        table.getColumns().addAll(colDoctor, colDate, colTime);

        Button btnLoad = new Button("Cargar citas");
        btnLoad.setStyle("-fx-background-color: #3b86df; -fx-text-fill: white;");

        btnLoad.setOnAction(e -> {
            // =========================================================
            // 🔌 AQUÍ VA EL BACKEND
            // service.getAppointmentsByPatient(...)
            // =========================================================

            table.getItems().clear();
            table.getItems().add("Cita 1");
            table.getItems().add("Cita 2");
        });

        contentContainer.getChildren().addAll(btnLoad, table);
    }
}