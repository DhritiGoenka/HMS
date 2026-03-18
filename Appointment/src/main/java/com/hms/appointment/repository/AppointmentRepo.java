package com.hms.appointment.repository;

import com.hms.appointment.dto.AppointmentDetails;
import com.hms.appointment.entity.Appointment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AppointmentRepo extends CrudRepository<Appointment, Long> {
    @Query("SELECT new com.hms.appointment.dto.AppointmentDetails(" +
            "a.id, " +
            "a.patientId, " +
            "a.doctorId, " +
            "null, " +
            "null, " +
            "null, " +
            "null, " +
            "a.appointmentTime, " +
            "a.status, " +
            "a.reason, " +
            "a.notes) " +
            "FROM Appointment a WHERE a.patientId = ?1")
    List<AppointmentDetails> findAllByPatientId(Long patientId);

    @Query("SELECT new com.hms.appointment.dto.AppointmentDetails(" +
            "a.id, " +
            "a.patientId, " +
            "a.doctorId, " +
            "null, " +
            "null, " +
            "null, " +
            "null, " +
            "a.appointmentTime, " +
            "a.status, " +
            "a.reason, " +
            "a.notes) " +
            "FROM Appointment a WHERE a.doctorId = ?1")
    List<AppointmentDetails> findAllByDoctorId(Long doctorId);
}
