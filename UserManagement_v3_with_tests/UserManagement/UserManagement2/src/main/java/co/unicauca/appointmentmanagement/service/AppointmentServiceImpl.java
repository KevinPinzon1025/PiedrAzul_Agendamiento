package co.unicauca.appointmentmanagement.service;

import co.unicauca.microkernel.piedaazul.common.entity.AppointmentEntity;
import co.unicauca.usermanagement.acces.IAppointmentRepository;
import co.unicauca.usermanagement.acces.ProfessionalRepositorySQL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class AppointmentServiceImpl implements IAppointmentService {

    private final IAppointmentRepository repository;
    private final ProfessionalRepositorySQL professionalRepository;

    public AppointmentServiceImpl(IAppointmentRepository repository,
                                  ProfessionalRepositorySQL professionalRepository) {
        this.repository = repository;
        this.professionalRepository = professionalRepository;
    }

    @Override
    public List<AppointmentEntity> getAll() {
        return repository.getAll();
    }

    @Override
    public List<AppointmentEntity> findByProfessionalAndDate(String professional, LocalDate date) {
        return repository.findByProfessionalAndDate(professional, date);
    }

    @Override
    public List<String> getAllProfessionals() {
        return professionalRepository.getActiveProfessionalNames();
    }

    @Override
    public boolean isDateAvailable(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
    }
    
    
}