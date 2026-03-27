package co.unicauca.usermanagement.view;

import co.unicauca.appointmentmanagement.Appointment;
import co.unicauca.appointmentmanagement.service.IAppointmentService;
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

import java.time.LocalDate;
import java.util.List;

import co.unicauca.usermanagement.controller.SearchAppointmentController;
import co.unicauca.usermanagement.main.ClientMain;

public class SearchAppointmentFrame extends Application {

    private IAppointmentService service;

    private SearchAppointmentController controller;

    private TableView<Appointment> table;
    private Label lblTotal;
    private ComboBox<String> cbProfessional;
    private DatePicker datePicker;
    private TextField txtSearch;

    @Override
    public void start(Stage stage) {
        this.service = ClientMain.service;
        this.controller = new SearchAppointmentController(new FxViewAdapter(stage), this.service);

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

        controller.onInit();
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
        VBox container = new VBox(10);
        container.setPadding(new Insets(20, 40, 10, 40));

        // Barra de navegación
        HBox navBar = new HBox(20);
        navBar.setAlignment(Pos.CENTER_LEFT);

        Button btnAgendar = new Button("Agendar cita");
        btnAgendar.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #555;" +
                "-fx-font-size: 16px;"
        );

        Button btnListar = new Button("Listar citas");
        btnListar.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #2d6dcc;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-border-color: transparent transparent #2d6dcc transparent;" +
                "-fx-border-width: 0 0 3 0;"
        );

        // Acción: ir a agendar
        btnAgendar.setOnAction(e -> controller.onNavigateToSchedule());

        navBar.getChildren().addAll(btnAgendar, btnListar);

        HBox toolbar = new HBox();
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

        toolbar.getChildren().addAll(title, spacer, txtSearch, btnSearch);

        btnSearch.setOnAction(e -> controller.onSearchByText());

        container.getChildren().addAll(navBar, toolbar);

        return container;
    }

    private Node createContent() {
        VBox wrapper = new VBox(15);
        wrapper.setPadding(new Insets(10, 40, 30, 40));

        HBox filters = new HBox(12);
        filters.setAlignment(Pos.CENTER_LEFT);

        cbProfessional = new ComboBox<>();
        cbProfessional.setPromptText("Seleccione profesional");
        cbProfessional.setPrefWidth(260);

        datePicker = new DatePicker();
        datePicker.setPrefWidth(180);
        datePicker.setValue(LocalDate.now());
        configureDatePickerValidations();

        Button btnConsultar = new Button("Consultar");
        btnConsultar.setStyle("-fx-background-color: #3b86df; -fx-text-fill: white;");
        btnConsultar.setOnAction(e -> controller.onSearch());

        filters.getChildren().addAll(cbProfessional, datePicker, btnConsultar);

        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-border-color: #ddd;");

        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Appointment, String> colId = new TableColumn<>("Identificación");
        colId.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getPatient() != null
                                ? String.valueOf((long) c.getValue().getPatient().getIdUser())
                                : ""
                )
        );

        TableColumn<Appointment, String> colDoctor = new TableColumn<>("Médico");
        colDoctor.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getProfessional() != null
                                ? c.getValue().getProfessional().getFirstName()
                                : ""
                )
        );

        TableColumn<Appointment, String> colPatient = new TableColumn<>("Paciente");
        colPatient.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getPatient() != null
                                ? c.getValue().getPatient().getFirstName()
                                : ""
                )
        );

        TableColumn<Appointment, String> colDate = new TableColumn<>("Fecha");
        colDate.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getAppointmenDate() != null
                                ? c.getValue().getAppointmenDate().toLocalDate().toString()
                                : ""
                )
        );

        TableColumn<Appointment, String> colTime = new TableColumn<>("Hora");
        colTime.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getAppointmenDate() != null
                                ? c.getValue().getAppointmenDate().toLocalTime().toString()
                                : ""
                )
        );

        table.getColumns().add(colId);
        table.getColumns().add(colDoctor);
        table.getColumns().add(colPatient);
        table.getColumns().add(colDate);
        table.getColumns().add(colTime);

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
            if (!controller.isDateSelectable(item)) {
                setDisable(true);
                setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #9e9e9e;");
            }
        }
    });
}

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Consulta");
        alert.setHeaderText("Resultado");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private class FxViewAdapter implements SearchAppointmentController.View {
        private final Stage stage;

        private FxViewAdapter(Stage stage) {
            this.stage = stage;
        }

        @Override
        public void setProfessionals(List<String> professionals) {
            if (cbProfessional == null) return;
            String previous = cbProfessional.getValue();
            cbProfessional.getItems().setAll(professionals);
            if (previous != null && professionals.contains(previous)) {
                cbProfessional.setValue(previous);
            }
        }

        @Override
        public String getSelectedProfessional() {
            return cbProfessional != null ? cbProfessional.getValue() : null;
        }

        @Override
        public void setSelectedProfessional(String professional) {
            if (cbProfessional != null) cbProfessional.setValue(professional);
        }

        @Override
        public LocalDate getSelectedDate() {
            return datePicker != null ? datePicker.getValue() : null;
        }

        @Override
        public String getSearchText() {
            return txtSearch != null ? txtSearch.getText() : null;
        }

        @Override
        public void clearAppointments() {
            if (table != null) table.getItems().clear();
        }

        @Override
        public void setAppointments(List<Appointment> appointments) {
            if (table != null) table.getItems().setAll(appointments);
        }

        @Override
        public void setTotal(int total) {
            if (lblTotal != null) lblTotal.setText("Total citas: " + total);
        }

        @Override
        public void showAlert(String message) {
            SearchAppointmentFrame.this.showAlert(message);
        }

        @Override
        public void navigateToScheduleAppointment() {
            try {
                new ScheduleAppointmentFrame().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void closeCurrentWindow() {
            if (stage != null) stage.close();
        }
    }
}