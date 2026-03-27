package co.unicauca.usermanagement.controller;

import co.unicauca.usermanagement.Patient;
import co.unicauca.usermanagement.service.IPatientService;
import java.time.LocalDate;

public class RegisterNewPatientFrameController {

    public interface View {
        String getIdText();
        String getLastName();
        String getName();
        String getGender();
        String getPhoneText();
        String getEmail();
        LocalDate getBirthdate();

        void showInfo(String message);
        void close();
    }

    private final View view;
    private final IPatientService patientService;

    public RegisterNewPatientFrameController(View view, IPatientService patientService) {
        this.view = view;
        this.patientService = patientService;
    }

    public void onRegister() {
        if (!validateRequiredFields()) return;

        try {
            double patId = Double.parseDouble(view.getIdText());
            double cellNumber = Double.parseDouble(view.getPhoneText());

            String patLastName = view.getLastName();
            String patName = view.getName();
            String email = normalizeEmail(view.getEmail());
            LocalDate birthdate = view.getBirthdate();

            char gender = mapGender(view.getGender());

            Patient newPatient = new Patient();
            newPatient.setActive(true);
            newPatient.setFirstName(patName);
            newPatient.setFirstLastName(patLastName);
            newPatient.setIdUser(patId);
            newPatient.setBirthdate(birthdate);
            newPatient.setCellnumber(cellNumber);
            newPatient.setGender(gender);
            newPatient.setEmail(email);
            newPatient.setPasswordHash("pending");
            newPatient.setPasswordSalt("pending");

            String login = (email != null && !email.isBlank()) ? email : ("doc_" + view.getIdText());
            newPatient.setLogin(login);

            boolean ok = patientService != null && patientService.register(newPatient);
            if (ok) {
                view.showInfo("Paciente registrado");
                view.close();
            } else {
                view.showInfo("Ocurrió un error al registrar");
            }
        } catch (NumberFormatException ex) {
            view.showInfo("Los campos numéricos deben contener solo números");
        }
    }

    private boolean validateRequiredFields() {
        if (isBlank(view.getIdText())
                || isBlank(view.getLastName())
                || isBlank(view.getName())
                || isBlank(view.getGender())
                || isBlank(view.getPhoneText())
                || view.getBirthdate() == null) {
            view.showInfo("Todos los campos son obligatorios excepto el email");
            return false;
        }
        return true;
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    private static String normalizeEmail(String email) {
        if (email == null) return null;
        String trimmed = email.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private static char mapGender(String gender) {
        if ("Masculino".equals(gender)) return 'M';
        return 'F';
    }
}

