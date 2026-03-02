import { Avatar, Button, Divider, Modal, NumberInput, Select, TextInput } from '@mantine/core'
import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import { Table } from "@mantine/core";
import { IconEdit } from '@tabler/icons-react';
import { DateInput } from '@mantine/dates';
import { doctorSpecialisations, doctorDepartment } from '../../../Data/DropDownData';
import { useDisclosure } from '@mantine/hooks';
import { getDoctor, updateDoctor } from '../../../Api/DoctorProfileApi';
import {formatDate } from '../../../Utility/DateUtility';
import { useForm } from '@mantine/form';
import { errorNotification, successNotification } from '../../../Utility/NotificationUtil';

type DoctorFormValues = {
    dob: Date | null;
    phone: string;
    address: string;
    licenseNumber: string;
    specialisation: string;
    department: string;
    totalExperience: number | "";
};

const Profile = () => {

    const user = useSelector((state:any) => state.user);
    const [editMode, setEditMode] = useState(false);
    const [opened, {open,close}] = useDisclosure(false);
    const [profile,setProfile] = useState<any>(null);

    const form = useForm<DoctorFormValues>({
        initialValues: {
            dob: null,
            phone: "",
            address: "",
            licenseNumber: "",
            specialisation: "",
            department: "",
            totalExperience: ""
        },
        validate: {
            dob: (value) => !value ? "Date of Birth is required" : undefined,
            phone: (value) => !value ? "Phone is required" : undefined,
            address: (value) => !value ? "Address is required" : undefined,
            licenseNumber: (value) => !value ? "License Number is required" : undefined,
            specialisation: (value) => !value ? "Specialisation is required" : undefined,
            department: (value) => !value ? "Department is required" : undefined,
        }
    });

    const handleEdit = () => {
        form.reset();
        form.setValues({
            ...profile,
            dob: profile?.dob ? new Date(profile.dob) : null,
            totalExperience: profile?.totalExperience ?? ""
        });
        setEditMode(true);
    };

    const handleSubmit = (values: DoctorFormValues) => {

        const payload = {
            ...profile,
            ...values,
            id: user.profileId
        };

        updateDoctor(payload)
            .then((data: any) => {

                setProfile(data);

                form.reset();
                setEditMode(false);

                successNotification("Doctor profile updated successfully");
            })
            .catch((err: any) => {
                errorNotification(err.response?.errorMessage || "Update failed");
            });
    };

    useEffect(() => {
        if (!user?.profileId) return;
        getDoctor(user.profileId)
            .then((data: any) => {
                setProfile(data);
            })
            .catch(() => {
                errorNotification("Failed to load doctor profile");
            });
    }, [user?.profileId]);
    
    return (
        <div className='p-10'>
            <div className='flex justify-between items-center'>
                <div className='flex gap-5 items-center'>
                    <div className='flex flex-col items-center gap-3'>
                        <Avatar variant='filled' src="/avatar.png" alt="It's me" size={150}/> 
                        {editMode && <Button variant='filled' size="sm" onClick={open}>
                            Upload
                        </Button>}
                    </div>
                    
                    <div className='flex flex-col gap-3'>
                        <div className='text-3xl font-medium text-neutral-900'>{user.name}</div>
                        <div className='text-xl font-medium text-neutral-700'>{user.email}</div>
                    </div>
                </div>
                {!editMode ?
                    <Button size="lg" type="button" onClick={handleEdit} variant='filled' leftSection={<IconEdit/>} >
                        Edit Profile
                    </Button>
                    :
                    <Button size="lg" type="button" 
                        onClick={() => {
                            const result = form.validate();
                            if (!result.hasErrors) {
                                handleSubmit(form.values);
                            }
                        }}
                        variant = "filled"> 
                            Save Changes
                    </Button>
                }
            </div>

            <Divider my="xl" />

            <div>
                <div className='text-2xl font-medium text-neutral-900 mb-7'>
                    Personal Information
                </div>
                <Table striped stripedColor="primary.1" verticalSpacing="md" withRowBorders={false}>
                    <Table.Tbody>
                        <Table.Tr>
                            <Table.Td className='font-semibold text-xl'>Date of Birth</Table.Td>
                            <Table.Td className='text-lg'>
                                { editMode ?
                                    <DateInput {...form.getInputProps('dob')} placeholder="Enter Date of Birth" />
                                    :
                                    formatDate(profile?.dob) ?? '-'
                                }
                            </Table.Td>
                        </Table.Tr>

                        <Table.Tr>
                            <Table.Td className='font-semibold text-xl'>Phone</Table.Td>
                            <Table.Td className='text-lg'>
                                { editMode ?
                                    <NumberInput {...form.getInputProps('phone')} placeholder='Enter phone number' hideControls={true} maxLength={10} clampBehavior='strict'/>
                                    :
                                    profile?.phone ?? '-'
                                }
                            </Table.Td>
                        </Table.Tr>

                        <Table.Tr>
                            <Table.Td className='font-semibold text-xl'>Address</Table.Td>
                            <Table.Td className='text-lg'>
                                { editMode ?
                                    <TextInput {...form.getInputProps('address')} placeholder="Enter Address" />
                                    :
                                    profile?.address ?? '-'
                                }
                            </Table.Td>
                        </Table.Tr>

                        <Table.Tr>
                            <Table.Td className='font-semibold text-xl'>License Number</Table.Td>
                            <Table.Td className='text-lg'>
                                { editMode ?
                                    <TextInput {...form.getInputProps('licenseNumber')} placeholder="Enter License Number"/>
                                    :
                                    profile?.licenseNumber ?? '-'
                                }
                            </Table.Td>
                        </Table.Tr>

                        <Table.Tr>
                            <Table.Td className='font-semibold text-xl'>Specialisation</Table.Td>
                            <Table.Td className='text-lg'>
                                { editMode ?
                                    <Select data={doctorSpecialisations} {...form.getInputProps("specialisation")} placeholder='Select Specialisation' />
                                    :
                                    profile?.specialisation ?? '-'
                                }
                            </Table.Td> 
                        </Table.Tr>

                        <Table.Tr>
                            <Table.Td className='font-semibold text-xl'>Department</Table.Td>
                            <Table.Td className='text-lg'>
                                { editMode ?
                                    <Select data={doctorDepartment} {...form.getInputProps("department")} placeholder='Select Department' />
                                    :
                                    profile?.department ?? '-'
                                }
                            </Table.Td>
                        </Table.Tr>

                        <Table.Tr>
                            <Table.Td className='font-semibold text-xl'>Total Experience</Table.Td>
                            <Table.Td className='text-lg'>
                                { editMode ?
                                    <NumberInput {...form.getInputProps('totalExperience')} placeholder="Enter Total Experience" hideControls={true} max={50} clampBehavior='strict'/>
                                    :
                                    profile?.totalExperience
                                        ? `${profile.totalExperience} years`
                                        : "-"
                                }
                            </Table.Td>
                        </Table.Tr>
                    </Table.Tbody>
                </Table>
            </div>
            <Modal centered opened={opened} onClose={close} title={<span className='text-xl font-medium'>Upload Profile picture</span>}>
            
            </Modal>
        </div>
    )
}

export default Profile
