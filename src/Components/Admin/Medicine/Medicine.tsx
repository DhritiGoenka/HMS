import { ActionIcon, Button, Fieldset, NumberInput, Select, TextInput } from "@mantine/core";
import { MedicineCategories, MedicineType} from "../../../Data/DropDownData";
import { IconEdit, IconEye, IconSearch, IconTrash } from "@tabler/icons-react";
import { useForm } from "@mantine/form";
import { errorNotification, successNotification } from "../../../Utility/NotificationUtil";
import { useDispatch } from "react-redux";
import { useEffect, useState } from "react";
import { Column } from "primereact/column";
import { DataTable, DataTableFilterMeta, DataTableFilterMetaData } from "primereact/datatable";
import { data, useNavigate } from "react-router-dom";
import { formatDate } from "../../../Utility/DateUtility";
import { FilterMatchMode } from "primereact/api";
import { Toolbar } from "primereact/toolbar";
import { createMedicine, getAllMedicines, updateMedicine } from "../../../Api/MedicineApi";
import { capitalizeFirstLetter } from "../../../Utility/Otherutility";

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

const Medicine = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const [data, setData] = useState<any[]>([]);
    const [edit, setEdit] = useState(false);
    const [loading, setLoading] = useState(false);
    const [globalFilterValue, setGlobalFilterValue] = useState<string>('');
    const [filters, setFilters] = useState<DataTableFilterMeta>({
        global: { value: null, matchMode: FilterMatchMode.CONTAINS } as DataTableFilterMetaData
    });

    useEffect(()=>{
        fetchData();
    },[])

    const fetchData = () => {
        getAllMedicines().then((data)=>{
            setData(data);
        }).catch((error)=>{
            errorNotification("Error fetching reports");
        })
    }

    const form = useForm({
        initialValues: {
            id:null,
            name: '',
            dosage: '',
            category: '',
            medicineType: '',
            manufacturer: '',
            unitPrice: 0,
        },
        validate:{
            name: (value) => value === '' ? 'Medicine name is required' : null,
            dosage: (value) => value === '' ? 'Dosage is required' : null,
            category: (value) => value === '' ? 'Category is required' : null,
            medicineType: (value) => value === '' ? 'Type is required' : null,
            manufacturer: (value) => value === '' ? 'Manufacturer is required' : null,
            unitPrice: (value) => value === 0 ? 'Unit price is required' : null,
        }
    });

    const handleSubmit = (data: any) => {
        console.log(data);
        let update = false;
        let method;
        if(data.id){
            update = true;
            method = updateMedicine;
        }
        else{
            method = createMedicine;
        }
        setLoading(true)
        method(data)
            .then((response) => {
                successNotification(`Medicine ${update ? 'edited' : 'added'} successfully`);
                form.reset();
                setEdit(false);
                fetchData();
            })
            .catch((error) => {
                errorNotification(error?.response?.data?.errorMessage || `Failed to ${update ? 'edit' : 'add'} medicine`);
            })
            .finally(() => {
                setLoading(false);
            });
    }

    const onEdit = (rowData: any) => {
        setEdit(true);
        form.setValues({
            id: rowData.id,
            name: rowData.name,
            dosage: rowData.dosage,
            category: rowData.category,
            medicineType: rowData.medicineType,
            manufacturer: rowData.manufacturer,
            unitPrice: rowData.unitPrice,
        });
    }

    const cancel=()=>{
        form.reset();
        setEdit(false);
    }

    const startToolbarTemplate = () => {
        return (
            <div className="flex flex-wrap gap-2 justify-between items-center">
                <Button variant = "filled" onClick={()=>setEdit(true)}>Add Medicine</Button>
                <TextInput leftSection={<IconSearch/>} fw={500} value={globalFilterValue} onChange={onGlobalFilterChange} placeholder="Keyword Search" />
            </div>
        );
    };

    const actionBodyTemplate = (rowData:any) => {
        return <div className='flex gap-5'>
            <ActionIcon onClick={()=>{onEdit(rowData)}}>
                <IconEdit size={20} stroke={1.5}/>
            </ActionIcon>
        </div>
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
                        globalFilterFields={['name', 'dosage', 'category', 'medicineType', 'manufacturer', 'unitPrice']}
                        emptyMessage="No prescriptions found." 
                        currentPageReportTemplate="Showing {first} to {last} of {totalRecords} entries"
                    >
                        <Column field="name" header="Medicine Name"/>
                        <Column field="dosage" header="Dosage"/>
                        <Column field="category" header="Category" body={(rowData) => capitalizeFirstLetter(rowData.category)} />
                        <Column field="medicineType" header="Type" body={(rowData) => capitalizeFirstLetter(rowData.medicineType)} />
                        <Column field="manufacturer" header="Manufacturer" />
                        <Column field="unitPrice" header="Unit Price" sortable body={(rowData) => rowData.unitPrice.toFixed(2)} />
                        <Column headerStyle={{ textAlign: 'center' }} bodyStyle={{ textAlign: 'center', overflow: 'visible' }} body={actionBodyTemplate}/>
                    </DataTable>
                </>)
            :

            <form  onSubmit={form.onSubmit(handleSubmit)} className="grid gap-5"> 
                <Fieldset legend={<span className="text-lg font-medium text-primary-500">Add Medicine</span>} radius="md" className="grid grid-cols-2 gap-4">
                    <TextInput {...form.getInputProps('name')} withAsterisk label="Medicine Name" placeholder="Enter medicine name" />
                    <TextInput {...form.getInputProps('dosage')} label="Dosage" placeholder="Enter dosage" />
                    <Select {...form.getInputProps('category')} label="Category" placeholder="Select category" data={MedicineCategories} />
                    <Select {...form.getInputProps('medicineType')} label="Type" placeholder="Select type" data={MedicineType} />
                    <TextInput {...form.getInputProps('manufacturer')} label="Manufacturer" placeholder="Enter manufacturer name" />
                    <NumberInput {...form.getInputProps('unitPrice')} label="Unit Price" placeholder="Enter unit price" min={0} step={0.01} clampBehavior="strict"/>
                </Fieldset>                

                <div className="flex justify-end gap-4 mt-4">
                    <Button variant="default" color="red" className="px-6" loading={loading} onClick={cancel}>
                        Cancel
                    </Button>

                    <Button type="submit" color="primary" className="px-6" loading={loading}>
                        {form.values?.id ? 'Update Medicine' : 'Add Medicine'}
                    </Button>
                </div>
            </form>}
        </div>
        
    )
}

export default Medicine;