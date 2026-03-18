package com.hms.appointment.repository;

import com.hms.appointment.entity.AppointmentRecord;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AppointmentRecordRepo extends CrudRepository<AppointmentRecord, Long> {
    Optional<AppointmentRecord> findByAppointment_Id(Long appointmentId);
}
