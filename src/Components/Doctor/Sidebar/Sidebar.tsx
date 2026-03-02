import { Avatar, Text } from '@mantine/core'
import { IconCalendarCheck, IconHeartbeat, IconLayoutGrid, IconMoodHeart, IconStethoscope, IconUser, IconVaccine } from '@tabler/icons-react'
import React from 'react'
import { NavLink } from 'react-router-dom';
import {useSelector} from 'react-redux';

const links = [
    { name:"Dashboard", url:"/doctor/dashboard", icon: <IconLayoutGrid stroke={1.5}/> },
    { name:"Profile", url:"/doctor/profile", icon: <IconUser stroke={.5}/> },
    { name:"Patients", url:"/doctor/patients", icon: <IconMoodHeart stroke={1.5}/> },
    { name: "Appointments", url:"/doctor/appointments", icon: <IconCalendarCheck stroke={1.5}/>},
    {name: "Pharmacy", url:"/doctor/pharmacy", icon:<IconVaccine stroke={1.5}/>},
]
const Sidebar = () => {

    const user = useSelector((state:any)=>state.user);

  return (
    <div className='flex'>
        <div className='w-64'></div>
        <div className='bg-dark w-64 flex flex-col gap-7 items-center h-screen overflow-y-auto hide-scrollbar fixed'>
            <div className='text-primary-400 flex gap-1 items-center fixed z-[500] bg-dark py-3'>
                <IconHeartbeat size={40} stroke={2.5}/>
                <span className='font-heading text-3xl font-semibold'> Pulse </span>
            </div>
            <div className='flex flex-col gap-5 mt-20'>
                <div className='flex flex-col gap-1 items-center'>
                    <div className='p-1 bg-white rounded-full shadow-lg'>
                        <Avatar variant='filled' src="/avatar.png" alt="It's me" size="lg"/> 
                    </div>
                    <span className='font-medium text-light'>{user.name}</span>
                    <Text c="dimmed" size="xs" className='text-light'>{user.role}</Text>
                </div>

                <div className='flex flex-col gap-1'>
                {
                    links.map((link)=>{
                        return <NavLink 
                                to = {link.url} 
                                key={link.url} 
                                className={({isActive})=>
                                    `flex items-center gap-3 w-full font-medium px-4 py-5 rounded-lg
                                    ${isActive
                                        ? "bg-primary-400 text-dark"
                                        : " text-light hover:bg-gray-100 hover:text-dark"}`
                                    }
                                >
                                    {link.icon}
                                    <span>{link.name}</span>
                        </NavLink>
                    })
                }
                </div>
            </div>
        </div>
    </div>
  )
}

export default Sidebar
