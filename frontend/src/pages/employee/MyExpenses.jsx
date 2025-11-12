import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { fetchExpenses } from '../../store/slices/expenseSlice';
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
  CircularProgress,
  Alert,
} from '@mui/material';
import {
  Edit as EditIcon,
  Delete as DeleteIcon,
  Visibility as VisibilityIcon,
} from '@mui/icons-material';

const categories = ['Travel', 'Meals', 'Office Supplies', 'Equipment', 'Training', 'Other'];
const statusColors = {
  PENDING_MANAGER_APPROVAL: 'warning',
  PENDING_ADMIN_APPROVAL: 'info',
  APPROVED: 'success',
  REJECTED: 'error',
};

function MyExpenses() {
  const dispatch = useDispatch();
  const { items: expenses, loading, error } = useSelector((state) => state.expenses);

  // States for the dialog/modal
  const [selectedExpense, setSelectedExpense] = useState(null);
  const [openDialog, setOpenDialog] = useState(false);
  const [isEditMode, setIsEditMode] = useState(false);

  // Fetch expenses when the component mounts
  useEffect(() => {
    dispatch(fetchExpenses());
  }, [dispatch]);

  const handleOpenDialog = (expense = null, edit = false) => {
    setSelectedExpense(expense);
    setIsEditMode(edit);
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setSelectedExpense(null);
  };

  const getStatusLabel = (status) => {
    return status.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, c => c.toUpperCase());
  };
  
  const renderContent = () => {
    if (loading) {
      return (
        <Box display="flex" justifyContent="center" alignItems="center" p={5}>
          <CircularProgress />
        </Box>
      );
    }
    if (error) {
        return <Alert severity="error">Failed to load expenses: {error.message || 'Server error'}</Alert>;
    }
    if (expenses.length === 0) {
        return <Typography p={3}>No expenses found. Try submitting one!</Typography>
    }
    return (
      <TableContainer>
        <Table>
          <TableHead>
            <TableRow>
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
                <TableCell>{expense.title}</TableCell>
                <TableCell>{expense.category}</TableCell>
                <TableCell align="right">${expense.amount.toLocaleString()}</TableCell>
                <TableCell>{expense.expenseDate}</TableCell>
                <TableCell>
                  <Chip
                    label={getStatusLabel(expense.status)}
                    color={statusColors[expense.status] || 'default'}
                    size="small"
                  />
                </TableCell>
                <TableCell>
                  <IconButton size="small" onClick={() => handleOpenDialog(expense, false)}>
                    <VisibilityIcon />
                  </IconButton>
                  {/* Only allow editing if the expense is pending */}
                  {expense.status.startsWith('PENDING') && (
                     <IconButton size="small" onClick={() => handleOpenDialog(expense, true)}>
                        <EditIcon />
                     </IconButton>
                  )}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    );
  };

  return (
    <Container maxWidth="lg">
      <Box sx={{ mb: 4, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Box>
          <Typography variant="h5">My Expenses</Typography>
          <Typography variant="body2" color="text.secondary">
            View and manage your expense submissions
          </Typography>
        </Box>
        <Button variant="contained" /* onClick should navigate to SubmitExpense page */ >
          New Expense
        </Button>
      </Box>

      <Paper sx={{ p: 3 }}>
        {renderContent()}
      </Paper>

      {/* The Dialog for viewing/editing remains mostly the same, but it's not functional yet */}
      <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="sm" fullWidth>
        <DialogTitle>
          {isEditMode ? 'Edit Expense' : 'View Expense'}
        </DialogTitle>
        <DialogContent>
          <Grid container spacing={2} sx={{ mt: 1 }}>
            <Grid item xs={12}>
              <TextField fullWidth label="Title" defaultValue={selectedExpense?.title} disabled={!isEditMode} />
            </Grid>
            {/* Add other fields here for viewing/editing */}
          </Grid>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          {isEditMode && <Button variant="contained">Save</Button>}
        </DialogActions>
      </Dialog>
    </Container>
  );
}

export default MyExpenses;