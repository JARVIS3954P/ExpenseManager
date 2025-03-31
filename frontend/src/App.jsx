import React, { useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import { LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { useSelector, useDispatch } from 'react-redux';
import { CircularProgress, Box } from '@mui/material';
import MainLayout from './layouts/MainLayout';
import theme from './theme';
import { checkAuth } from './store/slices/authSlice';

// Admin Pages
import AdminDashboard from './pages/admin/Dashboard';
import UserManagement from './pages/admin/UserManagement';
import SystemSettings from './pages/admin/SystemSettings';
import Reports from './pages/admin/Reports';

// Manager Pages
import ManagerDashboard from './pages/manager/Dashboard';
import TeamExpenses from './pages/manager/TeamExpenses';
import Approvals from './pages/manager/Approvals';
import TeamAnalytics from './pages/manager/TeamAnalytics';

// Employee Pages
import EmployeeDashboard from './pages/employee/Dashboard';
import MyExpenses from './pages/employee/MyExpenses';
import SubmitExpense from './pages/employee/SubmitExpense';
import TeamOverview from './pages/employee/TeamOverview';

// Auth Pages
import Login from './pages/Login';

// Protected Route Component
const ProtectedRoute = ({ children, allowedRoles }) => {
  const { user, isAuthenticated, loading } = useSelector((state) => state.auth);
  
  if (loading) {
    return (
      <Box
        display="flex"
        justifyContent="center"
        alignItems="center"
        minHeight="100vh"
      >
        <CircularProgress />
      </Box>
    );
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (!allowedRoles.includes(user?.role)) {
    return <Navigate to={`/${user?.role.toLowerCase()}/dashboard`} replace />;
  }

  return children;
};

function App() {
  const dispatch = useDispatch();
  const { loading } = useSelector((state) => state.auth);

  useEffect(() => {
    dispatch(checkAuth());
  }, [dispatch]);

  if (loading) {
    return (
      <Box
        display="flex"
        justifyContent="center"
        alignItems="center"
        minHeight="100vh"
      >
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Router>
      <ThemeProvider theme={theme}>
        <LocalizationProvider dateAdapter={AdapterDateFns}>
          <CssBaseline />
          <Routes>
            {/* Public Routes */}
            <Route path="/login" element={<Login />} />

            {/* Admin Routes */}
            <Route
              path="/admin/*"
              element={
                <ProtectedRoute allowedRoles={['ADMIN']}>
                  <MainLayout />
                </ProtectedRoute>
              }
            >
              <Route index element={<Navigate to="dashboard" replace />} />
              <Route path="dashboard" element={<AdminDashboard />} />
              <Route path="users" element={<UserManagement />} />
              <Route path="settings" element={<SystemSettings />} />
              <Route path="reports" element={<Reports />} />
            </Route>

            {/* Manager Routes */}
            <Route
              path="/manager/*"
              element={
                <ProtectedRoute allowedRoles={['MANAGER']}>
                  <MainLayout />
                </ProtectedRoute>
              }
            >
              <Route index element={<Navigate to="dashboard" replace />} />
              <Route path="dashboard" element={<ManagerDashboard />} />
              <Route path="expenses" element={<TeamExpenses />} />
              <Route path="approvals" element={<Approvals />} />
              <Route path="analytics" element={<TeamAnalytics />} />
            </Route>

            {/* Employee Routes */}
            <Route
              path="/employee/*"
              element={
                <ProtectedRoute allowedRoles={['EMPLOYEE']}>
                  <MainLayout />
                </ProtectedRoute>
              }
            >
              <Route index element={<Navigate to="dashboard" replace />} />
              <Route path="dashboard" element={<EmployeeDashboard />} />
              <Route path="expenses" element={<MyExpenses />} />
              <Route path="submit" element={<SubmitExpense />} />
              <Route path="team" element={<TeamOverview />} />
            </Route>

            {/* Default Route */}
            <Route path="/" element={<Navigate to="/login" replace />} />
            <Route path="*" element={<Navigate to="/login" replace />} />
          </Routes>
        </LocalizationProvider>
      </ThemeProvider>
    </Router>
  );
}

export default App;
