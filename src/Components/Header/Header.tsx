import React from 'react'
import ProfileMenu from './ProfileMenu'
import { ActionIcon, Button } from '@mantine/core';
import { IconBellRinging, IconLayoutSidebarLeftCollapseFilled } from '@tabler/icons-react';
import { Link } from 'react-router-dom';
import {useDispatch, useSelector} from 'react-redux';
import { removeJwt } from '../../Slices/JwtSlice';
import { removeUser } from '../../Slices/UserSlice';

const Header = () => {

  const jwt = useSelector((state:any)=>state.jwt);
  const dispatch = useDispatch();

  const handleLogout=()=>{
    dispatch(removeJwt());
    dispatch(removeUser());
  }

  return (
    <div className='bg-light shadow w-full h-16 flex justify-between px-5 items-center'>
      <ActionIcon variant="transparent" aria-label="Settings" size = "lg">
        <IconLayoutSidebarLeftCollapseFilled style={{ width: '90%', height: '90%' }} stroke={1.5} />
      </ActionIcon>
      <div className='flex gap-5 items-center'>
        {jwt? 
          <Link to="dashboard">
            <Button color='red' onClick={handleLogout}>Logout</Button>
          </Link>:
          <Link to="login">
            <Button>Login</Button>
          </Link>
        }
        {jwt && 
          <>
            <ActionIcon variant="transparent" aria-label="Settings" size = "md">
              <IconBellRinging style={{ width: '90%', height: '90%' }} stroke={2} />
            </ActionIcon>  
            <ProfileMenu/>
          </>
        }
      </div>
    </div>
  )
}

export default Header
