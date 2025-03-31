import React, { useState } from 'react';
import {
  Container,
  Paper,
  Typography,
  Grid,
  Box,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TextField,
} from '@mui/material';
import {
  Download as DownloadIcon,
  Refresh as RefreshIcon,
} from '@mui/icons-material';
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
const mockExpenseData = [
  { month: 'Jan', amount: 45000, approved: 38000, pending: 7000 },
  { month: 'Feb', amount: 52000, approved: 45000, pending: 7000 },
  { month: 'Mar', amount: 48000, approved: 42000, pending: 6000 },
  { month: 'Apr', amount: 55000, approved: 48000, pending: 7000 },
  { month: 'May', amount: 51000, approved: 45000, pending: 6000 },
  { month: 'Jun', amount: 58000, approved: 52000, pending: 6000 },
];

const mockDepartmentData = [
  { department: 'Engineering', total: 250000, approved: 220000, pending: 30000 },
  { department: 'Marketing', total: 180000, approved: 150000, pending: 30000 },
  { department: 'Sales', total: 220000, approved: 200000, pending: 20000 },
  { department: 'HR', total: 90000, approved: 85000, pending: 5000 },
  { department: 'Finance', total: 120000, approved: 110000, pending: 10000 },
];

function Reports() {
  const [reportType, setReportType] = useState('expense');
  const [dateRange, setDateRange] = useState('6months');
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');

  const handleGenerateReport = () => {
    // Simulate API call to generate report
    console.log('Generating report...');
  };

  const handleDownloadReport = () => {
    // Simulate API call to download report
    console.log('Downloading report...');
  };

  return (
    <Container maxWidth="lg">
      <Box sx={{ mb: 4 }}>
        <Typography variant="h5">Reports & Analytics</Typography>
        <Typography variant="body2" color="text.secondary">
          Generate and view system reports
        </Typography>
      </Box>

      <Paper sx={{ p: 3, mb: 3 }}>
        <Grid container spacing={3} alignItems="center">
          <Grid item xs={12} md={3}>
            <FormControl fullWidth>
              <InputLabel>Report Type</InputLabel>
              <Select
                value={reportType}
                label="Report Type"
                onChange={(e) => setReportType(e.target.value)}
              >
                <MenuItem value="expense">Expense Report</MenuItem>
                <MenuItem value="department">Department Report</MenuItem>
                <MenuItem value="user">User Activity Report</MenuItem>
                <MenuItem value="audit">Audit Log Report</MenuItem>
              </Select>
            </FormControl>
          </Grid>
          <Grid item xs={12} md={3}>
            <FormControl fullWidth>
              <InputLabel>Date Range</InputLabel>
              <Select
                value={dateRange}
                label="Date Range"
                onChange={(e) => setDateRange(e.target.value)}
              >
                <MenuItem value="7days">Last 7 Days</MenuItem>
                <MenuItem value="30days">Last 30 Days</MenuItem>
                <MenuItem value="3months">Last 3 Months</MenuItem>
                <MenuItem value="6months">Last 6 Months</MenuItem>
                <MenuItem value="custom">Custom Range</MenuItem>
              </Select>
            </FormControl>
          </Grid>
          {dateRange === 'custom' && (
            <>
              <Grid item xs={12} md={2}>
                <TextField
                  fullWidth
                  type="date"
                  label="Start Date"
                  value={startDate}
                  onChange={(e) => setStartDate(e.target.value)}
                  InputLabelProps={{ shrink: true }}
                />
              </Grid>
              <Grid item xs={12} md={2}>
                <TextField
                  fullWidth
                  type="date"
                  label="End Date"
                  value={endDate}
                  onChange={(e) => setEndDate(e.target.value)}
                  InputLabelProps={{ shrink: true }}
                />
              </Grid>
            </>
          )}
          <Grid item xs={12} md={2}>
            <Button
              fullWidth
              variant="contained"
              startIcon={<RefreshIcon />}
              onClick={handleGenerateReport}
            >
              Generate
            </Button>
          </Grid>
          <Grid item xs={12} md={2}>
            <Button
              fullWidth
              variant="outlined"
              startIcon={<DownloadIcon />}
              onClick={handleDownloadReport}
            >
              Download
            </Button>
          </Grid>
        </Grid>
      </Paper>

      <Grid container spacing={3}>
        <Grid item xs={12}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              Monthly Expense Trends
            </Typography>
            <Box sx={{ height: 400 }}>
              <ResponsiveContainer width="100%" height="100%">
                <LineChart data={mockExpenseData}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="month" />
                  <YAxis />
                  <Tooltip />
                  <Legend />
                  <Line
                    type="monotone"
                    dataKey="amount"
                    stroke="#8884d8"
                    name="Total"
                  />
                  <Line
                    type="monotone"
                    dataKey="approved"
                    stroke="#82ca9d"
                    name="Approved"
                  />
                  <Line
                    type="monotone"
                    dataKey="pending"
                    stroke="#ffc658"
                    name="Pending"
                  />
                </LineChart>
              </ResponsiveContainer>
            </Box>
          </Paper>
        </Grid>

        <Grid item xs={12}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              Department-wise Expenses
            </Typography>
            <Box sx={{ height: 400 }}>
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={mockDepartmentData}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="department" />
                  <YAxis />
                  <Tooltip />
                  <Legend />
                  <Bar dataKey="total" fill="#8884d8" name="Total" />
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
              Detailed Report
            </Typography>
            <TableContainer>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>Department</TableCell>
                    <TableCell align="right">Total Expenses</TableCell>
                    <TableCell align="right">Approved</TableCell>
                    <TableCell align="right">Pending</TableCell>
                    <TableCell align="right">Approval Rate</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {mockDepartmentData.map((row) => (
                    <TableRow key={row.department}>
                      <TableCell>{row.department}</TableCell>
                      <TableCell align="right">${row.total.toLocaleString()}</TableCell>
                      <TableCell align="right">${row.approved.toLocaleString()}</TableCell>
                      <TableCell align="right">${row.pending.toLocaleString()}</TableCell>
                      <TableCell align="right">
                        {((row.approved / row.total) * 100).toFixed(1)}%
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

export default Reports; 