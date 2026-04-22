import axiosInstance from "../Interceptor/AxiosIntercepter";

const createMedicine=async (data: any)=>{
    return axiosInstance.post("/pharmacy/medicines/create",data)
    .then((response:any)=>response.data)
    .catch((error:any)=>{throw error;})
}

const getMedicines=async (medicineId: any)=>{
    return axiosInstance.get("/pharmacy/medicines/" + medicineId)
    .then((response:any)=>response.data)
    .catch((error:any)=>{throw error;})
}

const updateMedicine=async (data:any)=>{
    return axiosInstance.put("/pharmacy/medicines/update",data)
    .then((response:any)=>response.data)
    .catch((error:any)=>{throw error;})
}

const getAllMedicines=async ()=>{
    return axiosInstance.get("/pharmacy/medicines/getAll")
    .then((response:any)=>response.data)
    .catch((error:any)=>{throw error;})
}

export {createMedicine, getMedicines, updateMedicine, getAllMedicines};