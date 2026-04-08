import { ActionIcon, Card, Divider, Grid, Modal, Text, TextInput, Title } from '@mantine/core';
import { IconEye, IconMedicineSyrup, IconSearch } from '@tabler/icons-react';
import { FilterMatchMode} from 'primereact/api';
import { Column } from 'primereact/column';
import { DataTable, DataTableFilterMeta, DataTableFilterMetaData } from 'primereact/datatable';
import { Toolbar } from 'primereact/toolbar';
import React, {useEffect, useState} from 'react'
import { getPrescriptionsByPatientId } from '../../../Api/AppointmentReportApi';
import { errorNotification } from '../../../Utility/NotificationUtil';
import { formatDate } from '../../../Utility/DateUtility';
import { useNavigate } from 'react-router-dom';
import { useDisclosure } from '@mantine/hooks';

const Prescriptions = ({appointment}:any) => {
    const navigate = useNavigate();
    const [data, setData] = useState<any[]>([]);
    const [medicineData, setMedicineData] = useState<any[]>([]);
    const [opened, {open,close}] = useDisclosure(false); 
    const [globalFilterValue, setGlobalFilterValue] = useState<string>('');
    const [filters, setFilters] = useState<DataTableFilterMeta>({
        global: { value: null, matchMode: FilterMatchMode.CONTAINS } as DataTableFilterMetaData
    });

    useEffect(() =>{
        if (!appointment?.patientId) return;
        getPrescriptionsByPatientId(appointment?.patientId).then((data)=>{
            setData(data);
        }).catch((error)=>{
            errorNotification("Error fetching prescriptions");
        });
    },[appointment])

    const onGlobalFilterChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value;
        let _filters = { ...filters };
    
        (_filters['global'] as DataTableFilterMetaData).value = value;
    
        setFilters(_filters);
        setGlobalFilterValue(value);
    };

    const startToolbarTemplate = () => {
        return (
            <TextInput leftSection={<IconSearch/>} fw={500} value={globalFilterValue} onChange={onGlobalFilterChange} placeholder="Keyword Search" />
        );
    };

    const handleMedicine = (medicines: any[]) => {
        open();
        setMedicineData(medicines);
    }

    const actionBodyTemplate = (rowData:any) => {
        return <div className='flex gap-5'>
            <ActionIcon onClick={()=>navigate("/doctor/appointments/"+rowData.appointmentId)}>
                <IconEye size={20} stroke={1.5}/>
            </ActionIcon>
            <ActionIcon onClick={() => handleMedicine(rowData.medicines)} color="pink">
                <IconMedicineSyrup size={20} stroke={1.5}/>
            </ActionIcon>
        </div>
    };
    
    return (
        <div className='card'>
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
                globalFilterFields={['doctorName', 'prescriptionDate']}
                emptyMessage="No prescriptions found." 
                currentPageReportTemplate="Showing {first} to {last} of {totalRecords} entries"
            >
                <Column field="doctorName" header="Doctor"/>
                <Column field="prescriptionDate" header="Prescription Date" sortable body={(rowData) => formatDate(rowData.prescriptionDate)} />
                <Column field="medicines" header="Medicines" body={(rowData) => rowData.medicines?.length ?? 0} />
                <Column field="notes" header="Notes" style={{ minWidth: '14rem' }} />
                <Column headerStyle={{ width: '5rem', textAlign: 'center' }} bodyStyle={{ textAlign: 'center', overflow: 'visible' }} body={actionBodyTemplate} />
            </DataTable>
            <Modal opened={opened} size = "xl" onClose={close} title={<Title order={2}>Medicines</Title>}>
                <div className='grid grid-cols-2 gap-5'>
                {
                    medicineData?.map((data:any, index:number) => (
                        <Card key={index} shadow="md" padding="lg" radius="md" withBorder >
                            <Title order={4} mb="sm">{data.name} {(data.type)}</Title>
                            <Divider my="sm"/>
                            <Grid>
                                <Grid.Col span={6}>
                                    <Text size="sm" fw={500}>Dosage:</Text>
                                    <Text>{data.dosage}</Text>
                                </Grid.Col>
                                <Grid.Col span={6}>
                                    <Text size="sm" fw={500}>Frequency:</Text>
                                    <Text>{data.frequency}</Text>
                                </Grid.Col>
                                <Grid.Col span={6}>
                                    <Text size="sm" fw={500}>Days:</Text>
                                    <Text>{data.duration}</Text>    
                                </Grid.Col>
                                <Grid.Col span={6}>
                                    <Text size="sm" fw={500}>Route:</Text>
                                    <Text>{data.route}</Text>    
                                </Grid.Col>
                                <Grid.Col span={6}>
                                    <Text size="sm" fw={500}>Instructions:</Text>
                                    <Text>{data.instructions=="" ? "Not specified":data.instructions}</Text>    
                                </Grid.Col>
                            </Grid>
                        </Card>
                    ))
                }
                </div>
                {
                    medicineData?.length === 0 && (
                        <Text color = "dimmed" size = "sm" mt = "md">
                            No medicines found for this prescription.
                        </Text>
                    )
                }
            </Modal>    
        </div>
    )
}

export default Prescriptions;
