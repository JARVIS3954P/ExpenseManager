import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { fetchSummaryByCategory } from '../../store/slices/analyticsSlice';
import {
  Container, Paper, Typography, Grid, Box, CircularProgress, Alert,
} from '@mui/material';
import {
  BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer,
} from 'recharts';

function Reports() {
  const dispatch = useDispatch();
  const { summaryByCategory, loading, error } = useSelector((state) => state.analytics);

  useEffect(() => {
    dispatch(fetchSummaryByCategory());
  }, [dispatch]);

  const renderChart = () => {
    if (loading) return <Box display="flex" justifyContent="center" p={5}><CircularProgress /></Box>;
    if (error) return <Alert severity="error">Could not load analytics data.</Alert>;

    return (
      <ResponsiveContainer width="100%" height={400}>
        <BarChart data={summaryByCategory}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="name" />
          <YAxis />
          <Tooltip formatter={(value) => `$${value.toLocaleString()}`} />
          <Legend />
          <Bar dataKey="value" fill="#8884d8" name="Total Amount" />
        </BarChart>
      </ResponsiveContainer>
    );
  };

  return (
    <Container maxWidth="lg">
      <Box sx={{ mb: 4 }}>
        <Typography variant="h5">Reports & Analytics</Typography>
        <Typography variant="body2" color="text.secondary">
          Generate and view system reports
        </Typography>
      </Box>

      {/* We will make the filter controls functional later. */}

      <Grid container spacing={3}>
        <Grid item xs={12}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              Total Expenses by Category
            </Typography>
            {renderChart()}
          </Paper>
        </Grid>
      </Grid>
    </Container>
  );
}

export default Reports;