import React from 'react';
import {
  Box,
  Container,
  Grid,
  Paper,
  Typography,
  List,
  ListItem,
  ListItemText,
  ListItemIcon,
  Divider,
  Button,
  LinearProgress,
} from '@mui/material';
import {
  Group as GroupIcon,
  Receipt as ReceiptIcon,
  CheckCircle as CheckCircleIcon,
  Warning as WarningIcon,
  TrendingUp as TrendingUpIcon,
} from '@mui/icons-material';

function ManagerDashboard() {
  const stats = {
    teamSize: 12,
    pendingApprovals: 8,
    monthlyBudget: 25000,
    spentAmount: 18000,
    budgetUtilization: (18000 / 25000) * 100,
  };

  const pendingExpenses = [
    { employee: 'John Smith', amount: 250, category: 'Travel', date: '2024-03-20' },
    { employee: 'Sarah Johnson', amount: 150, category: 'Food', date: '2024-03-19' },
    { employee: 'Mike Wilson', amount: 500, category: 'Office Supplies', date: '2024-03-18' },
  ];

  return (
    <Container maxWidth="lg">
      <Box sx={{ mt: 4, mb: 4 }}>
        <Typography variant="h4" gutterBottom>
          Department Overview
        </Typography>

        <Grid container spacing={3}>
          {/* Statistics Cards */}
          <Grid item xs={12} md={3}>
            <Paper sx={{ p: 2, display: 'flex', alignItems: 'center' }}>
              <GroupIcon sx={{ fontSize: 40, color: 'primary.main', mr: 2 }} />
              <Box>
                <Typography variant="h6">{stats.teamSize}</Typography>
                <Typography variant="body2" color="text.secondary">
                  Team Members
                </Typography>
              </Box>
            </Paper>
          </Grid>

          <Grid item xs={12} md={3}>
            <Paper sx={{ p: 2, display: 'flex', alignItems: 'center' }}>
              <ReceiptIcon sx={{ fontSize: 40, color: 'warning.main', mr: 2 }} />
              <Box>
                <Typography variant="h6">{stats.pendingApprovals}</Typography>
                <Typography variant="body2" color="text.secondary">
                  Pending Approvals
                </Typography>
              </Box>
            </Paper>
          </Grid>

          <Grid item xs={12} md={3}>
            <Paper sx={{ p: 2, display: 'flex', alignItems: 'center' }}>
              <TrendingUpIcon sx={{ fontSize: 40, color: 'success.main', mr: 2 }} />
              <Box>
                <Typography variant="h6">${stats.spentAmount.toLocaleString()}</Typography>
                <Typography variant="body2" color="text.secondary">
                  Spent This Month
                </Typography>
              </Box>
            </Paper>
          </Grid>

          <Grid item xs={12} md={3}>
            <Paper sx={{ p: 2, display: 'flex', alignItems: 'center' }}>
              <WarningIcon sx={{ fontSize: 40, color: 'error.main', mr: 2 }} />
              <Box>
                <Typography variant="h6">{stats.budgetUtilization.toFixed(1)}%</Typography>
                <Typography variant="body2" color="text.secondary">
                  Budget Utilization
                </Typography>
              </Box>
            </Paper>
          </Grid>

          {/* Budget Progress */}
          <Grid item xs={12}>
            <Paper sx={{ p: 2 }}>
              <Typography variant="h6" gutterBottom>
                Monthly Budget Progress
              </Typography>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                <Typography variant="body2" sx={{ mr: 2 }}>
                  ${stats.spentAmount.toLocaleString()} / ${stats.monthlyBudget.toLocaleString()}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  ({stats.budgetUtilization.toFixed(1)}% used)
                </Typography>
              </Box>
              <LinearProgress
                variant="determinate"
                value={stats.budgetUtilization}
                sx={{ height: 10, borderRadius: 5 }}
              />
            </Paper>
          </Grid>

          {/* Pending Approvals */}
          <Grid item xs={12} md={6}>
            <Paper sx={{ p: 2 }}>
              <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
                <Typography variant="h6">Pending Approvals</Typography>
                <Button variant="contained" color="primary" size="small">
                  View All
                </Button>
              </Box>
              <List>
                {pendingExpenses.map((expense, index) => (
                  <React.Fragment key={index}>
                    <ListItem>
                      <ListItemIcon>
                        <ReceiptIcon />
                      </ListItemIcon>
                      <ListItemText
                        primary={`${expense.employee} - ${expense.category}`}
                        secondary={`$${expense.amount} • ${expense.date}`}
                      />
                      <Button variant="outlined" color="primary" size="small">
                        Review
                      </Button>
                    </ListItem>
                    {index < pendingExpenses.length - 1 && <Divider />}
                  </React.Fragment>
                ))}
              </List>
            </Paper>
          </Grid>

          {/* Team Performance */}
          <Grid item xs={12} md={6}>
            <Paper sx={{ p: 2 }}>
              <Typography variant="h6" gutterBottom>
                Team Performance
              </Typography>
              <List>
                <ListItem>
                  <ListItemIcon>
                    <CheckCircleIcon color="success" />
                  </ListItemIcon>
                  <ListItemText
                    primary="Expense Submission Rate"
                    secondary="95% of team members submitted expenses on time"
                  />
                </ListItem>
                <Divider />
                <ListItem>
                  <ListItemIcon>
                    <WarningIcon color="warning" />
                  </ListItemIcon>
                  <ListItemText
                    primary="Policy Compliance"
                    secondary="2 policy violations this month"
                  />
                </ListItem>
                <Divider />
                <ListItem>
                  <ListItemIcon>
                    <TrendingUpIcon color="primary" />
                  </ListItemIcon>
                  <ListItemText
                    primary="Budget Efficiency"
                    secondary="15% better than last month"
                  />
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