import React, {useState, useEffect} from 'react'
import { FilterMatchMode, FilterOperator } from 'primereact/api';
import { DataTable, DataTableFilterMeta, DataTableFilterMetaData } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { Tag } from 'primereact/tag';
import { ActionIcon, Button, LoadingOverlay, Modal, SegmentedControl, Select, Textarea, TextInput } from '@mantine/core';
import { IconEdit, IconEye, IconPlus, IconSearch, IconTrash } from '@tabler/icons-react';
import { useDisclosure } from '@mantine/hooks';
import { getPatientDropdown } from '../../../Api/PatientProfileApi';
import { DateTimePicker } from '@mantine/dates';
import { useForm } from '@mantine/form';
import { appointmentReasons } from '../../../Data/DropDownData';
import { useSelector } from 'react-redux';
import { cancelAppointment, getAllAppointmentByDoctor, scheduleAppointment } from '../../../Api/AppointmentApi';
import { errorNotification, successNotification } from '../../../Utility/NotificationUtil';
import { formatDateWithTime } from '../../../Utility/DateUtility';
import { modals } from '@mantine/modals';
import { Text } from '@mantine/core';
import { Toolbar } from 'primereact/toolbar';
import { useNavigate } from 'react-router-dom';

const Appointment = () => {
    const navigate = useNavigate();
    
    const [patients, setPatients] = useState<any[]>([]); 
    const [appointments, setAppointments] = useState<any[]>([]);
    const [tab,setTab] = useState<string>('today');
    const [loading, setLoading] = useState(false); 
    const [opened, { open, close }] = useDisclosure(false);
    const user = useSelector((state: any)=> state.user);

    const [filters, setFilters] = useState<DataTableFilterMeta>({
        global: { value: null, matchMode: FilterMatchMode.CONTAINS },
        patientName: { operator: FilterOperator.AND, constraints: [{ value: null, matchMode: FilterMatchMode.STARTS_WITH }] },
        patientEmail: { operator: FilterOperator.AND, constraints: [{ value: null, matchMode: FilterMatchMode.STARTS_WITH }] },
        reason: { operator: FilterOperator.AND, constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }] },
        status: { operator: FilterOperator.OR, constraints: [{ value: null, matchMode: FilterMatchMode.EQUALS }] }
    });

    const [globalFilterValue, setGlobalFilterValue] = useState<string>('');
   
    useEffect(() => {
        getAllAppointmentByDoctor(user.profileId).then((data)=>{
            setAppointments(data);
        }).catch((error)=>{
            errorNotification("Error fetching appointments");
        });
        
        getPatientDropdown().then((data) =>{
            setPatients(data.map((patient: any) => ({ 
                label: ""+patient.name, 
                value: ""+patient.id 
            })));
        }).catch((error) => {
            console.error("Error fetching patient dropdown data:", error);
        });

    }, []);

    const onGlobalFilterChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value;
        let _filters = { ...filters };

        (_filters['global'] as DataTableFilterMetaData).value = value;

        setFilters(_filters);
        setGlobalFilterValue(value);
    };

    const getSeverity = (status: string) => {
        switch (status) {
            case 'CANCELLED':
                return 'danger';

            case 'COMPLETED':
                return 'success';

            case 'SCHEDULED':
                return 'info';

            case 'negotiation':
                return 'warning';

            case 'default':
                return null;
        }
    };    

    const statusBodyTemplate = (rowData: any) => {
        return <Tag value={rowData.status} severity={getSeverity(rowData.status)} />;
    };

    const actionBodyTemplate = (rowData:any) => {
        return <div className='flex gap-5'>
            <ActionIcon onClick={()=>navigate(""+rowData.id)}>
                <IconEye size={20} stroke={1.5}/>
            </ActionIcon>
            <ActionIcon color="red" onClick={()=>handleDelete(rowData)}>
                <IconTrash size={20} stroke={1.5}/>
            </ActionIcon>
        </div>
    };

    const timeTemplate = (rowData: any)=>{
        return <>{formatDateWithTime(rowData.appointmentTime)}</>;
    }

    const leftToolbarTemplate = () => {
        return (
            <Button leftSection={<IconPlus />} variant='filled' onClick={open}>Schedule Appointment</Button> 
        );
    };

    const centerToolbarTemplate = () => {
        return (
            <SegmentedControl
                value={tab}
                onChange={setTab}
                data={["Today","Upcoming","Past"]}
                variant='filled'
                color='primary'
            />
            
        );
    };

    const rightToolbarTemplate = () => {
        return (
            <TextInput leftSection={<IconSearch/>} fw={500} value={globalFilterValue} onChange={onGlobalFilterChange} placeholder="Keyword Search" />
        );
    };

    const form = useForm({
        initialValues: {
            patientId: '',
            doctorId: user.profileId, 
            appointmentTime: new Date(),
            reason: '',
            notes: ''
        },
        validate: {
            patientId: (value) => value ? null : 'Patient is required',
            appointmentTime: (value) => value ? null : 'Appointment date is required',
            reason: (value) => value ? null : 'Reason for appointment is required',
        }
    });

    const handleDelete = (rowData: any) => {
        modals.openConfirmModal({
            title: <span className='text-xl font-serif'>Do you want to delete?</span>,
            centered: true,
            children: (
                <Text size="sm">
                    Are you sure you want to cancel this appointment? This action cannot be undone.
                </Text>
            ),
            labels: { confirm: 'Delete', cancel: 'Cancel' },
            onCancel : () => {
                
            },
            onConfirm: () => {
                cancelAppointment(rowData.id).then((response) => {
                    setAppointments((prev) => prev.map((appointment) => {
                        if (appointment.id == rowData.id) {
                            return { ...appointment, status: 'CANCELLED' };
                        }
                        return appointment;
                    }));
                    successNotification("Your appointment has been successfully cancelled.");
                }).catch((error) => {
                    errorNotification(error.response?.data?.errorMessage || "Failed to cancel appointment. Please try again."); 
                });
            }
        });
    };

    const formatLocalDateTime = (date: Date) => {
        return date.toLocaleString('sv-SE').replace(' ', 'T');
    };

    const handleSubmit = (values: typeof form.values) => {
        setLoading(true);
        const payload = {
            ...values,
            patientId: Number(values.patientId),
            appointmentTime: values.appointmentTime
                ? formatLocalDateTime(values.appointmentTime)
                : null
        };
        
        scheduleAppointment(payload).then((response) => {
            getAllAppointmentByDoctor(user.profileId)
            .then((data) => {
                setAppointments(data);
            })
            .catch((error) => {
                errorNotification(error.response?.data?.errorMessage || "Failed to refresh appointments. Please try again.");
            });
            close();
            form.reset();
            successNotification("The appointment has been successfully scheduled.");
        }).catch((error) => {
            errorNotification(error.response?.data?.errorMessage || "Failed to schedule appointment. Please try again.");
        }).finally(() => {
            setLoading(false);
        });
    };

    const filterAppointments = appointments.filter((appointment) => {
        const appointmentDate = new Date(appointment.appointmentTime);
        const today = new Date();
        appointmentDate.setHours(0, 0, 0, 0);
        today.setHours(0, 0, 0, 0);
        if (tab === 'Today') {
            return appointmentDate.getTime() === today.getTime();
        }
        else if (tab === 'Upcoming') {
            return appointmentDate.getTime() > today.getTime();
        }
        else if (tab === 'Past') {
            return appointmentDate.getTime() < today.getTime();
        }

        return true;
    });

    return (
        <div className="card">
            <Toolbar className="mb-4" start={centerToolbarTemplate} end={rightToolbarTemplate}></Toolbar>
            <DataTable 
                value={filterAppointments} 
                size='small'
                paginator 
                rows={10}
                paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                rowsPerPageOptions={[10, 25, 50]} 
                dataKey="id"
                filters={filters} 
                filterDisplay="menu" 
                globalFilterFields={['patientName', 'reason', 'notes', 'status']}
                emptyMessage="No appointments found." 
                currentPageReportTemplate="Showing {first} to {last} of {totalRecords} entries"
            >
                <Column field="patientName" header="Patient" sortable filter filterPlaceholder="Search by name" style={{ minWidth: '14rem' }} />
                <Column field="patientEmail" header="Patient Email" filter filterPlaceholder="Search by email" style={{ minWidth: '14rem' }} />
                <Column field="appointmentTime" header="Appointment Time" sortable style={{ minWidth: '14rem' }} body={timeTemplate}/>
                <Column field="reason" header="Reason" filter filterPlaceholder="Search by reason" style={{ minWidth: '14rem' }} />
                <Column field="notes" header="Notes" style={{ minWidth: '14rem' }} />
                <Column field="status" header="Status" filterMenuStyle={{width:'14rem'}} style={{minWidth: '12rem'}} body={statusBodyTemplate}/>
                <Column headerStyle={{ width: '5rem', textAlign: 'center' }} bodyStyle={{ textAlign: 'center', overflow: 'visible' }} body={actionBodyTemplate} />
            </DataTable>
            
            <Modal opened={opened} onClose={close} size="lg" title={<div className='text-xl font-semibold text-primary-500'>Schedule Appointment</div>} centered>
                <LoadingOverlay visible={loading} zIndex={1000} overlayProps={{radius:"sm", blur: 2}} />   
                <form onSubmit={form.onSubmit(handleSubmit)} className='grid grid-cols-1 gap-5'>
                    <Select {...form.getInputProps('Id')} withAsterisk data={patients} label="Patients" placeholder='Select Patient'/>
                    <DateTimePicker minDate={new Date()}{...form.getInputProps('appointmentTime')} withAsterisk label="Appointment Date & Time" placeholder='Select Date & Time'/>
                    <Select {...form.getInputProps('reason')} withAsterisk data={appointmentReasons} label="Reason for Appointment" placeholder='Enter reason for appointment' mt="md"/>
                    <Textarea {...form.getInputProps('notes')} label="Additional Notes" placeholder='Enter any additional notes' mt="md"/>
                    <Button type="submit" variant='filled' fullWidth className='mt-4'>Schedule</Button>
                </form>
            </Modal>
        </div>
    );
}     

export default Appointment
