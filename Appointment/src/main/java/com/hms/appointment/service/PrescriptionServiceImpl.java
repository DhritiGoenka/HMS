package com.hms.appointment.service;

import com.hms.appointment.clients.ProfileClient;
import com.hms.appointment.dto.DoctorDropDown;
import com.hms.appointment.dto.PrescriptionDTO;
import com.hms.appointment.dto.PrescriptionDetails;
import com.hms.appointment.entity.Prescription;
import com.hms.appointment.exception.HmsException;
import com.hms.appointment.repository.PrescriptionRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PrescriptionServiceImpl implements PrescriptionService{

    private final PrescriptionRepo prescriptionRepo;
    private final MedicineService medicineService;
    private final ProfileClient profileClient;

    @Override
    public Long savePrescription(PrescriptionDTO prescriptionDTO) {
        prescriptionDTO.setPrescriptionDate(LocalDate.now());
        Long prescriptionId = prescriptionRepo.save(prescriptionDTO.toEntity()).getId();
        prescriptionDTO.getMedicines().forEach(medicine->
                medicine.setPrescriptionId(prescriptionId));
        medicineService.saveAllMedicines(prescriptionDTO.getMedicines());
        return prescriptionId;
    }

    @Override
    public PrescriptionDTO getPrescriptionByAppointmentId(Long appointmentId) throws HmsException{
        PrescriptionDTO prescriptionDTO = prescriptionRepo.findByAppointment_Id(appointmentId).orElseThrow(()->new HmsException("PRESCRIPTION_NOT_FOUND")).toDTO();
        prescriptionDTO.setMedicines(medicineService.getAllMedicinesByPrescriptionId(prescriptionDTO.getId()));
        return prescriptionDTO;
    }

    @Override
    public PrescriptionDTO getPrescriptionById(Long prescriptionId) throws HmsException{
        PrescriptionDTO prescriptionDTO = prescriptionRepo.findById(prescriptionId).orElseThrow(()->new HmsException("PRESCRIPTION_NOT_FOUND")).toDTO();
        prescriptionDTO.setMedicines(medicineService.getAllMedicinesByPrescriptionId(prescriptionDTO.getId()));
        return prescriptionDTO;
    }

    @Override
    public List<PrescriptionDetails> getPrescriptionsByPatientId(Long patientId) throws HmsException {
        List<Prescription> prescriptions = prescriptionRepo.findAllByPatientId(patientId);
        List<PrescriptionDetails> presDetails = prescriptions.stream()
                .map(Prescription::toPrescriptionDetails)
                .toList();
        presDetails.forEach(details->{
            details.setMedicines(medicineService.getAllMedicinesByPrescriptionId(details.getId()));
        });

        List<Long> doctorIds = presDetails.stream()
                .map(PrescriptionDetails::getDoctorId)
                .distinct().toList();

        List<DoctorDropDown> doctors = profileClient.getDoctorsById(doctorIds);
        Map<Long, DoctorDropDown> doctormap = doctors.stream()
                .collect(Collectors.toMap(DoctorDropDown::getId, doctor->doctor));

        presDetails.forEach(details->{
            DoctorDropDown doctor = doctormap.get(details.getDoctorId());
            if(doctor!=null){
                details.setDoctorName(
                        doctor.getName() != null ? doctor.getName() : "Unknown Doctor"
                );

                details.setDoctorEmail(
                        doctor.getEmail() != null ? doctor.getEmail() : "N/A"
                );

                details.setDoctorPhone(
                        doctor.getPhone() != null ? doctor.getPhone() : "N/A"
                );
            }
            else {
                details.setDoctorName("Unknown Doctor");
                details.setDoctorEmail("N/A");
                details.setDoctorPhone("N/A");
            }
        });
        return presDetails;
    }
}
