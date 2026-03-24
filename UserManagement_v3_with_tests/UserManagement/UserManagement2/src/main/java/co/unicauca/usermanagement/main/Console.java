package co.unicauca.usermanagement.main;

import co.unicauca.appointmentmanagement.service.IAppointmentService;
import co.unicauca.microkernel.core.ReportService;
import co.unicauca.microkernel.piedaazul.common.entity.AppointmentEntity;
import co.unicauca.microkernel.piedaazul.common.entity.Report;
import java.util.List;
import java.util.Scanner;

public class Console {
    private IAppointmentService appointmentService;
    private ReportService reportService;
    private Scanner scanner;

    public Console(IAppointmentService appointmentService) {
        this.appointmentService = appointmentService;
        this.reportService = new ReportService();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        int option;

        System.out.println("Generación de reportes");

        do {
            System.out.println();
            System.out.println("1. Generar reporte de citas");
            System.out.println("2. Salir.");

            option = scanner.nextInt();

            switch (option) {
                case 1:
                    handleReportGenerationOption();
                    break;
            }

        } while (option != 2);

        System.out.println("Aplicación terminada");
    }

    private void handleReportGenerationOption() {
        List<AppointmentEntity> appointments = appointmentService.getAll();

        scanner.nextLine();

        System.out.println("Formato al cual se exportaran los datos:");
        System.out.println("1. JSON");
        System.out.println("2. HTML");

        String option = scanner.nextLine();
        String format = "";

        switch (option) {
            case "1":
                format = "json";
                break;
            case "2":
                format = "html";
                break;
            default:
                System.out.println("Formato inválido");
                return;
        }

        System.out.println("Nombre del archivo: ");
        String fileName = scanner.nextLine();

        Report reportEntity = new Report(appointments, format, fileName);

        try {
            reportService.generarReporte(reportEntity);
            System.out.println("Archivo creado...");
        } catch (Exception exception) {
            System.out.println("No fue posible exportar los datos. " + exception.getMessage());
        }
    }
}