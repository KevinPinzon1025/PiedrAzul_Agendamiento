package co.unicauca.usermanagement.service;

/**
 * Listener para notificar cambios en el conjunto de pacientes.
 * La vista (UI) puede observar el modelo (servicio) y refrescar su estado.
 */
public interface IPatientChangeListener {
    void onPatientsChanged();
}

