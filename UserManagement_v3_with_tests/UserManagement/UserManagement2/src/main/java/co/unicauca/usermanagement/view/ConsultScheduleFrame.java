package co.unicauca.usermanagement.view;

import co.unicauca.appointmentmanagement.Appointment;
import co.unicauca.appointmentmanagement.service.IAppointmentService;
import co.unicauca.usermanagement.AvailableSlot;
import co.unicauca.usermanagement.controller.ConsultScheduleFrameController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;

public class ConsultScheduleFrame {

    private Stage stage;

    private IAppointmentService appointmentService;
    private final ConsultScheduleFrameController controller;

    private String currentProfessional;
    private LocalDate currentDate;

       TableView<AvailableSlot> tableAvailable;
    private TableView<Appointment> tableOccupied;

    private boolean showingAvailableSchedulesTab = true;

    //  CONTENEDOR DINÁMICO 
    private VBox contentContainer;

    //Constructor
    public ConsultScheduleFrame(Stage owner, IAppointmentService service, String professional, LocalDate date) {
        stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Consultar horarios");

        this.appointmentService = service;
        this.currentProfessional = professional;
        this.currentDate = date;
        this.controller = new ConsultScheduleFrameController(
                new FxViewAdapter(),
                this.appointmentService,
                this.currentProfessional,
                this.currentDate
        );

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: rgba(0,0,0,0.25);");

        VBox modal = createModal();

        root.getChildren().add(modal);

        Scene scene = new Scene(root, 950, 600);
        stage.setScene(scene);

        stage.setOnCloseRequest(e -> {
            controller.onClose();
        });

        controller.onInit();
    }

    public ConsultScheduleFrame(Stage owner) {
        this(owner, null, null, null);
    }

    //Metodos
    
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

        HBox box = new HBox(15); 
        box.setAlignment(Pos.CENTER); 

        Button btnSchedules = new Button("Horarios disponibles");
        Button btnAppointments = new Button("Mis citas");

        btnSchedules.setPrefWidth(180);
        btnAppointments.setPrefWidth(120);

        styleTabButton(btnSchedules, true);
        styleTabButton(btnAppointments, false);

        btnSchedules.setOnAction(e -> {
            styleTabButton(btnSchedules, true);
            styleTabButton(btnAppointments, false);
            showingAvailableSchedulesTab = true;
            showAvailableSchedules();
            controller.onShowAvailableTab();
        });

        btnAppointments.setOnAction(e -> {
            styleTabButton(btnSchedules, false);
            styleTabButton(btnAppointments, true);
            showingAvailableSchedulesTab = false;
            showMyAppointments();
            controller.onShowAppointmentsTab();
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

        VBox availableBox = new VBox(6);
        Label lblAvailable = new Label("Disponibles");
        lblAvailable.setStyle("-fx-font-weight: bold;");
        tableAvailable = new TableView<>();
        tableAvailable.setPlaceholder(new Label("Sin datos"));
       
        
        
        
        TableColumn<AvailableSlot, String> colAvailableDay = new TableColumn<>("Día");
        TableColumn<AvailableSlot, String> colAvailableEntryHour = new TableColumn<>("Hora de Entrada");
        TableColumn<AvailableSlot, String> colAvailableExitHour = new TableColumn<>("Hora de Salida");

        colAvailableDay.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDay())
        );

        colAvailableEntryHour.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getStartTime())
        );

        colAvailableExitHour.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getEndTime())
        );

        
        tableAvailable.getColumns().add(colAvailableDay);
        tableAvailable.getColumns().add(colAvailableEntryHour);
        tableAvailable.getColumns().add(colAvailableExitHour);
        
        availableBox.getChildren().addAll(lblAvailable, tableAvailable);

       
        tableOccupied = new TableView<>();
        tableOccupied.setPlaceholder(new Label("Sin datos"));
     

        contentContainer.getChildren().addAll(filters, availableBox);
    }

    // =========================================================
    // VISTA 2: MIS CITAS
    // =========================================================
    private void showMyAppointments() {

        contentContainer.getChildren().clear();

        TableColumn<Appointment, String> colDate = new TableColumn<>("Fecha");
        colDate.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getAppointmenDate().toLocalDate().toString()
                )
        );

        TableColumn<Appointment, String> colTime = new TableColumn<>("Hora");
        colTime.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getAppointmenDate().toLocalTime().toString()
                )
        );

        TableColumn<Appointment, String> colPatient = new TableColumn<>("Paciente");
        colPatient.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getPatient() != null
                                ? c.getValue().getPatient().getFirstName()
                                : ""
                )
        );

        TableColumn<Appointment, String> colReason = new TableColumn<>("Motivo");
        colReason.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getObservation()
                )
        );

        tableOccupied.getColumns().addAll(colDate, colTime, colPatient, colReason);

        contentContainer.getChildren().addAll(tableOccupied);
    }

    private class FxViewAdapter implements ConsultScheduleFrameController.View {
        @Override
        public void setAvailableSlots(List<AvailableSlot> slots) {
            if (tableAvailable == null) return;
            tableAvailable.getItems().setAll(slots);
        }

        @Override
        public void setAppointments(java.util.List<Appointment> appointments) {
            if (tableOccupied == null) return;
            tableOccupied.getItems().setAll(appointments);
        }
    }
}