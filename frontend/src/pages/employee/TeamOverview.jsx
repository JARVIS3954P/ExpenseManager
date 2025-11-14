import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { fetchTeamSummary } from '../../store/slices/analyticsSlice';
import {
  Container, Paper, Typography, Grid, Box, CircularProgress, Alert, Table,
  TableBody, TableCell, TableContainer, TableHead, TableRow
} from '@mui/material';
import {
  BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer,
} from 'recharts';

function TeamOverview() {
  const dispatch = useDispatch();
  const { teamSummary, loading, error } = useSelector((state) => state.analytics);

  useEffect(() => {
    dispatch(fetchTeamSummary());
  }, [dispatch]);

  const renderContent = () => {
    if (loading) {
      return <Box display="flex" justifyContent="center" p={5}><CircularProgress /></Box>;
    }
    if (error) {
      return <Alert severity="error">Could not load team analytics data.</Alert>;
    }
    if (!teamSummary || teamSummary.length === 0) {
      return <Typography align="center" p={3}>No team expense data to display.</Typography>
    }
    return (
      <Grid container spacing={3}>
        <Grid item xs={12}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>Total Expenses by Team Member</Typography>
            <ResponsiveContainer width="100%" height={400}>
              <BarChart data={teamSummary}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="name" />
                <YAxis />
                <Tooltip formatter={(value) => `$${value.toLocaleString()}`} />
                <Legend />
                <Bar dataKey="value" fill="#82ca9d" name="Total Amount Spent" />
              </BarChart>
            </ResponsiveContainer>
          </Paper>
        </Grid>
        <Grid item xs={12}>
          <Paper sx={{ p: 3 }}>
             <Typography variant="h6" gutterBottom>Team Expense Data</Typography>
             <TableContainer>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Team Member</TableCell>
                            <TableCell align="right">Total Spent</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {teamSummary.map((member) => (
                            <TableRow key={member.name}>
                                <TableCell>{member.name}</TableCell>
                                <TableCell align="right">${member.value.toLocaleString()}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
             </TableContainer>
          </Paper>
        </Grid>
      </Grid>
    );
  };

  return (
    <Container maxWidth="lg">
      <Box sx={{ mb: 4 }}>
        <Typography variant="h5">Team Overview</Typography>
        <Typography variant="body2" color="text.secondary">
          A high-level view of your team's expense patterns.
        </Typography>
      </Box>
      {renderContent()}
    </Container>
  );
}

export default TeamOverview;