import axiosInstance from "../Interceptor/AxiosIntercepter";

const getPatient=async (id: any)=>{
    return axiosInstance.get(`/profile/patient/get/${id}`)
    .then((response:any)=>response.data)
    .catch((error:any)=>{throw error;})
}

const updatePatient=async (patient: any)=>{
    const cleanedPatient = {
        ...patient,
        bloodGroup: patient.bloodGroup || null,  // convert "" → null
        allergies: Array.isArray(patient.allergies)
            ? patient.allergies.join(", ")
            : patient.allergies || null,

        chronicDisease: Array.isArray(patient.chronicDisease)
            ? patient.chronicDisease.join(", ")
            : patient.chronicDisease || null
    }

    return axiosInstance.put('/profile/patient/update', cleanedPatient)
        .then((response: any) => response.data)
        .catch((error: any) => { throw error; })
}

const getPatientDropdown=async ()=>{
    return axiosInstance.get('/profile/patient/dropdowns')
    .then((response:any)=>response.data)
    .catch((error:any)=>{throw error;}) 
}

export {getPatient, updatePatient, getPatientDropdown};