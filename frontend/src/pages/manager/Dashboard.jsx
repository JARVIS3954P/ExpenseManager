import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { fetchDashboardSummary } from '../../store/slices/analyticsSlice';
import {
  Box, Container, Grid, Paper, Typography, List, ListItem, ListItemIcon, 
  ListItemText, Divider, CircularProgress, Alert
} from '@mui/material';
import {
  Group as GroupIcon,
  Receipt as ReceiptIcon,
  CheckCircle as CheckCircleIcon,
  Analytics as AnalyticsIcon,
} from '@mui/icons-material';

// Reusable StatCard component for consistency
const StatCard = ({ title, value, icon }) => (
  <Paper sx={{ p: 2, display: 'flex', alignItems: 'center', height: '100%' }}>
    {icon}
    <Box>
      <Typography variant="h6">{value}</Typography>
      <Typography variant="body2" color="text.secondary">{title}</Typography>
    </Box>
  </Paper>
);

function ManagerDashboard() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { dashboardSummary: stats, loading, error } = useSelector((state) => state.analytics);

  useEffect(() => {
    // This one thunk provides data for all dashboards
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
        <Typography variant="h4" gutterBottom>Department Overview</Typography>

        <Grid container spacing={3}>
          {/* Statistics Cards using live data */}
          <Grid item xs={12} sm={6}>
            <StatCard 
              title="Team Members" 
              value={stats.teamSize || 0} 
              icon={<GroupIcon sx={{ fontSize: 40, color: 'primary.main', mr: 2 }} />} 
            />
          </Grid>

          <Grid item xs={12} sm={6}>
            <StatCard 
              title="Pending Approvals" 
              value={stats.pendingApprovals || 0} 
              icon={<ReceiptIcon sx={{ fontSize: 40, color: 'warning.main', mr: 2 }} />} 
            />
          </Grid>

          {/* Quick Actions Panel */}
          <Grid item xs={12}>
            <Paper sx={{ p: 2 }}>
              <Typography variant="h6" gutterBottom>Quick Actions</Typography>
              <List>
                <ListItem button onClick={() => navigate('/manager/approvals')}>
                  <ListItemIcon><CheckCircleIcon /></ListItemIcon>
                  <ListItemText primary="Review Pending Approvals" />
                </ListItem>
                <Divider />
                <ListItem button onClick={() => navigate('/manager/expenses')}>
                  <ListItemIcon><ReceiptIcon /></ListItemIcon>
                  <ListItemText primary="View All Team Expenses" />
                </ListItem>
                <Divider />
                <ListItem button onClick={() => navigate('/manager/analytics')}>
                  <ListItemIcon><AnalyticsIcon /></ListItemIcon>
                  <ListItemText primary="View Team Analytics" />
                </ListItem>
              </List>
            </Paper>
          </Grid>
        </Grid>
      </Box>
    </Container>
  );
}

export default ManagerDashboard;