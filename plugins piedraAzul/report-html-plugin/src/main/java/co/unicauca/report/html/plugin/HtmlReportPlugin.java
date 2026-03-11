package co.unicauca.report.html.plugin;

import co.unicauca.microkernel.piedraazul.common.interfaz.IReportPlugin;
import co.unicauca.microkernel.piedaazul.common.entity.ApointmentDTO;
import java.io.FileWriter;
import java.util.List;

public class HtmlReportPlugin implements IReportPlugin {

    @Override
    public void generateReport(List<ApointmentDTO> data, String fileName) {

        try (FileWriter writer = new FileWriter(fileName + ".html")) {

            writer.write("<!DOCTYPE html>");
            writer.write("<html lang='es'>");
            writer.write("<head>");
            writer.write("<meta charset='UTF-8'>");
            writer.write("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            writer.write("<title>Reporte de Citas</title>");
            writer.write("<style>");
            writer.write("body { font-family: Arial, sans-serif; margin: 30px; background-color: #f8f9fa; color: #333; }");
            writer.write("h2 { text-align: center; color: #2c3e50; margin-bottom: 20px; }");
            writer.write("table { width: 100%; border-collapse: collapse; background-color: white; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }");
            writer.write("th, td { padding: 12px; border: 1px solid #ddd; text-align: left; }");
            writer.write("th { background-color: #34495e; color: white; }");
            writer.write("tr:nth-child(even) { background-color: #f2f2f2; }");
            writer.write("tr:hover { background-color: #eaf2f8; }");
            writer.write("</style>");
            writer.write("</head>");
            writer.write("<body>");

            writer.write("<h2>Reporte de Citas</h2>");
            writer.write("<table>");

            writer.write("<tr>");
            writer.write("<th>Fecha de agendamiento</th>");
            writer.write("<th>Fecha de la cita</th>");
            writer.write("<th>Observacion</th>");
            writer.write("<th>Agendador</th>");
            writer.write("<th>Paciente</th>");
            writer.write("<th>Profesional</th>");
            writer.write("<th>Cedula del paciente</th>");
            writer.write("</tr>");

            for (ApointmentDTO appointment : data) {
                writer.write("<tr>");
                writer.write("<td>" + safe(appointment.getSchedulingDate()) + "</td>");
                writer.write("<td>" + safe(appointment.getAppointmenDate()) + "</td>");
                writer.write("<td>" + safe(appointment.getObservation()) + "</td>");
                writer.write("<td>" + safe(appointment.getScheduler()) + "</td>");
                writer.write("<td>" + safe(appointment.getPatient()) + "</td>");
                writer.write("<td>" + safe(appointment.getProfessional()) + "</td>");
                writer.write("<td>" + safe(appointment.getCedPatient()) + "</td>");
                writer.write("</tr>");
            }

            writer.write("</table>");
            writer.write("</body>");
            writer.write("</html>");

            System.out.println("Archivo HTML creado: " + fileName + ".html");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}