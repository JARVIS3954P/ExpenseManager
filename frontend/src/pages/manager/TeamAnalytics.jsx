import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { fetchSummaryByCategory, fetchTeamSummary } from '../../store/slices/analyticsSlice';
import {
  Container, Paper, Typography, Grid, Box, CircularProgress, Alert
} from '@mui/material';
import {
  BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, PieChart, Pie, Cell,
} from 'recharts';

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#8884D8'];

function TeamAnalytics() {
  const dispatch = useDispatch();
  // We get data from two different fields in the analytics state
  const { summaryByCategory, teamSummary, loading, error } = useSelector((state) => state.analytics);

  useEffect(() => {
    // Fetch both sets of data when the component mounts
    dispatch(fetchSummaryByCategory());
    dispatch(fetchTeamSummary());
  }, [dispatch]);

  if (loading) {
    return <Box display="flex" justifyContent="center" p={5}><CircularProgress /></Box>;
  }
  if (error) {
    return <Alert severity="error">Could not load analytics data.</Alert>;
  }

  return (
    <Container maxWidth="lg">
      <Box sx={{ mb: 4 }}>
        <Typography variant="h5">Team Analytics</Typography>
        <Typography variant="body2" color="text.secondary">Monitor team performance and expense patterns</Typography>
      </Box>

      <Grid container spacing={3}>
        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>Expense Distribution by Category</Typography>
            <ResponsiveContainer width="100%" height={400}>
              <PieChart>
                <Pie data={summaryByCategory} dataKey="value" nameKey="name" cx="50%" cy="50%" outerRadius={150} label>
                  {summaryByCategory.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                  ))}
                </Pie>
                <Tooltip formatter={(value) => `$${value.toLocaleString()}`} />
                <Legend />
              </PieChart>
            </ResponsiveContainer>
          </Paper>
        </Grid>

        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>Total Expenses by Team Member</Typography>
            <ResponsiveContainer width="100%" height={400}>
              <BarChart data={teamSummary}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="name" />
                <YAxis />
                <Tooltip formatter={(value) => `$${value.toLocaleString()}`} />
                <Legend />
                <Bar dataKey="value" fill="#82ca9d" name="Total Amount" />
              </BarChart>
            </ResponsiveContainer>
          </Paper>
        </Grid>
      </Grid>
    </Container>
  );
}

export default TeamAnalytics;