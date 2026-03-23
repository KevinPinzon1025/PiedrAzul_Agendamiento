package co.unicauca.appointmentmanagement.service;


import co.unicauca.microkernel.piedaazul.common.entity.AppointmentEntity;
import java.time.LocalDate;
import java.util.List;

public interface IAppointmentService {
    List<AppointmentEntity> getAll();
    List<AppointmentEntity> findByProfessionalAndDate(String professional, LocalDate date);
    List<String> getAllProfessionals();
}