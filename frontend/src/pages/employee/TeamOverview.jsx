import React from 'react';
import {
  Container,
  Paper,
  Typography,
  Grid,
  Box,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Chip,
} from '@mui/material';
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
  BarChart,
  Bar,
} from 'recharts';

// Mock data for demo
const teamData = {
  members: [
    { name: 'John Doe', role: 'Team Lead', expenses: 25000, status: 'active' },
    { name: 'Jane Smith', role: 'Developer', expenses: 18000, status: 'active' },
    { name: 'Mike Johnson', role: 'Developer', expenses: 22000, status: 'active' },
    { name: 'Sarah Wilson', role: 'Designer', expenses: 15000, status: 'active' },
    { name: 'Tom Brown', role: 'Developer', expenses: 20000, status: 'active' },
  ],
  monthlyExpenses: [
    { month: 'Jan', amount: 45000, budget: 50000 },
    { month: 'Feb', amount: 52000, budget: 50000 },
    { month: 'Mar', amount: 48000, budget: 50000 },
    { month: 'Apr', amount: 55000, budget: 50000 },
    { month: 'May', amount: 51000, budget: 50000 },
    { month: 'Jun', amount: 58000, budget: 50000 },
  ],
  categoryBreakdown: [
    { category: 'Travel', amount: 250000 },
    { category: 'Meals', amount: 180000 },
    { category: 'Office Supplies', amount: 120000 },
    { category: 'Equipment', amount: 90000 },
    { category: 'Training', amount: 60000 },
  ],
};

function TeamOverview() {
  const getStatusColor = (status) => {
    return status === 'active' ? 'success' : 'error';
  };

  return (
    <Container maxWidth="lg">
      <Box sx={{ mb: 4 }}>
        <Typography variant="h5">Team Overview</Typography>
        <Typography variant="body2" color="text.secondary">
          View your team's performance and expense patterns
        </Typography>
      </Box>

      <Grid container spacing={3}>
        {/* Team Statistics */}
        <Grid item xs={12} md={4}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              Team Statistics
            </Typography>
            <Box sx={{ mt: 2 }}>
              <Typography variant="body2" color="text.secondary">
                Total Team Members: {teamData.members.length}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Total Team Expenses: ${teamData.members.reduce((sum, member) => sum + member.expenses, 0).toLocaleString()}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Average Expense per Member: ${(teamData.members.reduce((sum, member) => sum + member.expenses, 0) / teamData.members.length).toLocaleString()}
              </Typography>
            </Box>
          </Paper>
        </Grid>

        {/* Monthly Expenses Chart */}
        <Grid item xs={12}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              Monthly Expenses vs Budget
            </Typography>
            <Box sx={{ height: 400 }}>
              <ResponsiveContainer width="100%" height="100%">
                <LineChart data={teamData.monthlyExpenses}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="month" />
                  <YAxis />
                  <Tooltip />
                  <Legend />
                  <Line
                    type="monotone"
                    dataKey="amount"
                    stroke="#8884d8"
                    name="Expenses"
                  />
                  <Line
                    type="monotone"
                    dataKey="budget"
                    stroke="#82ca9d"
                    name="Budget"
                    strokeDasharray="5 5"
                  />
                </LineChart>
              </ResponsiveContainer>
            </Box>
          </Paper>
        </Grid>

        {/* Category Breakdown */}
        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              Expense Distribution by Category
            </Typography>
            <Box sx={{ height: 400 }}>
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={teamData.categoryBreakdown}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="category" />
                  <YAxis />
                  <Tooltip />
                  <Legend />
                  <Bar dataKey="amount" fill="#8884d8" name="Amount" />
                </BarChart>
              </ResponsiveContainer>
            </Box>
          </Paper>
        </Grid>

        {/* Team Members Table */}
        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              Team Members
            </Typography>
            <TableContainer>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>Name</TableCell>
                    <TableCell>Role</TableCell>
                    <TableCell align="right">Expenses</TableCell>
                    <TableCell>Status</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {teamData.members.map((member) => (
                    <TableRow key={member.name}>
                      <TableCell>{member.name}</TableCell>
                      <TableCell>{member.role}</TableCell>
                      <TableCell align="right">
                        ${member.expenses.toLocaleString()}
                      </TableCell>
                      <TableCell>
                        <Chip
                          label={member.status.charAt(0).toUpperCase() + member.status.slice(1)}
                          color={getStatusColor(member.status)}
                          size="small"
                        />
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          </Paper>
        </Grid>
      </Grid>
    </Container>
  );
}

export default TeamOverview; 