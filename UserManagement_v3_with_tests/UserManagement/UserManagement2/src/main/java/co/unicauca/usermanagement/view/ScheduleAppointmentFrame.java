package co.unicauca.usermanagement.view;
import co.unicauca.appointmentmanagement.Appointment;
import co.unicauca.appointmentmanagement.service.IAppointmentService;
import javafx.application.Platform;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import co.unicauca.usermanagement.Patient;
import co.unicauca.usermanagement.Professional;
import co.unicauca.usermanagement.Scheduler;
import co.unicauca.usermanagement.service.IPatientService;
import co.unicauca.usermanagement.service.PatientServiceImpl;
import co.unicauca.usermanagement.service.IPatientChangeListener;
import co.unicauca.usermanagement.acces.PatientRepositorySQL;
import co.unicauca.usermanagement.User;

import co.unicauca.usermanagement.main.ClientMain;
import co.unicauca.usermanagement.service.IProfessionalService;

public class ScheduleAppointmentFrame extends Application {
    private IAppointmentService service;
    private IPatientService patientService;
    private IProfessionalService professionalService;

    private ComboBox<String> cbPatient;
    private ComboBox<String> cbProfessional;
    private DatePicker datePicker;
    private ComboBox<String> cbTime;
    private TextArea txtMotivo;
    private Label lblFeedback;
    private  Stage stage;

    private final IPatientChangeListener patientChangeListener = this::onPatientsChanged;
    
    @Override
    public void start(Stage stage) {
        this.service = ClientMain.service;
        this.patientService = ClientMain.patientService;
        if (this.patientService == null) {
            this.patientService = new PatientServiceImpl(new PatientRepositorySQL());
        }
        this.professionalService = ClientMain.professionalService;
        this.stage = stage;
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f4f6fb;");

        VBox topContainer = new VBox(0);
        topContainer.getChildren().addAll(createHeader(), createToolbar());

        root.setTop(topContainer);
        root.setCenter(createForm());

        registerPatientObserver();

        Scene scene = new Scene(root, 980, 680);
        stage.setScene(scene);
        stage.setTitle("Agendar Cita");
        stage.setOnCloseRequest(e -> {
            if (this.patientService != null) {
                this.patientService.removePatientChangeListener(patientChangeListener);
            }
        });
        stage.show();
    }

    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 30, 15, 30));
        header.setSpacing(12);
        header.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #d9d9d9;" +
                "-fx-border-width: 0 0 1 0;"
        );

        Label brand = new Label("Servicios médicos Piedrazul");
        brand.setFont(Font.font("System", 22));
        brand.setStyle("-fx-text-fill: #2a5fa9; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label userLabel = new Label("Miguel - Profesional de la salud");
        userLabel.setStyle("-fx-text-fill: #2d6dcc; -fx-font-size: 14px; -fx-font-weight: bold;");

        header.getChildren().addAll(brand, spacer, userLabel);
        return header;
    }

  /*  private Node createToolbar() {
        HBox toolbar = new HBox();
        toolbar.setPadding(new Insets(20, 50, 10, 50));
        toolbar.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Agendar Cita");
        title.setFont(Font.font("System", 30));
        title.setStyle("-fx-text-fill: #222; -fx-font-weight: bold;");

        toolbar.getChildren().add(title);
        return toolbar;
    }*/
    
    private Node createToolbar() {
        HBox toolbar = new HBox();
        toolbar.setPadding(new Insets(20, 50, 10, 50));
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setSpacing(20);

        // Botón AGENDAR (activo)
        Button btnAgendar = new Button("Agendar cita");
        btnAgendar.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #2d6dcc;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-border-color: transparent transparent #2d6dcc transparent;" +
                "-fx-border-width: 0 0 3 0;"
        );

        // Botón LISTAR
        Button btnListar = new Button("Listar citas");
        btnListar.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #555;" +
                "-fx-font-size: 16px;"
        );

        // Acción: ir a listar citas
        btnListar.setOnAction(e -> {
            try {
                new SearchAppointmentFrame().start(new Stage());
                stage.close(); // cerrar actual
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        toolbar.getChildren().addAll(btnAgendar, btnListar);
        return toolbar;
    }

    private Node createForm() {
        VBox wrapper = new VBox();
        wrapper.setAlignment(Pos.TOP_CENTER);
        wrapper.setPadding(new Insets(10, 40, 30, 40));

        VBox card = new VBox(18);
        card.setMaxWidth(760);
        card.setPadding(new Insets(28));
        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 18;" +
                "-fx-border-radius: 18;" +
                "-fx-border-color: #dfe3eb;" +
                "-fx-border-width: 1;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 14, 0.2, 0, 4);"
        );

        HBox topActions = createTopActions();
        GridPane formGrid = createFormGrid();
        HBox bottomActions = createBottomActions();

        lblFeedback = new Label();
        lblFeedback.setStyle("-fx-text-fill: #0b5345; -fx-font-size: 13px; -fx-font-weight: bold;");

        card.getChildren().addAll(topActions, formGrid, bottomActions, lblFeedback);
        wrapper.getChildren().add(card);

        initData();

        return wrapper;
    }

    private HBox createTopActions() {
        HBox topActions = new HBox(12);
        topActions.setAlignment(Pos.CENTER);

        Button btnNewPatient = new Button("Nuevo Paciente");
        btnNewPatient.setPrefWidth(150);
        btnNewPatient.setPrefHeight(34);
        btnNewPatient.setStyle(
                "-fx-background-color: white;" +
                "-fx-text-fill: #2d6dcc;" +
                "-fx-font-weight: bold;" +
                "-fx-border-color: #2d6dcc;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;"
        );
        btnNewPatient.setOnAction(e -> openNewPatientWindow());


        Button btnConsultarHorarios = new Button("Consultar Horarios");
        btnConsultarHorarios.setPrefWidth(160);
        btnConsultarHorarios.setPrefHeight(34);
        btnConsultarHorarios.setStyle(
                "-fx-background-color: white;" +
                "-fx-text-fill: #5b8def;" +
                "-fx-font-weight: bold;" +
                "-fx-border-color: #b7cdfa;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;"
        );
        btnConsultarHorarios.setOnAction(e -> consultAvailableSchedules());

        topActions.getChildren().addAll(btnNewPatient, btnConsultarHorarios);
        return topActions;
    }

    private GridPane createFormGrid() {
        GridPane form = new GridPane();
        form.setHgap(18);
        form.setVgap(16);
        form.setAlignment(Pos.TOP_CENTER);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);

        form.getColumnConstraints().addAll(col1, col2);

        cbPatient = new ComboBox<>();
        cbPatient.setPromptText("Seleccionar paciente");
        cbPatient.setMaxWidth(Double.MAX_VALUE);
        cbPatient.setPrefHeight(38);
        List<User> patients = patientService.getAllPatients();
        cbPatient.getItems().addAll(
                patients.stream()
                        .map(p -> p.getFirstName() + " " + p.getFirstLastName())
                        .collect(Collectors.toList())
        );
        

        cbProfessional = new ComboBox<>();
        cbProfessional.setPromptText("Seleccionar profesional");
        cbProfessional.setMaxWidth(Double.MAX_VALUE);
        cbProfessional.setPrefHeight(38);
        List<User> professional = professionalService.getAllProfessionals();
        cbProfessional.getItems().addAll(
                professional.stream()
                    .map(p -> p.getFirstName())
                    .collect(Collectors.toList()));

        datePicker = new DatePicker(LocalDate.now());
        datePicker.setPromptText("Fecha");
        datePicker.setMaxWidth(Double.MAX_VALUE);
        datePicker.setPrefHeight(38);

        cbTime = new ComboBox<>();
        cbTime.setPromptText("Hora");
        cbTime.setMaxWidth(Double.MAX_VALUE);
        cbTime.setPrefHeight(38);

        txtMotivo = new TextArea();
        txtMotivo.setPromptText("Motivo de la consulta");
        txtMotivo.setWrapText(true);
        txtMotivo.setPrefHeight(120);

        VBox patientBox = createFieldBox("Paciente", cbPatient);
        VBox professionalBox = createFieldBox("Profesional", cbProfessional);
        VBox dateBox = createFieldBox("Fecha", datePicker);
        VBox timeBox = createFieldBox("Hora", cbTime);
        VBox reasonBox = createFieldBox("Motivo", txtMotivo);

        form.add(patientBox, 0, 0);
        form.add(professionalBox, 1, 0); // profesional al frente de paciente
        form.add(dateBox, 0, 1);
        form.add(timeBox, 1, 1);
        form.add(reasonBox, 0, 2, 2, 1);

        return form;
    }

    private VBox createFieldBox(String labelText, Control field) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #444;");

        VBox box = new VBox(6);
        box.getChildren().addAll(label, field);
        VBox.setVgrow(field, Priority.NEVER);

        return box;
    }

    private HBox createBottomActions() {
        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER);

        Button btnRegister = new Button("Registrar");
        btnRegister.setPrefWidth(150);
        btnRegister.setPrefHeight(40);
        btnRegister.setStyle(
                "-fx-background-color: #25b05b;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;"
        );
        btnRegister.setOnAction(e -> registerAppointment());

        Button btnClear = new Button("Limpiar");
        btnClear.setPrefWidth(120);
        btnClear.setPrefHeight(40);
        btnClear.setStyle(
                "-fx-background-color: #eef2f7;" +
                "-fx-text-fill: #333;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: #cfd6df;" +
                "-fx-border-radius: 8;"
        );
        btnClear.setOnAction(e -> clearForm());

        actions.getChildren().addAll(btnRegister, btnClear);
        return actions;
    }

    private void initData() {

        // TODO: Cargar horarios disponibles desde la capa de negocio
        cbTime.getItems().addAll(
                "08:00",
                "08:30",
                "09:00",
                "09:30",
                "10:00"
        );
    }

    private void openNewPatientWindow() {
        RegisterNewPatientFrame frame = new RegisterNewPatientFrame(stage, this.patientService);
        frame.show();
    }
    

    private void registerPatientObserver() {
        if (this.patientService == null) return;
        this.patientService.addPatientChangeListener(patientChangeListener);
    }

    private void onPatientsChanged() {
        
        Platform.runLater(this::refreshPatientsComboBox);
    }

    private void refreshPatientsComboBox() {
        if (cbPatient == null || patientService == null) return;

        String previousSelection = cbPatient.getValue();

        List<User> patients = patientService.getAllPatients();
        List<String> patientNames = patients.stream()
                .map(p -> p.getFirstName() + " " + p.getFirstLastName())
                .collect(Collectors.toList());

        cbPatient.getItems().setAll(patientNames);

        // Conserva selección si aún existe en la nueva lista.
        if (previousSelection != null && patientNames.contains(previousSelection)) {
            cbPatient.setValue(previousSelection);
        } else {
            cbPatient.getSelectionModel().clearSelection();
        }
    }

    private Patient resolvePatientByDisplayName(String patientDisplayName) {
        if (patientDisplayName == null || patientService == null) return null;

        return patientService.getAllPatients().stream()
                .filter(u -> u instanceof Patient)
                .map(u -> (Patient) u)
                .filter(p -> (p.getFirstName() + " " + p.getFirstLastName()).equals(patientDisplayName))
                .findFirst()
                .orElse(null);
    }

    private Professional buildProfessionalFromDisplayName(String professionalDisplayName) {
        Professional professional = new Professional();
        professional.setFirstName(professionalDisplayName);
        professional.setFirstLastName("");
        return professional;
    }

    private Scheduler buildDefaultScheduler() {
        // El backend requiere un agendador no nulo (columna NOT NULL).
        // Por ahora se usa un valor fijo; idealmente debe venir del usuario autenticado.
        Scheduler scheduler = new Scheduler();
        scheduler.setFirstName("Miguel");
        scheduler.setFirstLastName("");
        scheduler.setIdUser(0);
        return scheduler;
    }

    private void consultAvailableSchedules() {
        
        String professional = cbProfessional.getValue();
        LocalDate date = datePicker.getValue();

        if (professional == null || date == null) {
            lblFeedback.setText("Seleccione un profesional y una fecha para consultar horarios.");
            return;
        }

        ConsultScheduleFrame frame = new ConsultScheduleFrame(stage, service, professional, date);
        frame.show();
        
    }

    private void registerAppointment() {
        String patientDisplayName = cbPatient.getValue();
        String professional = cbProfessional.getValue();
        LocalDate date = datePicker.getValue();
        String time = cbTime.getValue();
        String motivo = txtMotivo.getText();

        if (patientDisplayName == null || professional == null || date == null || time == null || motivo == null || motivo.isBlank()) {
            lblFeedback.setText("Por favor complete todos los campos antes de registrar la cita.");
            return;
        }

        Patient patient = resolvePatientByDisplayName(patientDisplayName);
        if (patient == null) {
            lblFeedback.setText("No se encontró el paciente seleccionado en la base de datos.");
            return;
        }

        Professional professionalEntity = buildProfessionalFromDisplayName(professional);
        Scheduler scheduler = buildDefaultScheduler();

        LocalDateTime appointmentDateTime = LocalDateTime.of(date, LocalTime.parse(time));

        Appointment appointment = new Appointment(
                LocalDateTime.now(),
                appointmentDateTime,
                motivo,
                scheduler,
                patient,
                professionalEntity
        );

        boolean ok = service.scheduleAppointment(appointment);
        if (ok) {
            lblFeedback.setText("Cita registrada para " + patientDisplayName + " con " + professional + " el " + date + " a las " + time + ".");
            cbTime.getSelectionModel().clearSelection();
            txtMotivo.clear();
        } else {
            lblFeedback.setText("No se pudo registrar la cita. Intente nuevamente.");
        }
    }

    private void clearForm() {
        cbPatient.getSelectionModel().clearSelection();
        cbProfessional.getSelectionModel().clearSelection();
        datePicker.setValue(LocalDate.now());
        cbTime.getSelectionModel().clearSelection();
        txtMotivo.clear();
        lblFeedback.setText("");
    }

    public static void main(String[] args) {
        launch(args);
    }
}