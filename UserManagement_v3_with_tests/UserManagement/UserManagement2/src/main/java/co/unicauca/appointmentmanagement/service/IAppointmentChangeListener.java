package co.unicauca.appointmentmanagement.service;

/**
 * Listener para notificar cambios en el conjunto de citas.
 * La vista puede refrescar datos (disponibles/ocupados) sin acoplarse al modelo.
 */
public interface IAppointmentChangeListener {
    void onAppointmentsChanged();
}

