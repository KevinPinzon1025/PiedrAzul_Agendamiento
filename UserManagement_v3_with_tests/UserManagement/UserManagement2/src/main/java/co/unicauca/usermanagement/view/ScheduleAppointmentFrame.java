package co.unicauca.usermanagement.view;

import co.unicauca.appointmentmanagement.service.AppointmentServiceImpl;
import co.unicauca.appointmentmanagement.service.IAppointmentService;
import co.unicauca.microkernel.piedaazul.common.entity.AppointmentEntity;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ScheduleAppointmentFrame extends Application {

    private final IAppointmentService appointmentService = new AppointmentServiceImpl();

    private ComboBox<String> cbPatient;
    private ComboBox<String> cbProfessional;
    private DatePicker datePicker;
    private ComboBox<String> cbTime;
    private TextArea txtMotivo;
    private Label lblFeedback;

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f4f6fb;");

        VBox topContainer = new VBox(0);
        topContainer.getChildren().addAll(createHeader(), createToolbar());

        root.setTop(topContainer);
        root.setCenter(createForm());

        Scene scene = new Scene(root, 900, 650);
        stage.setScene(scene);
        stage.setTitle("Agendar Cita");
        stage.show();
    }

    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 30, 15, 30));
        header.setSpacing(12);
        header.setStyle("-fx-background-color: white; -fx-border-color: #d9d9d9; -fx-border-width: 0 0 1 0;");

        Label brand = new Label("Servicios médicos Piedrazul");
        brand.setFont(Font.font("System", 22));
        brand.setStyle("-fx-text-fill: #2a5fa9; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label userLabel = new Label("Miguel - Agendador");
        userLabel.setStyle("-fx-text-fill: #2d6dcc; -fx-font-size: 14px;");

        header.getChildren().addAll(brand, spacer, userLabel);
        return header;
    }

    private Node createToolbar() {
        HBox toolbar = new HBox();
        toolbar.setPadding(new Insets(20, 40, 10, 40));
        toolbar.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Agendar Cita");
        title.setFont(Font.font("System", 30));
        title.setStyle("-fx-text-fill: #222; -fx-font-weight: bold;");

        toolbar.getChildren().add(title);

        return toolbar;
    }

    private Node createForm() {
        VBox wrapper = new VBox(15);
        wrapper.setPadding(new Insets(20, 40, 30, 40));

        GridPane form = new GridPane();
        form.setHgap(18);
        form.setVgap(16);
        form.setAlignment(Pos.TOP_LEFT);

        Label lblPatient = new Label("Paciente:");
        cbPatient = new ComboBox<>();
        cbPatient.setPrefWidth(300);

        Label lblProfessional = new Label("Profesional:");
        cbProfessional = new ComboBox<>();
        cbProfessional.setPrefWidth(300);

        Label lblDate = new Label("Fecha:");
        datePicker = new DatePicker(LocalDate.now());
        datePicker.setPrefWidth(300);

        Label lblTime = new Label("Hora:");
        cbTime = new ComboBox<>();
        cbTime.setPrefWidth(300);

        Label lblMotivo = new Label("Motivo:");
        txtMotivo = new TextArea();
        txtMotivo.setPromptText("Escribir motivo de la consulta...");
        txtMotivo.setWrapText(true);
        txtMotivo.setPrefHeight(150);
        txtMotivo.setPrefWidth(620);

        form.add(lblPatient, 0, 0);
        form.add(cbPatient, 1, 0);
        form.add(lblProfessional, 0, 1);
        form.add(cbProfessional, 1, 1);
        form.add(lblDate, 0, 2);
        form.add(datePicker, 1, 2);
        form.add(lblTime, 0, 3);
        form.add(cbTime, 1, 3);
        form.add(lblMotivo, 0, 4);
        form.add(txtMotivo, 1, 4, 2, 1);

        btnInitData();

        cbProfessional.setOnAction(e -> refreshAvailableHours());
        datePicker.setOnAction(e -> refreshAvailableHours());

        HBox buttonRow = new HBox(12);
        buttonRow.setAlignment(Pos.CENTER_LEFT);

        Button btnSchedule = new Button("Registrar Cita");
        btnSchedule.setStyle("-fx-background-color: #26a65b; -fx-text-fill: white; -fx-font-weight: bold;");
        btnSchedule.setOnAction(e -> scheduleAppointment());

        Button btnReset = new Button("Limpiar");
        btnReset.setOnAction(e -> clearForm());

        buttonRow.getChildren().addAll(btnSchedule, btnReset);

        lblFeedback = new Label();
        lblFeedback.setStyle("-fx-text-fill: #0b5345; -fx-font-size: 13px;");

        VBox card = new VBox(14);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 8; -fx-background-radius: 8;");
        card.getChildren().addAll(form, buttonRow, lblFeedback);

        wrapper.getChildren().add(card);
        return wrapper;
    }

    private void btnInitData() {
        // Cargar profesionales de la lógica existente
        List<String> professionals = appointmentService.getAllProfessionals();
        cbProfessional.getItems().clear();
        cbProfessional.getItems().addAll(professionals);

        // Cargar pacientes desde la tabla de citas (necesita lógica de negocio real que recupere pacientes
        // en el futuro, por ahora se usa la información de citas ya creadas)
        Set<String> uniquePatients = appointmentService.getAll().stream()
                .map(AppointmentEntity::getPatient)
                .collect(Collectors.toSet());
        cbPatient.getItems().clear();
        cbPatient.getItems().addAll(uniquePatients);
    }

    private void refreshAvailableHours() {
        cbTime.getItems().clear();

        String professional = cbProfessional.getValue();
        LocalDate date = datePicker.getValue();

        if (professional == null || date == null) {
            return;
        }

        // TODO: Reemplazar con llamada a la lógica de negocio:
        //  List<String> availableHours = yourBusinessService.getAvailableHours(professional, date);
        //  cbTime.getItems().setAll(availableHours);

        // Grabamos los horarios ocupados de las citas existentes
        List<AppointmentEntity> booked = appointmentService.findByProfessionalAndDate(professional, date);
        Set<String> occupied = booked.stream()
                .map(a -> a.getAppointmenDate().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                .collect(Collectors.toSet());

        // rango simulado de horas disponibles cada 30 minutos
        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(17, 0);
        List<String> allSlots = new ArrayList<>();
        while (!start.isAfter(end)) {
            allSlots.add(start.format(DateTimeFormatter.ofPattern("HH:mm")));
            start = start.plusMinutes(30);
        }

        List<String> freeSlots = allSlots.stream()
                .filter(slot -> !occupied.contains(slot))
                .collect(Collectors.toList());

        cbTime.getItems().addAll(freeSlots);
        if (freeSlots.isEmpty()) {
            cbTime.setPromptText("No hay horas disponibles");
        }
    }

    private void scheduleAppointment() {
        String patient = cbPatient.getValue();
        String professional = cbProfessional.getValue();
        LocalDate date = datePicker.getValue();
        String time = cbTime.getValue();
        String motivo = txtMotivo.getText();

        if (patient == null || professional == null || date == null || time == null || motivo == null || motivo.isBlank()) {
            lblFeedback.setText("Por favor complete todos los campos antes de registrar la cita.");
            return;
        }

        // TODO: Reemplezar por llamada real a capa de negocio que cree la cita
        //  Appointment appointment = new Appointment(...);
        //  boolean ok = appointmentManager.scheduleAppointment(appointment);
        //  if (ok) { ...}

        // Ejemplo de mensaje temporal:
        lblFeedback.setText("Cita generada localmente: " + patient + " con " + professional + " el " + date + " a las " + time + ".");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Agendar cita");
        alert.setHeaderText("Agendamiento");
        alert.setContentText("Se ha enviado la solicitud de agendamiento a la capa de negocio. (Implementar persistencia real)");
        alert.showAndWait();
    }

    private void clearForm() {
        cbPatient.getSelectionModel().clearSelection();
        cbProfessional.getSelectionModel().clearSelection();
        datePicker.setValue(LocalDate.now());
        cbTime.getItems().clear();
        txtMotivo.clear();
        lblFeedback.setText("");
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
