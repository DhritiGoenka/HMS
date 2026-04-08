import { ActionIcon, Button, Fieldset, MultiSelect, NumberInput, Select, Textarea, TextInput } from "@mantine/core";
import { medicationFrequencies, symptoms, tests } from "../../../Data/DropDownData";
import { IconEye, IconSearch, IconTrash } from "@tabler/icons-react";
import { useForm } from "@mantine/form";
import { createAppointmentReport, getReportsByPatientId, reportExists } from "../../../Api/AppointmentReportApi";
import { errorNotification, successNotification } from "../../../Utility/NotificationUtil";
import { useDispatch } from "react-redux";
import { useEffect, useState } from "react";
import { Column } from "primereact/column";
import { DataTable, DataTableFilterMeta, DataTableFilterMetaData } from "primereact/datatable";
import { data, useNavigate } from "react-router-dom";
import { formatDate } from "../../../Utility/DateUtility";
import { FilterMatchMode } from "primereact/api";
import { Toolbar } from "primereact/toolbar";

type Medicine={
    name: string;
    medicineId?: number;
    dosage: string;
    frequency: string;
    duration: number;
    route?: string;
    type?: string;
    instructions?: string;
    prescriptionId?: number;
}

const AppointmentReport = ({appointment}: any) => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const [data, setData] = useState<any[]>([]);
    const [allowAdd, setAllowAdd] = useState(false);
    const [edit, setEdit] = useState(false);
    const [loading, setLoading] = useState(false);
    const [globalFilterValue, setGlobalFilterValue] = useState<string>('');
    const [filters, setFilters] = useState<DataTableFilterMeta>({
        global: { value: null, matchMode: FilterMatchMode.CONTAINS } as DataTableFilterMetaData
    });

    useEffect(()=>{
        fetchData();
    },[appointment])

    const fetchData = () => {
        if (!appointment?.patientId) return;
        getReportsByPatientId(appointment.patientId).then((data)=>{
            setData(data);
        }).catch((error)=>{
            errorNotification("Error fetching reports");
        })
        reportExists(appointment?.id).then((response)=>{
            setAllowAdd(!response);
        }).catch((error)=>{
            errorNotification("Error checking report existence");
        })
    }

    const form = useForm({
        initialValues: {
            symptoms: [],
            tests: [],
            diagnosis: '',
            referredBy: '',
            referredTo: '',
            notes: '',
            prescription: {
                notes:'',
                medicines:[] as Medicine[]
            }
        },
        validate:{
            symptoms: (value) => value.length === 0 ? 'Please select at least one symptom' : null,
            diagnosis: (value) => value.trim() === '' ? 'Diagnosis is required' : null,
            prescription: {
                medicines: {
                    name: (value) => value.trim() === '' ? 'Medicine name is required' : null,
                    dosage: (value) => value.trim() === '' ? 'Dosage is required' : null,
                    frequency: (value) => value.trim() === '' ? 'Frequency is required' : null,
                    duration: (value) => value <= 0 ? 'Duration must be greater than 0' : null
                }
            }
        }
    });

    const insertMedicine = () => {
        form.insertListItem('prescription.medicines', {
            name: '',
            dosage: '',
            frequency: '',
            duration: 0,
            route: '',
            type: '',
            instructions: ''
        });
    }

    const removeMedicine = (index: number) => {
        form.removeListItem('prescription.medicines', index);
    }

    const handleSubmit = (values: typeof form.values) => {
        let data = {
            ...values,
            doctorId: appointment.doctorId,
            patientId: appointment.patientId,
            appointmentId: appointment.id,
            prescription:{
                ...values.prescription,
                doctorId: appointment.doctorId,
                patientId: appointment.patientId,
                appointmentId: appointment.id
            }
        }
        setLoading(true);
        createAppointmentReport(data)
        .then((response) => {
            successNotification("Report created successfully");
            form.reset();
            setEdit(false);
            setAllowAdd(false);
            fetchData();
        })
        .catch((error) => {
            errorNotification(error?.response?.data?.errorMessage || "Failed to create report");
        })
        .finally(() => {
            setLoading(false);
        });
    }

    const startToolbarTemplate = () => {
        return (
            <div className="flex flex-wrap gap-2 justify-between items-center">
                {allowAdd && <Button variant = "filled" onClick={()=>setEdit(true)}>Add Report</Button>}
                <TextInput leftSection={<IconSearch/>} fw={500} value={globalFilterValue} onChange={onGlobalFilterChange} placeholder="Keyword Search" />
            </div>
        );
    };

    const actionBodyTemplate = (rowData:any) => {
        // return <div className='flex gap-5'>
        //     <ActionIcon onClick={()=>navigate("/doctor/appointments/"+rowData.appointmentId)}>
        //         <IconEye size={20} stroke={1.5}/>
        //     </ActionIcon>
        // </div>
    };

    const onGlobalFilterChange = (e: React.ChangeEvent<HTMLInputElement>) => {
            const value = e.target.value;
            let _filters = { ...filters };
        
            (_filters['global'] as DataTableFilterMetaData).value = value;
        
            setFilters(_filters);
            setGlobalFilterValue(value);
        };

    return(
        <div>
            {!edit ? (
                <> 
                    <Toolbar className="mb-4" start={startToolbarTemplate}></Toolbar>
                    <DataTable 
                        value={data} 
                        size='small'
                        paginator 
                        rows={10}
                        paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                        rowsPerPageOptions={[10, 25, 50]} 
                        dataKey="id"
                        filterDisplay="menu" 
                        globalFilterFields={['doctorName', 'diagnosis']}
                        emptyMessage="No prescriptions found." 
                        currentPageReportTemplate="Showing {first} to {last} of {totalRecords} entries"
                    >
                        <Column field="doctorName" header="Doctor"/>
                        <Column field="reportDate" header="Report Date" sortable body={(rowData) => formatDate(rowData.createdAt)} />
                        <Column field="diagnosis" header="Diagnosis"/>
                        <Column field="notes" header="Notes" />
                        {/* <Column headerStyle={{ width: '5rem', textAlign: 'center' }} bodyStyle={{ textAlign: 'center', overflow: 'visible' }} body={actionBodyTemplate} /> */}
                    </DataTable>
                </>)
            :

            <form onSubmit={form.onSubmit(handleSubmit)} className="grid gap-5"> 
                <Fieldset legend={<span className="text-lg font-medium text-primary-500">Personal Information</span>} radius="md" className="grid grid-cols-2 gap-4">
                    <MultiSelect
                        {...form.getInputProps('symptoms')}
                        withAsterisk
                        label="Symptoms"
                        placeholder="Select symptoms"
                        data={symptoms}
                        searchable
                        clearable
                        className="col-span-2"
                    />
                    <MultiSelect
                        {...form.getInputProps('tests')}
                        label="Tests"
                        placeholder="Select tests"
                        data={tests}
                        searchable
                        clearable
                        className="col-span-2"
                    />
                    <TextInput {...form.getInputProps('diagnosis')} withAsterisk label="Diagnosis" placeholder="Enter diagnosis" className="col-span-2" />
                    <TextInput {...form.getInputProps('referredBy')} label="Referred By" placeholder="Enter referral details" />
                    <TextInput {...form.getInputProps('referredTo')} label="Referred To" placeholder="Enter referral details" />
                    <Textarea {...form.getInputProps('notes')} label="Additional Notes" placeholder="Enter any additional notes" className="col-span-2" />

                </Fieldset>


                <Fieldset legend={<span className="text-lg font-medium text-primary-500">Prescription</span>} radius="md" className="grid gap-5">
                    <Textarea {...form.getInputProps('prescription.notes')} label="Notes" placeholder="Enter any notes" className="col-span-2" />
                    {
                        form.values.prescription.medicines.map((medicine, index) => (
                            <Fieldset key={index} className="grid gap-4 col-span-2 grid-cols-2">
                                
                                <div className="flex items-center justify-between col-span-2">
                                    <h1 className="text-lg font-medium">Medicine {index + 1}</h1>
                                    <ActionIcon onClick={() => removeMedicine(index)} variant="filled" color="red" size="lg">
                                        <IconTrash />
                                    </ActionIcon>
                                </div>

                                <TextInput {...form.getInputProps(`prescription.medicines.${index}.name`)} withAsterisk label="Medicine Name" placeholder="Enter medicine name" />
                                <TextInput {...form.getInputProps(`prescription.medicines.${index}.dosage`)} withAsterisk label="Dosage" placeholder="Enter dosage" />
                                <Select {...form.getInputProps(`prescription.medicines.${index}.frequency`)} withAsterisk label="Frequency" placeholder="Select frequency" data={medicationFrequencies} />
                                <NumberInput {...form.getInputProps(`prescription.medicines.${index}.duration`)} withAsterisk label="Duration (days)" placeholder="Enter duration in days" min={1} />
                                <Select
                                    label="Route of Administration"
                                    placeholder="Select route"
                                    data={["Oral", "Intravenous", "Inhalation"]}
                                    {...form.getInputProps(`prescription.medicines.${index}.route`)}
                                />
                                <Select
                                    label="Type"
                                    placeholder="Select type"
                                    data={["Tablet", "Syrup", "Injection","Capsule","Ointment"]}
                                    {...form.getInputProps(`prescription.medicines.${index}.type`)}
                                />
                                <Textarea
                                    label="Additional Instructions"
                                    placeholder="Enter any additional instructions"
                                    className="col-span-2"
                                    {...form.getInputProps(`prescription.medicines.${index}.instructions`)}
                                />
                            </Fieldset>
                        ))
                    }
                    <div className=" col-span-2 flex items-center justify-center">
                        <Button onClick={insertMedicine} variant="outline" color='primary' className="col-span-2">
                            Add Medicine
                        </Button> 
                    </div>
                    
                </Fieldset>

                <div className="flex justify-end gap-4 mt-4">
                    <Button variant="default" color="red" className="px-6" loading={loading}>
                        Cancel
                    </Button>

                    <Button type="submit" color="primary" className="px-6" loading={loading}>
                        Save Report
                    </Button>
                </div>
            </form>}
        </div>
        
    )
}

export default AppointmentReport;