import React from 'react'
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Random from '../Random';
import AdminDashboard from '../Pages/AdminDashboard';
import LoginPage from '../Pages/LoginPage';
import RegisterPage from '../Pages/RegisterPage';
import PublicRoute from './PublicRoute';
import ProtectedRoute from './ProtectedRoute';

const AppRoutes = () => {
  return (
    <BrowserRouter>
        <Routes>
          <Route path = "/login" element={<PublicRoute><LoginPage/></PublicRoute>} />
          <Route path = "/register" element = {<PublicRoute><RegisterPage/></PublicRoute>} />
          <Route path="/" element={<ProtectedRoute><AdminDashboard/></ProtectedRoute>}>
            <Route path="/dashboard" element={<Random/>}></Route>
            <Route path="/pharmacy" element={<Random/>}></Route>
            <Route path="/patients" element={<Random/>}></Route>
            <Route path="/doctors" element={<Random/>}></Route>
          </Route>
        </Routes> 
    </BrowserRouter>
  )
}

export default AppRoutes
