package com.hms.appointment.repository;

import com.hms.appointment.entity.AppointmentRecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AppointmentRecordRepo extends CrudRepository<AppointmentRecord, Long> {
    Optional<AppointmentRecord> findByAppointment_Id(Long appointmentId);

    List<AppointmentRecord> findByPatientId(Long patientId);

    Boolean existsByAppointment_Id(Long appointmentId);
}
