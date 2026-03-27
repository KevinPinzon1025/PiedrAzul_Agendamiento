package co.unicauca.usermanagement.view;

import co.unicauca.usermanagement.controller.RegisterNewPatientFrameController;
import co.unicauca.usermanagement.service.IPatientService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;

public class RegisterNewPatientFrame {

    private Stage stage;
    private final IPatientService patientService;
    private final RegisterNewPatientFrameController controller;

    private TextField txtId;
    private TextField txtLastName;
    private TextField txtName;
    private ComboBox<String> cbGender;
    private TextField txtPhone;
    private TextField txtEmail;
    private DatePicker dpBirth;

    public RegisterNewPatientFrame(Stage owner, IPatientService service) {
        stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Registrar paciente");
        this.patientService = service;
        this.controller = new RegisterNewPatientFrameController(new FxViewAdapter(), this.patientService);

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: rgba(255,255,255,0.25);");

        VBox modal = createModal();

        root.getChildren().add(modal);

        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
    }

    public void show() {
        stage.showAndWait();
    }

    private VBox createModal() {

        VBox container = new VBox(20);
        container.setPadding(new Insets(30));
        container.setMaxWidth(650);
        container.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 20,0,0,4);"
        );

        Label title = new Label("Registrar paciente");
        title.setStyle(
                "-fx-font-size: 26px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #222;"
        );

        GridPane form = createForm();

        HBox buttons = createButtons();

        container.getChildren().addAll(title, form, buttons);
        return container;
    }

    private GridPane createForm() {

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);

        grid.getColumnConstraints().addAll(col1, col2);

        // Campos
        txtId = createTextField("Documento de identidad *");
        txtLastName = createTextField("Apellidos *");
        txtName = createTextField("Nombres *");
        cbGender = new ComboBox<>();
        cbGender.setPromptText("Género *");
        cbGender.getItems().addAll("Femenino","Masculino");
        cbGender.setPrefHeight(40);
        styleInput(cbGender);

        txtPhone = createTextField("Celular *");
        txtEmail = createTextField("Correo electrónico");
        dpBirth = new DatePicker();
        dpBirth.setPromptText("Fecha de nacimiento");
        dpBirth.setPrefHeight(40);
        styleInput(dpBirth);

        //mascaras
        txtId.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtId.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        txtPhone.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtPhone.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        txtName.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*")) {
                txtName.setText(newValue.replaceAll("[^a-zA-ZáéíóúÁÉÍÓÚñÑ ]", ""));
            }
        });

        txtLastName.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*")) {
                txtLastName.setText(newValue.replaceAll("[^a-zA-ZáéíóúÁÉÍÓÚñÑ ]", ""));
            }
        });

        dpBirth.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                LocalDate today = LocalDate.now();
                LocalDate minDate = LocalDate.of(1900, 1, 1);

                if (empty || date.isAfter(today) || date.isBefore(minDate)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;"); // opcional (rosado)
                }
            }
        });
        // Ubicación en grid
        grid.add(txtId, 0, 0);
        grid.add(txtLastName, 1, 0);

        grid.add(txtName, 0, 1);
        grid.add(cbGender, 1, 1);

        grid.add(txtPhone, 0, 2);
        grid.add(txtEmail, 1, 2);

        grid.add(dpBirth, 0, 3);

        return grid;
    }

    private HBox createButtons() {

        HBox box = new HBox(20);
        box.setAlignment(Pos.CENTER);

        Button btnCancel = new Button("Cancelar");
        btnCancel.setPrefWidth(180);
        btnCancel.setPrefHeight(45);
        btnCancel.setStyle(
                "-fx-background-color: #ff3b3b;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;"
        );

        Button btnSave = new Button("Registrar");
        btnSave.setPrefWidth(180);
        btnSave.setPrefHeight(45);
        btnSave.setStyle(
                "-fx-background-color: #2eaa60;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;"
        );

        // Acción cancelar
        btnCancel.setOnAction(e -> stage.close());

        // Acción registrar
        btnSave.setOnAction(e -> controller.onRegister());

        box.getChildren().addAll(btnCancel, btnSave);
        return box;
    }

    private TextField createTextField(String prompt) {
        TextField txt = new TextField();
        txt.setPromptText(prompt);
        txt.setPrefHeight(40);
        styleInput(txt);
        return txt;
    }

    private void styleInput(Control control) {
        control.setStyle(
                "-fx-background-radius: 8;" +
                        "-fx-border-radius: 8;" +
                        "-fx-border-color: #dcdcdc;" +
                        "-fx-padding: 0 10 0 10;"
        );
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registro");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private class FxViewAdapter implements RegisterNewPatientFrameController.View {
        @Override
        public String getIdText() {
            return txtId != null ? txtId.getText() : null;
        }

        @Override
        public String getLastName() {
            return txtLastName != null ? txtLastName.getText() : null;
        }

        @Override
        public String getName() {
            return txtName != null ? txtName.getText() : null;
        }

        @Override
        public String getGender() {
            return cbGender != null ? cbGender.getValue() : null;
        }

        @Override
        public String getPhoneText() {
            return txtPhone != null ? txtPhone.getText() : null;
        }

        @Override
        public String getEmail() {
            return txtEmail != null ? txtEmail.getText() : null;
        }

        @Override
        public LocalDate getBirthdate() {
            return dpBirth != null ? dpBirth.getValue() : null;
        }

        @Override
        public void showInfo(String message) {
            showAlert(message);
        }

        @Override
        public void close() {
            stage.close();
        }
    }
}