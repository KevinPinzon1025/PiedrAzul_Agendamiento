package co.unicauca.usermanagement.view;

import co.unicauca.appointmentmanagement.service.IAppointmentService;
import co.unicauca.appointmentmanagement.service.AppointmentServiceImpl;
import co.unicauca.microkernel.piedaazul.common.entity.AppointmentEntity;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class SearchAppointmentFrame extends Application {

    private IAppointmentService service = new AppointmentServiceImpl();

    private TableView<AppointmentEntity> table;
    private Label lblTotal;
    private ComboBox<String> cbProfessional;
    private DatePicker datePicker;

    @Override
    public void start(Stage stage) {

        HBox topBar = new HBox(15);
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-background-color: #2C3E50;");

        Label title = new Label("Lista de Citas");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");

        cbProfessional = new ComboBox<>();
        cbProfessional.setPromptText("Seleccione profesional");

        cbProfessional.getItems().addAll(service.getAllProfessionals());

        datePicker = new DatePicker();

        Button btnSearch = new Button("Buscar");
        btnSearch.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white;");

        Button btnNew = new Button("Nueva Cita");
        btnNew.setStyle("-fx-background-color: #2980B9; -fx-text-fill: white;");

        topBar.getChildren().addAll(title, cbProfessional, datePicker, btnSearch, btnNew);

        // 🔹 Tabla
        table = new TableView<>();

        TableColumn<AppointmentEntity, String> colPatient = new TableColumn<>("Paciente");
        colPatient.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getPatient()
                )
        );

        TableColumn<AppointmentEntity, String> colDoctor = new TableColumn<>("Médico");
        colDoctor.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getProfessional()
                )
        );

        TableColumn<AppointmentEntity, String> colDate = new TableColumn<>("Fecha");
        colDate.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getAppointmenDate().toLocalDate().toString()
                )
        );

        TableColumn<AppointmentEntity, String> colTime = new TableColumn<>("Hora");
        colTime.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getAppointmenDate().toLocalTime().toString()
                )
        );

        table.getColumns().addAll(colPatient, colDoctor, colDate, colTime);

        lblTotal = new Label("Total citas: 0");
        lblTotal.setPadding(new Insets(10));

        VBox root = new VBox(topBar, table, lblTotal);

        btnSearch.setOnAction(e -> search());

        Scene scene = new Scene(root, 900, 500);
        stage.setScene(scene);
        stage.setTitle("Buscar citas");
        stage.show();
    }

    private void search() {

        table.getItems().clear();

        String professional = cbProfessional.getValue();
        LocalDate date = datePicker.getValue();

        if (professional == null || date == null) {
            showAlert("Debe seleccionar profesional y fecha");
            return;
        }

        List<AppointmentEntity> list =
                service.findByProfessionalAndDate(professional, date);

        if (list.isEmpty()) {
            lblTotal.setText("Total citas: 0");
            showAlert("No se encontraron citas para la búsqueda");
            return;
        }

        table.getItems().addAll(list);
        lblTotal.setText("Total citas: " + list.size());
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

}