import React from 'react';
import {
  Box,
  Grid,
  Paper,
  Typography,
  Card,
  CardContent,
} from '@mui/material';
import {
  TrendingUp as TrendingUpIcon,
  AccountBalance as AccountBalanceIcon,
  Receipt as ReceiptIcon,
  Warning as WarningIcon,
} from '@mui/icons-material';

function Dashboard() {
  const stats = [
    {
      title: 'Total Expenses',
      value: '$12,345',
      icon: <AccountBalanceIcon sx={{ fontSize: 40, color: 'primary.main' }} />,
    },
    {
      title: 'Pending Approvals',
      value: '5',
      icon: <ReceiptIcon sx={{ fontSize: 40, color: 'warning.main' }} />,
    },
    {
      title: 'Monthly Budget',
      value: '$15,000',
      icon: <TrendingUpIcon sx={{ fontSize: 40, color: 'success.main' }} />,
    },
    {
      title: 'Over Budget Items',
      value: '2',
      icon: <WarningIcon sx={{ fontSize: 40, color: 'error.main' }} />,
    },
  ];

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Dashboard
      </Typography>
      <Grid container spacing={3}>
        {stats.map((stat) => (
          <Grid item xs={12} sm={6} md={3} key={stat.title}>
            <Card>
              <CardContent>
                <Box
                  sx={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'space-between',
                  }}
                >
                  <Box>
                    <Typography color="textSecondary" gutterBottom>
                      {stat.title}
                    </Typography>
                    <Typography variant="h5">{stat.value}</Typography>
                  </Box>
                  {stat.icon}
                </Box>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
      <Grid container spacing={3} sx={{ mt: 2 }}>
        <Grid item xs={12} md={8}>
          <Paper sx={{ p: 2 }}>
            <Typography variant="h6" gutterBottom>
              Recent Expenses
            </Typography>
            <Typography color="textSecondary">
              No recent expenses to display
            </Typography>
          </Paper>
        </Grid>
        <Grid item xs={12} md={4}>
          <Paper sx={{ p: 2 }}>
            <Typography variant="h6" gutterBottom>
              Budget Overview
            </Typography>
            <Typography color="textSecondary">
              No budget data available
            </Typography>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
}

export default Dashboard; 