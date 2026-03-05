package com.hms.appointment.repository;

import com.hms.appointment.entity.Appointment;
import org.springframework.data.repository.CrudRepository;

public interface AppointmentRepo extends CrudRepository<Appointment, Long> {
}
