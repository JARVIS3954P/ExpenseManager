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
  Receipt as ReceiptIcon,
  CheckCircle as CheckCircleIcon,
  Pending as PendingIcon,
  TrendingUp as TrendingUpIcon,
  Add as AddIcon,
} from '@mui/icons-material';

function EmployeeDashboard() {
  const stats = {
    monthlyBudget: 2000,
    spentAmount: 1200,
    pendingExpenses: 3,
    approvedExpenses: 8,
    budgetUtilization: (1200 / 2000) * 100,
  };

  const recentExpenses = [
    { category: 'Travel', amount: 250, status: 'approved', date: '2024-03-20' },
    { category: 'Food', amount: 150, status: 'pending', date: '2024-03-19' },
    { category: 'Office Supplies', amount: 75, status: 'approved', date: '2024-03-18' },
  ];

  return (
    <Container maxWidth="lg">
      <Box sx={{ mt: 4, mb: 4 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 3 }}>
          <Typography variant="h4">My Dashboard</Typography>
          <Button
            variant="contained"
            startIcon={<AddIcon />}
            color="primary"
          >
            Submit Expense
          </Button>
        </Box>

        <Grid container spacing={3}>
          {/* Statistics Cards */}
          <Grid item xs={12} md={3}>
            <Paper sx={{ p: 2, display: 'flex', alignItems: 'center' }}>
              <ReceiptIcon sx={{ fontSize: 40, color: 'primary.main', mr: 2 }} />
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
              <PendingIcon sx={{ fontSize: 40, color: 'warning.main', mr: 2 }} />
              <Box>
                <Typography variant="h6">{stats.pendingExpenses}</Typography>
                <Typography variant="body2" color="text.secondary">
                  Pending Expenses
                </Typography>
              </Box>
            </Paper>
          </Grid>

          <Grid item xs={12} md={3}>
            <Paper sx={{ p: 2, display: 'flex', alignItems: 'center' }}>
              <CheckCircleIcon sx={{ fontSize: 40, color: 'success.main', mr: 2 }} />
              <Box>
                <Typography variant="h6">{stats.approvedExpenses}</Typography>
                <Typography variant="body2" color="text.secondary">
                  Approved Expenses
                </Typography>
              </Box>
            </Paper>
          </Grid>

          <Grid item xs={12} md={3}>
            <Paper sx={{ p: 2, display: 'flex', alignItems: 'center' }}>
              <TrendingUpIcon sx={{ fontSize: 40, color: 'error.main', mr: 2 }} />
              <Box>
                <Typography variant="h6">{stats.budgetUtilization.toFixed(1)}%</Typography>
                <Typography variant="body2" color="text.secondary">
                  Budget Used
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

          {/* Recent Expenses */}
          <Grid item xs={12} md={6}>
            <Paper sx={{ p: 2 }}>
              <Typography variant="h6" gutterBottom>
                Recent Expenses
              </Typography>
              <List>
                {recentExpenses.map((expense, index) => (
                  <React.Fragment key={index}>
                    <ListItem>
                      <ListItemIcon>
                        {expense.status === 'approved' ? (
                          <CheckCircleIcon color="success" />
                        ) : (
                          <PendingIcon color="warning" />
                        )}
                      </ListItemIcon>
                      <ListItemText
                        primary={expense.category}
                        secondary={`$${expense.amount} • ${expense.date}`}
                      />
                      <Typography
                        variant="body2"
                        color={expense.status === 'approved' ? 'success.main' : 'warning.main'}
                      >
                        {expense.status.charAt(0).toUpperCase() + expense.status.slice(1)}
                      </Typography>
                    </ListItem>
                    {index < recentExpenses.length - 1 && <Divider />}
                  </React.Fragment>
                ))}
              </List>
            </Paper>
          </Grid>

          {/* Quick Actions */}
          <Grid item xs={12} md={6}>
            <Paper sx={{ p: 2 }}>
              <Typography variant="h6" gutterBottom>
                Quick Actions
              </Typography>
              <List>
                <ListItem button>
                  <ListItemIcon>
                    <AddIcon />
                  </ListItemIcon>
                  <ListItemText primary="Submit New Expense" />
                </ListItem>
                <Divider />
                <ListItem button>
                  <ListItemIcon>
                    <ReceiptIcon />
                  </ListItemIcon>
                  <ListItemText primary="View All Expenses" />
                </ListItem>
                <Divider />
                <ListItem button>
                  <ListItemIcon>
                    <PendingIcon />
                  </ListItemIcon>
                  <ListItemText primary="Check Pending Status" />
                </ListItem>
                <Divider />
                <ListItem button>
                  <ListItemIcon>
                    <TrendingUpIcon />
                  </ListItemIcon>
                  <ListItemText primary="View Expense History" />
                </ListItem>
              </List>
            </Paper>
          </Grid>
        </Grid>
      </Box>
    </Container>
  );
}

export default EmployeeDashboard; 