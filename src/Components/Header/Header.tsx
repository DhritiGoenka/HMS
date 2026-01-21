import React from 'react'
import ProfileMenu from './ProfileMenu'
import { ActionIcon } from '@mantine/core';
import { IconBellRinging, IconLayoutSidebarLeftCollapseFilled } from '@tabler/icons-react';

const Header = () => {
  return (
    <div className='bg-cyan-100 w-full h-16 flex justify-between px-5 items-center'>
      <ActionIcon variant="transparent" aria-label="Settings" size = "lg">
        <IconLayoutSidebarLeftCollapseFilled style={{ width: '90%', height: '90%' }} stroke={1.5} />
    </ActionIcon>
    <div className='flex gap-5 items-center'>
        <ActionIcon variant="transparent" aria-label="Settings" size = "md">
            <IconBellRinging style={{ width: '90%', height: '90%' }} stroke={2} />
        </ActionIcon>  
        <ProfileMenu/>
    </div>
    </div>
  )
}

export default Header
