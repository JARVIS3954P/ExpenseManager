import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { fetchDashboardSummary } from '../../store/slices/analyticsSlice';
import {
  Box, Container, Grid, Paper, Typography, List, ListItem, ListItemText,
  ListItemIcon, Divider, CircularProgress, Alert
} from '@mui/material';
import {
  Receipt as ReceiptIcon,
  CheckCircle as CheckCircleIcon,
  PendingActions as PendingIcon,
  Add as AddIcon,
} from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';

// A small helper component for the stat cards
const StatCard = ({ title, value, icon }) => (
  <Paper sx={{ p: 2, display: 'flex', alignItems: 'center', height: '100%' }}>
    {icon}
    <Box>
      <Typography variant="h6">{value}</Typography>
      <Typography variant="body2" color="text.secondary">{title}</Typography>
    </Box>
  </Paper>
);

function EmployeeDashboard() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { dashboardSummary: stats, loading, error } = useSelector((state) => state.analytics);

  useEffect(() => {
    dispatch(fetchDashboardSummary());
  }, [dispatch]);

  if (loading) {
    return <Box display="flex" justifyContent="center" alignItems="center" height="50vh"><CircularProgress /></Box>;
  }

  if (error) {
    return <Alert severity="error">Could not load dashboard data.</Alert>;
  }

  return (
    <Container maxWidth="lg">
      <Box sx={{ mt: 4, mb: 4 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 3 }}>
          <Typography variant="h4">My Dashboard</Typography>
        </Box>

        <Grid container spacing={3}>
          {/* Statistics Cards */}
          <Grid item xs={12} sm={6} md={4}>
            <StatCard 
              title="Spent This Month" 
              value={`$${(stats.spentThisMonth || 0).toLocaleString()}`} 
              icon={<ReceiptIcon sx={{ fontSize: 40, color: 'primary.main', mr: 2 }} />} 
            />
          </Grid>

          <Grid item xs={12} sm={6} md={4}>
            <StatCard 
              title="Pending Expenses" 
              value={stats.myPendingExpenses || 0} 
              icon={<PendingIcon sx={{ fontSize: 40, color: 'warning.main', mr: 2 }} />} 
            />
          </Grid>

          <Grid item xs={12} sm={6} md={4}>
            <StatCard 
              title="Approved Expenses" 
              value={stats.myApprovedExpenses || 0} 
              icon={<CheckCircleIcon sx={{ fontSize: 40, color: 'success.main', mr: 2 }} />} 
            />
          </Grid>

          {/* Quick Actions */}
          <Grid item xs={12} md={6}>
            <Paper sx={{ p: 2 }}>
              <Typography variant="h6" gutterBottom>Quick Actions</Typography>
              <List>
                <ListItem button onClick={() => navigate('/employee/submit')}>
                  <ListItemIcon><AddIcon /></ListItemIcon>
                  <ListItemText primary="Submit New Expense" />
                </ListItem>
                <Divider />
                <ListItem button onClick={() => navigate('/employee/expenses')}>
                  <ListItemIcon><ReceiptIcon /></ListItemIcon>
                  <ListItemText primary="View All My Expenses" />
                </ListItem>
              </List>
            </Paper>
          </Grid>

          {/*recent expenses list*/}
          
        </Grid>
      </Box>
    </Container>
  );
}

export default EmployeeDashboard;