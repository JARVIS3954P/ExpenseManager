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
  PieChart,
  Pie,
  Cell,
} from 'recharts';

// Mock data for demo
const monthlyData = [
  { month: 'Jan', expenses: 45000, budget: 50000 },
  { month: 'Feb', expenses: 52000, budget: 50000 },
  { month: 'Mar', expenses: 48000, budget: 50000 },
  { month: 'Apr', expenses: 55000, budget: 50000 },
  { month: 'May', expenses: 51000, budget: 50000 },
  { month: 'Jun', expenses: 58000, budget: 50000 },
];

const categoryData = [
  { name: 'Travel', value: 250000 },
  { name: 'Meals', value: 180000 },
  { name: 'Office Supplies', value: 120000 },
  { name: 'Equipment', value: 90000 },
  { name: 'Training', value: 60000 },
];

const employeeData = [
  { name: 'John Doe', total: 45000, approved: 42000, pending: 3000 },
  { name: 'Jane Smith', total: 38000, approved: 35000, pending: 3000 },
  { name: 'Mike Johnson', total: 42000, approved: 40000, pending: 2000 },
  { name: 'Sarah Wilson', total: 35000, approved: 33000, pending: 2000 },
  { name: 'Tom Brown', total: 40000, approved: 38000, pending: 2000 },
];

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#8884D8'];

function TeamAnalytics() {
  return (
    <Container maxWidth="lg">
      <Box sx={{ mb: 4 }}>
        <Typography variant="h5">Team Analytics</Typography>
        <Typography variant="body2" color="text.secondary">
          Monitor team performance and expense patterns
        </Typography>
      </Box>

      <Grid container spacing={3}>
        <Grid item xs={12}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              Monthly Expenses vs Budget
            </Typography>
            <Box sx={{ height: 400 }}>
              <ResponsiveContainer width="100%" height="100%">
                <LineChart data={monthlyData}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="month" />
                  <YAxis />
                  <Tooltip />
                  <Legend />
                  <Line
                    type="monotone"
                    dataKey="expenses"
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

        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              Expense Distribution by Category
            </Typography>
            <Box sx={{ height: 400 }}>
              <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                  <Pie
                    data={categoryData}
                    cx="50%"
                    cy="50%"
                    labelLine={false}
                    outerRadius={150}
                    fill="#8884d8"
                    dataKey="value"
                    label={({ name, percent }) =>
                      `${name} ${(percent * 100).toFixed(0)}%`
                    }
                  >
                    {categoryData.map((entry, index) => (
                      <Cell
                        key={`cell-${index}`}
                        fill={COLORS[index % COLORS.length]}
                      />
                    ))}
                  </Pie>
                  <Tooltip />
                  <Legend />
                </PieChart>
              </ResponsiveContainer>
            </Box>
          </Paper>
        </Grid>

        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              Employee Expense Overview
            </Typography>
            <Box sx={{ height: 400 }}>
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={employeeData}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="name" />
                  <YAxis />
                  <Tooltip />
                  <Legend />
                  <Bar dataKey="approved" fill="#82ca9d" name="Approved" />
                  <Bar dataKey="pending" fill="#ffc658" name="Pending" />
                </BarChart>
              </ResponsiveContainer>
            </Box>
          </Paper>
        </Grid>

        <Grid item xs={12}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              Detailed Employee Statistics
            </Typography>
            <TableContainer>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>Employee</TableCell>
                    <TableCell align="right">Total Expenses</TableCell>
                    <TableCell align="right">Approved</TableCell>
                    <TableCell align="right">Pending</TableCell>
                    <TableCell align="right">Approval Rate</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {employeeData.map((employee) => (
                    <TableRow key={employee.name}>
                      <TableCell>{employee.name}</TableCell>
                      <TableCell align="right">
                        ${employee.total.toLocaleString()}
                      </TableCell>
                      <TableCell align="right">
                        ${employee.approved.toLocaleString()}
                      </TableCell>
                      <TableCell align="right">
                        ${employee.pending.toLocaleString()}
                      </TableCell>
                      <TableCell align="right">
                        {((employee.approved / employee.total) * 100).toFixed(1)}%
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

export default TeamAnalytics; 