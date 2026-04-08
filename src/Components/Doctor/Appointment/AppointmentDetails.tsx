import { Badge, Breadcrumbs, Card, Divider, Group, Tabs, Text, Title } from "@mantine/core";
import { useState } from "react";
import { Link, useParams } from "react-router-dom";
import { getAppointmentWithNamesById } from "../../../Api/AppointmentApi";
import { useEffect } from "react";
import { formatDateWithTime } from "../../../Utility/DateUtility";
import { IconClipboardHeart, IconVaccine, IconStethoscope } from "@tabler/icons-react";
import AppointmentReport from "./AppointmentReport";
import Prescriptions from "./Prescriptions";
import { errorNotification } from "../../../Utility/NotificationUtil";

const AppointmentDetails = () =>{
    const {id} = useParams();
    const [appointment,setAppointment] = useState<any>({});
    useEffect(()=>{
        getAppointmentWithNamesById(id).then((data)=>{
            setAppointment(data);
        }).catch((error)=>{
            errorNotification("Error fetching appointment details");
        });
    },[id])

    return(
        <div>
            <Breadcrumbs mb='md'>
                <Link className="text-primary-400 hover:underline" to="/doctor/dashboard">Dashboard</Link>
                <Link className="text-primary-400 hover:underline" to="/doctor/appointments">Appointments</Link>
                <Text className="text-primary-400">Details</Text>
            </Breadcrumbs>

            <Card shadow="sm" padding="lg" radius="md" withBorder>
                <Group justify="space-between" mb="sm">
                    <Title order={2}>{appointment.patientName}</Title>
                    <Badge color={appointment.status==="CANCELLED"?'red':'green'} variant='light'>
                        {appointment.status}
                    </Badge>
                </Group>
                <div className="grid grid-cols-2 gap-5 mb-2">
                    <Text><strong>Email: </strong>{appointment.patientEmail}</Text>
                </div>

                <div className="grid grid-cols-2 gap-5">
                    <Text><strong>Reason: </strong>{appointment.reason}</Text>
                    <Text><strong>Appointment Time: </strong>{formatDateWithTime(appointment.appointmentTime)}</Text>
                </div>

                {appointment.notes && (
                    <Text mt="sm" color="dimmed" size="sm">
                        <strong>Notes: </strong> {appointment.notes}
                    </Text>
                )}
            </Card>

            <Tabs variant="pills" defaultValue="gallery" my="md">
                <Tabs.List>
                    <Tabs.Tab value="medical" leftSection={<IconStethoscope size={20}/>}>
                        Medical History
                    </Tabs.Tab>
                    <Tabs.Tab value="prescriptions" leftSection={<IconVaccine size={20}/>}>
                        Prescriptions
                    </Tabs.Tab>
                    <Tabs.Tab value="report" leftSection={<IconClipboardHeart size={20}/>}>
                        Report
                    </Tabs.Tab>
                </Tabs.List>
                <Divider my="md"/>
                <Tabs.Panel value="medical">
                    Medical History content goes here.
                </Tabs.Panel>

                <Tabs.Panel value="prescriptions">
                    <Prescriptions appointment={appointment}/>
                </Tabs.Panel>

                <Tabs.Panel value="report">
                    <AppointmentReport appointment = {appointment}/>
                </Tabs.Panel>
            </Tabs>
        </div>
    )
}

export default AppointmentDetails;