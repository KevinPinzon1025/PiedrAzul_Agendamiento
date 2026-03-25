package co.unicauca.usermanagement.view;

import co.unicauca.appointmentmanagement.service.AppointmentServiceImpl;
import co.unicauca.appointmentmanagement.service.IAppointmentService;
import co.unicauca.microkernel.piedaazul.common.entity.AppointmentEntity;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

import co.unicauca.usermanagement.main.ClientMain;

public class SearchAppointmentFrame extends Application {

    private IAppointmentService service;
    

    private TableView<AppointmentEntity> table;
    private Label lblTotal;
    private ComboBox<String> cbProfessional;
    private DatePicker datePicker;
    private TextField txtSearch;
    
    private final Set<LocalDate> holidays = new HashSet<>(Arrays.asList(
        LocalDate.of(2026, 1, 1),   // Año Nuevo
        LocalDate.of(2026, 1, 12),  // Reyes Magos trasladado
        LocalDate.of(2026, 3, 23),  // San José trasladado
        LocalDate.of(2026, 4, 2),   // Jueves Santo
        LocalDate.of(2026, 4, 3),   // Viernes Santo
        LocalDate.of(2026, 5, 1),   // Día del Trabajo
        LocalDate.of(2026, 5, 18),  // Ascensión trasladada
        LocalDate.of(2026, 6, 8),   // Corpus Christi trasladado
        LocalDate.of(2026, 6, 15),  // Sagrado Corazón trasladado
        LocalDate.of(2026, 6, 29),  // San Pedro y San Pablo trasladado
        LocalDate.of(2026, 7, 20),  // Independencia
        LocalDate.of(2026, 8, 7),   // Batalla de Boyacá
        LocalDate.of(2026, 8, 17),  // Asunción trasladada
        LocalDate.of(2026, 10, 12), // Día de la Raza trasladado
        LocalDate.of(2026, 11, 2),  // Todos los Santos trasladado
        LocalDate.of(2026, 11, 16), // Independencia de Cartagena trasladado
        LocalDate.of(2026, 12, 8),  // Inmaculada Concepción
        LocalDate.of(2026, 12, 25)  // Navidad
));

    @Override
    public void start(Stage stage) {
        this.service = ClientMain.service;

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f3f4f6;");

        VBox topContainer = new VBox();
        topContainer.getChildren().addAll(createHeader(), createToolbar());

        root.setTop(topContainer);
        root.setCenter(createContent());

        Scene scene = new Scene(root, 1100, 650);
        stage.setScene(scene);
        stage.setTitle("Buscar citas");
        stage.show();
    }

    private Node createHeader() {
    HBox header = new HBox();
    header.setAlignment(Pos.CENTER_LEFT);
    header.setPadding(new Insets(18, 28, 18, 28));
    header.setSpacing(15);
    header.setStyle("-fx-background-color: white; -fx-border-color: #d9d9d9; -fx-border-width: 0 0 1 0;");

    Image logo = new Image(
            getClass().getResourceAsStream("/logo_piedrazul.png")
    );

    ImageView logoView = new ImageView(logo);
    logoView.setFitWidth(85);
    logoView.setPreserveRatio(true);

    VBox textLogoBox = new VBox(0);

    Label logoTop = new Label("Servicios médicos");
    logoTop.setStyle("-fx-text-fill: #5b8bd9; -fx-font-size: 14px;");

    Label logoBottom = new Label("Piedrazul");
    logoBottom.setStyle("-fx-text-fill: #2d6dcc; -fx-font-size: 22px; -fx-font-weight: bold;");

    textLogoBox.getChildren().addAll(logoTop, logoBottom);

    HBox brandBox = new HBox(10);
    brandBox.setAlignment(Pos.CENTER_LEFT);
    brandBox.getChildren().addAll(logoView, textLogoBox);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    Label user = new Label("Miguel - Agendador");
    user.setStyle("-fx-text-fill: #2d6dcc; -fx-font-size: 14px;");

    header.getChildren().addAll(brandBox, spacer, user);
    return header;
}

    private Node createToolbar() {
        HBox toolbar = new HBox();
        toolbar.setPadding(new Insets(25, 40, 10, 40));
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setSpacing(20);

        Label title = new Label("Lista de Citas");
        title.setFont(Font.font("System", 30));
        title.setStyle("-fx-text-fill: #222; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        txtSearch = new TextField();
        txtSearch.setPromptText("Buscar por profesional");
        txtSearch.setPrefWidth(220);

        Button btnSearch = new Button("Buscar");

        Button btnNew = new Button("Nueva Cita");
        btnNew.setStyle(
                "-fx-background-color: #3b86df;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;"
        );

        toolbar.getChildren().addAll(title, spacer, txtSearch, btnSearch, btnNew);

        btnSearch.setOnAction(e -> searchByText());

        return toolbar;
    }

    private Node createContent() {
        VBox wrapper = new VBox(15);
        wrapper.setPadding(new Insets(10, 40, 30, 40));

        HBox filters = new HBox(12);
        filters.setAlignment(Pos.CENTER_LEFT);

        cbProfessional = new ComboBox<>();
        cbProfessional.setPromptText("Seleccione profesional");
        cbProfessional.setPrefWidth(260);
        cbProfessional.getItems().addAll(service.getAllProfessionals());

        datePicker = new DatePicker();
        datePicker.setPrefWidth(180);
        datePicker.setValue(LocalDate.now());
        configureDatePickerValidations();

        Button btnConsultar = new Button("Consultar");
        btnConsultar.setStyle("-fx-background-color: #3b86df; -fx-text-fill: white;");
        btnConsultar.setOnAction(e -> search());

        filters.getChildren().addAll(cbProfessional, datePicker, btnConsultar);

        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-border-color: #ddd;");

        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<AppointmentEntity, String> colId = new TableColumn<>("Identificación");
        colId.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getCedPatient()))
        );

        TableColumn<AppointmentEntity, String> colDoctor = new TableColumn<>("Médico");
        colDoctor.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getProfessional())
        );

        TableColumn<AppointmentEntity, String> colPatient = new TableColumn<>("Paciente");
        colPatient.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getPatient())
        );

        TableColumn<AppointmentEntity, String> colDate = new TableColumn<>("Fecha");
        colDate.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getAppointmenDate().toLocalDate().toString())
        );

        TableColumn<AppointmentEntity, String> colTime = new TableColumn<>("Hora");
        colTime.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getAppointmenDate().toLocalTime().toString())
        );

        table.getColumns().addAll(colId, colDoctor, colPatient, colDate, colTime);

        lblTotal = new Label("Total citas: 0");

        card.getChildren().addAll(table, lblTotal);
        wrapper.getChildren().addAll(filters, card);

        return wrapper;
    }

    private void configureDatePickerValidations() {
    datePicker.setDayCellFactory(picker -> new DateCell() {
        @Override
        public void updateItem(LocalDate item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                return;
            }

            boolean isPastDate = item.isBefore(LocalDate.now());
            boolean isWeekend = item.getDayOfWeek() == java.time.DayOfWeek.SATURDAY
                    || item.getDayOfWeek() == java.time.DayOfWeek.SUNDAY;
            boolean isHoliday = holidays.contains(item);

            if (isPastDate || isWeekend || isHoliday) {
                setDisable(true);
                setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #9e9e9e;");
            }
        }
    });
}

    private void searchByText() {
        String text = txtSearch.getText();

        if (text == null || text.isBlank()) {
            showAlert("Escriba el nombre de un profesional para realizar la búsqueda.");
            return;
        }

        String matchedProfessional = findProfessionalIgnoreCase(text.trim());

        if (matchedProfessional == null) {
            showAlert("El profesional ingresado no existe en la lista disponible.");
            return;
        }

        cbProfessional.setValue(matchedProfessional);
        search();
    }

    private String findProfessionalIgnoreCase(String text) {
        for (String professional : cbProfessional.getItems()) {
            if (professional.equalsIgnoreCase(text)) {
                return professional;
            }
        }
        return null;
    }

    private void search() {
        table.getItems().clear();

        String professional = cbProfessional.getValue();
        LocalDate date = datePicker.getValue();

        if (professional == null || professional.isBlank() || date == null) {
            lblTotal.setText("Total citas: 0");
            showAlert("Por favor seleccione un profesional y una fecha.");
            return;
        }

        if (date.isBefore(LocalDate.now())) {
            lblTotal.setText("Total citas: 0");
            showAlert("No se permiten consultas con fechas anteriores al día de hoy.");
            return;
        }

        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            lblTotal.setText("Total citas: 0");
            showAlert("La fecha seleccionada no tiene atención disponible. Seleccione un día hábil.");
            return;
        }

        if (!service.isDateAvailable(date)) {
            lblTotal.setText("Total citas: 0");
            showAlert("La fecha seleccionada no tiene atención disponible.");
            return;
        }

        List<AppointmentEntity> list = service.findByProfessionalAndDate(professional, date);

        if (list.isEmpty()) {
            lblTotal.setText("Total citas: 0");
            showAlert("No hay citas programadas para este profesional en esa fecha.");
            return;
        }
        
        if (holidays.contains(date)) {
            lblTotal.setText("Total citas: 0");
            showAlert("La fecha seleccionada corresponde a un día festivo. Seleccione un día hábil.");
            return;
        }

        table.getItems().addAll(list);
        lblTotal.setText("Total citas: " + list.size());
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Consulta");
        alert.setHeaderText("Resultado");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}