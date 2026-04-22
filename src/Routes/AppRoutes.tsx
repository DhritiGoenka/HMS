import React from 'react'
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Random from '../Random';
import AdminDashboard from '../Layout/AdminDashboard';
import LoginPage from '../Pages/LoginPage';
import RegisterPage from '../Pages/RegisterPage';
import PublicRoute from './PublicRoute';
import ProtectedRoute from './ProtectedRoute';
import PatientDashboard from '../Layout/PatientDashboard';
import PatientProfilePage from '../Pages/Patient/PatientProfilePage';
import DoctorDashboard from '../Layout/DoctorDashboard';
import DoctorProfilePage from '../Pages/Doctor/DoctorProfilePage';
import PatientAppointmentPage from '../Pages/Patient/PatientAppointmentPage';
import DoctorAppointmentPage from '../Pages/Doctor/DoctorAppointmentPage';
import DoctorAppointmentDetailsPage from '../Pages/Doctor/DoctorAppointmentDetailsPage';
import AdminMedicinePage from '../Pages/Admin/AdminMedicinePage';
import { Navigate } from "react-router-dom";

const AppRoutes = () => {
  return (
    <BrowserRouter>
        <Routes>
          <Route path = "/login" element={<PublicRoute><LoginPage/></PublicRoute>} />
          <Route path="/" element={<Navigate to="/login" />} />
          <Route path = "/register" element = {<PublicRoute><RegisterPage/></PublicRoute>} />
          <Route path="/admin" element={<ProtectedRoute><AdminDashboard/></ProtectedRoute>}>
            <Route path="dashboard" element={<Random/>}></Route>
            <Route path="patients" element={<Random/>}></Route>
            <Route path="doctors" element={<Random/>}></Route>
            <Route path="medicine" element={<AdminMedicinePage/>}></Route>
            <Route path="inventory" element={<Random/>}></Route>
            <Route path="sales" element={<Random/>}></Route>
          </Route>

          <Route path="/patient" element={<ProtectedRoute><PatientDashboard/></ProtectedRoute>}>
            <Route path="dashboard" element={<Random/>}></Route>
            <Route path="profile" element={<PatientProfilePage/>}></Route>
            <Route path="appointments" element={<PatientAppointmentPage/>}></Route>
            <Route path="book" element={<Random/>}></Route>
          </Route>

          <Route path="/doctor" element={<ProtectedRoute><DoctorDashboard/></ProtectedRoute>}>
            <Route path="dashboard" element={<Random/>}></Route>
            <Route path="profile" element={<DoctorProfilePage/>}></Route>
            <Route path="appointments" element={<DoctorAppointmentPage/>}></Route>
            <Route path="appointments/:id" element={<DoctorAppointmentDetailsPage/>}></Route>
            <Route path="patients" element={<Random/>}></Route>
            <Route path="pharmacy" element={<Random/>}></Route>
          </Route>

          <Route path="*" element={<div>Page not found</div>} />
        </Routes> 
    </BrowserRouter>
  )
}

export default AppRoutes
