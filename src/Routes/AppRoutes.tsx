import React from 'react'
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Random from '../Random';
import AdminDashboard from '../Pages/AdminDashboard';

const AppRoutes = () => {
  return (
    <BrowserRouter>
        <Routes>
            <Route path="/" element={<AdminDashboard/>} />
            <Route path = "/dashboard" element={<AdminDashboard/>} />
        </Routes> 
    </BrowserRouter>
  )
}

export default AppRoutes
