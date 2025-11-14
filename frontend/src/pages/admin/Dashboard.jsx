import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { fetchDashboardSummary } from '../../store/slices/analyticsSlice';
import {
  Box, Container, Grid, Paper, Typography, List, ListItem, ListItemIcon,
  ListItemText, Divider, CircularProgress, Alert
} from '@mui/material';
import {
  People as PeopleIcon,
  Receipt as ReceiptIcon,
  Assessment as AssessmentIcon,
} from '@mui/icons-material';

// Reusable StatCard component
const StatCard = ({ title, value, icon }) => (
  <Paper sx={{ p: 2, display: 'flex', alignItems: 'center', height: '100%' }}>
    {icon}
    <Box>
      <Typography variant="h6">{value}</Typography>
      <Typography variant="body2" color="text.secondary">{title}</Typography>
    </Box>
  </Paper>
);

function AdminDashboard() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { dashboardSummary: stats, loading, error } = useSelector((state) => state.analytics);

  useEffect(() => {
    dispatch(fetchDashboardSummary());
  }, [dispatch]);

  // Conditional rendering for loading and error states
  if (loading) {
    return <Box display="flex" justifyContent="center" alignItems="center" height="50vh"><CircularProgress /></Box>;
  }
  if (error) {
    return <Alert severity="error">Could not load dashboard data: {error.message}</Alert>;
  }

  return (
    <Container maxWidth="lg">
      <Box sx={{ mt: 4, mb: 4 }}>
        <Typography variant="h4" gutterBottom>System Overview</Typography>

        <Grid container spacing={3}>
          {/* Statistics Cards using live data */}
          <Grid item xs={12} sm={6} md={4}>
            <StatCard 
              title="Total Users" 
              value={stats.totalUsers || 0} 
              icon={<PeopleIcon sx={{ fontSize: 40, color: 'primary.main', mr: 2 }} />} 
            />
          </Grid>

          <Grid item xs={12} sm={6} md={4}>
            <StatCard 
              title="Pending Approvals" 
              value={stats.pendingApprovals || 0} 
              icon={<ReceiptIcon sx={{ fontSize: 40, color: 'warning.main', mr: 2 }} />} 
            />
          </Grid>

          <Grid item xs={12} sm={6} md={4}>
             <StatCard 
              title="Total Expense Value" 
              value={`$${(stats.totalExpensesValue || 0).toLocaleString()}`} 
              icon={<AssessmentIcon sx={{ fontSize: 40, color: 'success.main', mr: 2 }} />} 
            />
          </Grid>

          {/* Quick Actions Panel */}
          <Grid item xs={12}>
            <Paper sx={{ p: 2 }}>
              <Typography variant="h6" gutterBottom>Quick Actions</Typography>
              <List>
                <ListItem button onClick={() => navigate('/admin/users')}>
                  <ListItemIcon><PeopleIcon /></ListItemIcon>
                  <ListItemText primary="Manage Users" />
                </ListItem>
                <Divider />
                <ListItem button onClick={() => navigate('/admin/reports')}>
                  <ListItemIcon><AssessmentIcon /></ListItemIcon>
                  <ListItemText primary="Generate Reports" />
                </ListItem>
              </List>
            </Paper>
          </Grid>
        </Grid>
      </Box>
    </Container>
  );
}

export default AdminDashboard;