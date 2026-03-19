package co.unicauca.appointmentmanagement.service;

import co.unicauca.microkernel.piedaazul.common.entity.AppointmentEntity;
import java.util.List;

public interface IAppointmentService {
    List<AppointmentEntity> getAll();
}