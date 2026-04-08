import axiosInstance from "../Interceptor/AxiosIntercepter";

const createAppointmentReport=async (data: any)=>{
    return axiosInstance.post("/appointment/record/create",data)
    .then((response:any)=>response.data)
    .catch((error:any)=>{throw error;})
}

const reportExists=async (appointmentId: any)=>{
    return axiosInstance.get("/appointment/record/recordExists/" + appointmentId)
    .then((response:any)=>response.data)
    .catch((error:any)=>{throw error;})
}

const getReportsByPatientId=async (patientId: any)=>{
    return axiosInstance.get("/appointment/record/getRecordsByPatientId/" + patientId)
    .then((response:any)=>response.data)
    .catch((error:any)=>{throw error;})
}

const getPrescriptionsByPatientId=async (patientId: any)=>{
    return axiosInstance.get("/appointment/record/getPrescriptionsByPatientId/" + patientId)
    .then((response:any)=>response.data)
    .catch((error:any)=>{throw error;})
}
export {createAppointmentReport, reportExists, getReportsByPatientId, getPrescriptionsByPatientId};