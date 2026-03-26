package co.unicauca.usermanagement.view;

import co.unicauca.appointmentmanagement.Appointment;
import co.unicauca.appointmentmanagement.service.IAppointmentChangeListener;
import co.unicauca.appointmentmanagement.service.IAppointmentService;
import co.unicauca.microkernel.piedaazul.common.entity.AppointmentEntity;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConsultScheduleFrame {

    private Stage stage;

    private IAppointmentService appointmentService;
    private final IAppointmentChangeListener appointmentChangeListener = this::onAppointmentsChanged;

    private String currentProfessional;
    private LocalDate currentDate;

    private TableView<String> tableAvailable;
    private TableView<Appointment> tableOccupied;

    private boolean showingAvailableSchedulesTab = true;

    private final List<String> timeSlots = Arrays.asList(
            "08:00",
            "08:30",
            "09:00",
            "09:30",
            "10:00"
    );

    //  CONTENEDOR DINÁMICO 
    private VBox contentContainer;

    //METODOS
    public ConsultScheduleFrame(Stage owner, IAppointmentService service, String professional, LocalDate date) {
        stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Consultar horarios");

        this.appointmentService = service;
        this.currentProfessional = professional;
        this.currentDate = date;

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: rgba(0,0,0,0.25);");

        VBox modal = createModal();

        root.getChildren().add(modal);

        Scene scene = new Scene(root, 950, 600);
        stage.setScene(scene);

        if (this.appointmentService != null) {
            this.appointmentService.addAppointmentChangeListener(appointmentChangeListener);
        }

        stage.setOnCloseRequest(e -> {
            if (this.appointmentService != null) {
                this.appointmentService.removeAppointmentChangeListener(appointmentChangeListener);
            }
        });
    }

    public ConsultScheduleFrame(Stage owner) {
        this(owner, null, null, null);
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
        });

        btnAppointments.setOnAction(e -> {
            styleTabButton(btnSchedules, false);
            styleTabButton(btnAppointments, true);
            showingAvailableSchedulesTab = false;
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

    private void onAppointmentsChanged() {
        if (!showingAvailableSchedulesTab) return;
        Platform.runLater(this::refreshSchedules);
    }

    private void refreshSchedules() {
        if (appointmentService == null) return;
        if (currentProfessional == null || currentDate == null) return;

        if (tableAvailable != null) {
            tableAvailable.getItems().clear();
        }
        if (tableOccupied != null) {
            tableOccupied.getItems().clear();
        }

        // TRAER CITAS DEL BACKEND (YA SON Appointment)
        List<Appointment> appointments =
                appointmentService.findByProfessionalAndDate(currentProfessional, currentDate);

      
        Set<String> occupiedTimes = new HashSet<>();
        List<String> availableList = new ArrayList<>();
        List<Appointment> occupiedList = new ArrayList<>();

        for (Appointment appointment : appointments) {

            if (appointment.getAppointmenDate() == null) continue;

            String timeText = appointment.getAppointmenDate().toLocalTime().toString();

            occupiedTimes.add(timeText);

            // AHORA GUARDAMOS EL OBJETO COMPLETO
            occupiedList.add(appointment);
        }

        // CALCULAR DISPONIBLES
        for (String slot : timeSlots) {
            if (!occupiedTimes.contains(slot)) {
                availableList.add(slot);
            }
        }

        // SETEAR TABLAS
        if (tableAvailable != null) {
            tableAvailable.getItems().setAll(availableList);
        }

        if (tableOccupied != null) {
            tableOccupied.getItems().setAll(occupiedList);
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
        TableColumn<String, String> colAvailableDay = new TableColumn<>("Día");
        TableColumn<String, String> colAvailableEntryHour = new TableColumn<>("Hora de Entrada");
        TableColumn<String, String> colAvailableExitHour = new TableColumn<>("Hora de Salida");
        
        colAvailableDay.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue()));
        colAvailableEntryHour.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue()));
        colAvailableExitHour.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue()));

        tableAvailable.getColumns().add(colAvailableDay);
        tableAvailable.getColumns().add(colAvailableEntryHour);
        tableAvailable.getColumns().add(colAvailableExitHour);
        availableBox.getChildren().addAll(lblAvailable, tableAvailable);

       
        tableOccupied = new TableView<>();
        tableOccupied.setPlaceholder(new Label("Sin datos"));
     

        contentContainer.getChildren().addAll(filters, availableBox);
        refreshSchedules();
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
        refreshSchedules();
    }
}