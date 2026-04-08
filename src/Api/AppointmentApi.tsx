import axiosInstance from "../Interceptor/AxiosIntercepter";

const scheduleAppointment=async (data: any)=>{
    return axiosInstance.post("/appointment/schedule",data)
    .then((response:any)=>response.data)
    .catch((error:any)=>{throw error;})
}

const cancelAppointment=async (id: any)=>{
    return axiosInstance.put(`/appointment/cancel/${id}`)
    .then((response:any)=>response.data)
    .catch((error:any)=>{throw error;})
}

const getAppointmentById=async(id: any)=>{
    return axiosInstance.get(`/appointment/get/${id}`)
    .then((response:any)=>response.data)
    .catch((error:any)=>{throw error;})
}

const getAppointmentWithNamesById=async(id: any)=>{
    return axiosInstance.get(`/appointment/get/details/${id}`)
    .then((response:any)=>response.data)
    .catch((error:any)=>{throw error;})
}

const getAllAppointmentByPatient=async(patientId: any)=>{
    return axiosInstance.get(`/appointment/getAllByPatient/${patientId}`)
    .then((response:any)=>response.data)
    .catch((error:any)=>{throw error;})
}

const getAllAppointmentByDoctor=async(doctorId: any)=>{
    return axiosInstance.get(`/appointment/getAllByDoctor/${doctorId}`)
    .then((response:any)=>response.data)
    .catch((error:any)=>{throw error;})
}
export {scheduleAppointment, cancelAppointment, getAppointmentById, getAppointmentWithNamesById, getAllAppointmentByPatient, getAllAppointmentByDoctor};
