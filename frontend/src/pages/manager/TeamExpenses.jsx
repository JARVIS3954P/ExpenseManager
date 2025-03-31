import React, { useState } from 'react';
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
  Button,
  Chip,
  IconButton,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
} from '@mui/material';
import {
  Visibility as VisibilityIcon,
  CheckCircle as CheckCircleIcon,
  Cancel as CancelIcon,
} from '@mui/icons-material';

// Mock data for demo
const mockTeamExpenses = [
  {
    id: 1,
    employee: 'John Doe',
    title: 'Business Trip to New York',
    category: 'Travel',
    amount: 2500,
    date: '2024-03-15',
    status: 'pending',
    description: 'Client meeting and project kickoff',
  },
  {
    id: 2,
    employee: 'Jane Smith',
    title: 'Office Supplies',
    category: 'Office Supplies',
    amount: 450,
    date: '2024-03-14',
    status: 'approved',
    description: 'Monthly office supplies replenishment',
  },
  {
    id: 3,
    employee: 'Mike Johnson',
    title: 'Team Lunch',
    category: 'Meals',
    amount: 280,
    date: '2024-03-13',
    status: 'rejected',
    description: 'Team building activity',
  },
];

const categories = [
  'Travel',
  'Meals',
  'Office Supplies',
  'Equipment',
  'Training',
  'Other',
];

const statusColors = {
  pending: 'warning',
  approved: 'success',
  rejected: 'error',
};

function TeamExpenses() {
  const [expenses, setExpenses] = useState(mockTeamExpenses);
  const [selectedExpense, setSelectedExpense] = useState(null);
  const [openDialog, setOpenDialog] = useState(false);
  const [comment, setComment] = useState('');

  const handleOpenDialog = (expense) => {
    setSelectedExpense(expense);
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setSelectedExpense(null);
    setComment('');
  };

  const handleApprove = () => {
    setExpenses(expenses.map(expense =>
      expense.id === selectedExpense.id
        ? { ...expense, status: 'approved' }
        : expense
    ));
    handleCloseDialog();
  };

  const handleReject = () => {
    setExpenses(expenses.map(expense =>
      expense.id === selectedExpense.id
        ? { ...expense, status: 'rejected' }
        : expense
    ));
    handleCloseDialog();
  };

  const getStatusLabel = (status) => {
    return status.charAt(0).toUpperCase() + status.slice(1);
  };

  return (
    <Container maxWidth="lg">
      <Box sx={{ mb: 4 }}>
        <Typography variant="h5">Team Expenses</Typography>
        <Typography variant="body2" color="text.secondary">
          Manage and approve team expense submissions
        </Typography>
      </Box>

      <Grid container spacing={3}>
        <Grid item xs={12} md={4}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              Team Statistics
            </Typography>
            <Box sx={{ mt: 2 }}>
              <Typography variant="body2" color="text.secondary">
                Total Team Members: 8
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Pending Approvals: {expenses.filter(e => e.status === 'pending').length}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Monthly Budget: $50,000
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Spent This Month: $32,500
              </Typography>
            </Box>
          </Paper>
        </Grid>

        <Grid item xs={12}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              Expense Requests
            </Typography>
            <TableContainer>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>Employee</TableCell>
                    <TableCell>Title</TableCell>
                    <TableCell>Category</TableCell>
                    <TableCell align="right">Amount</TableCell>
                    <TableCell>Date</TableCell>
                    <TableCell>Status</TableCell>
                    <TableCell>Actions</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {expenses.map((expense) => (
                    <TableRow key={expense.id}>
                      <TableCell>{expense.employee}</TableCell>
                      <TableCell>{expense.title}</TableCell>
                      <TableCell>{expense.category}</TableCell>
                      <TableCell align="right">
                        ${expense.amount.toLocaleString()}
                      </TableCell>
                      <TableCell>{expense.date}</TableCell>
                      <TableCell>
                        <Chip
                          label={getStatusLabel(expense.status)}
                          color={statusColors[expense.status]}
                          size="small"
                        />
                      </TableCell>
                      <TableCell>
                        <IconButton
                          size="small"
                          onClick={() => handleOpenDialog(expense)}
                        >
                          <VisibilityIcon />
                        </IconButton>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          </Paper>
        </Grid>
      </Grid>

      <Dialog
        open={openDialog}
        onClose={handleCloseDialog}
        maxWidth="sm"
        fullWidth
      >
        <DialogTitle>
          Review Expense: {selectedExpense?.title}
        </DialogTitle>
        <DialogContent>
          <Grid container spacing={2} sx={{ mt: 1 }}>
            <Grid item xs={12}>
              <Typography variant="subtitle2" color="text.secondary">
                Employee
              </Typography>
              <Typography variant="body1">
                {selectedExpense?.employee}
              </Typography>
            </Grid>
            <Grid item xs={12} md={6}>
              <Typography variant="subtitle2" color="text.secondary">
                Category
              </Typography>
              <Typography variant="body1">
                {selectedExpense?.category}
              </Typography>
            </Grid>
            <Grid item xs={12} md={6}>
              <Typography variant="subtitle2" color="text.secondary">
                Amount
              </Typography>
              <Typography variant="body1">
                ${selectedExpense?.amount.toLocaleString()}
              </Typography>
            </Grid>
            <Grid item xs={12}>
              <Typography variant="subtitle2" color="text.secondary">
                Description
              </Typography>
              <Typography variant="body1">
                {selectedExpense?.description}
              </Typography>
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                multiline
                rows={3}
                label="Comments"
                value={comment}
                onChange={(e) => setComment(e.target.value)}
              />
            </Grid>
          </Grid>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          <Button
            onClick={handleReject}
            color="error"
            startIcon={<CancelIcon />}
          >
            Reject
          </Button>
          <Button
            onClick={handleApprove}
            color="success"
            startIcon={<CheckCircleIcon />}
            variant="contained"
          >
            Approve
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
}

export default TeamExpenses; 