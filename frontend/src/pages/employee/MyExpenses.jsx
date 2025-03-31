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
  Edit as EditIcon,
  Delete as DeleteIcon,
  Visibility as VisibilityIcon,
} from '@mui/icons-material';

// Mock data for demo
const mockExpenses = [
  {
    id: 1,
    title: 'Business Trip to New York',
    category: 'Travel',
    amount: 2500,
    date: '2024-03-15',
    status: 'pending',
    description: 'Client meeting and project kickoff',
  },
  {
    id: 2,
    title: 'Office Supplies',
    category: 'Office Supplies',
    amount: 450,
    date: '2024-03-14',
    status: 'approved',
    description: 'Monthly office supplies replenishment',
  },
  {
    id: 3,
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

function MyExpenses() {
  const [expenses, setExpenses] = useState(mockExpenses);
  const [selectedExpense, setSelectedExpense] = useState(null);
  const [openDialog, setOpenDialog] = useState(false);
  const [editMode, setEditMode] = useState(false);
  const [formData, setFormData] = useState({
    title: '',
    category: '',
    amount: '',
    date: '',
    description: '',
  });

  const handleOpenDialog = (expense = null) => {
    if (expense) {
      setSelectedExpense(expense);
      setFormData(expense);
      setEditMode(true);
    } else {
      setSelectedExpense(null);
      setFormData({
        title: '',
        category: '',
        amount: '',
        date: '',
        description: '',
      });
      setEditMode(false);
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setSelectedExpense(null);
    setFormData({
      title: '',
      category: '',
      amount: '',
      date: '',
      description: '',
    });
    setEditMode(false);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = () => {
    if (editMode) {
      setExpenses(expenses.map(expense =>
        expense.id === selectedExpense.id
          ? { ...expense, ...formData }
          : expense
      ));
    } else {
      setExpenses([
        ...expenses,
        {
          id: Date.now(),
          ...formData,
          status: 'pending',
        },
      ]);
    }
    handleCloseDialog();
  };

  const handleDelete = (id) => {
    if (window.confirm('Are you sure you want to delete this expense?')) {
      setExpenses(expenses.filter(expense => expense.id !== id));
    }
  };

  const getStatusLabel = (status) => {
    return status.charAt(0).toUpperCase() + status.slice(1);
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
        <Button
          variant="contained"
          onClick={() => handleOpenDialog()}
        >
          New Expense
        </Button>
      </Box>

      <Grid container spacing={3}>
        <Grid item xs={12} md={4}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              Summary
            </Typography>
            <Box sx={{ mt: 2 }}>
              <Typography variant="body2" color="text.secondary">
                Total Expenses: ${expenses.reduce((sum, exp) => sum + exp.amount, 0).toLocaleString()}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Pending: ${expenses.filter(e => e.status === 'pending').reduce((sum, exp) => sum + exp.amount, 0).toLocaleString()}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Approved: ${expenses.filter(e => e.status === 'approved').reduce((sum, exp) => sum + exp.amount, 0).toLocaleString()}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Rejected: ${expenses.filter(e => e.status === 'rejected').reduce((sum, exp) => sum + exp.amount, 0).toLocaleString()}
              </Typography>
            </Box>
          </Paper>
        </Grid>

        <Grid item xs={12}>
          <Paper sx={{ p: 3 }}>
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
                          <EditIcon />
                        </IconButton>
                        <IconButton
                          size="small"
                          onClick={() => handleDelete(expense.id)}
                        >
                          <DeleteIcon />
                        </IconButton>
                        <IconButton
                          size="small"
                          onClick={() => {
                            setSelectedExpense(expense);
                            setOpenDialog(true);
                            setEditMode(false);
                          }}
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
          {editMode ? 'Edit Expense' : selectedExpense ? 'View Expense' : 'New Expense'}
        </DialogTitle>
        <DialogContent>
          <Grid container spacing={2} sx={{ mt: 1 }}>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Title"
                name="title"
                value={formData.title}
                onChange={handleChange}
                disabled={!editMode && selectedExpense}
              />
            </Grid>
            <Grid item xs={12} md={6}>
              <FormControl fullWidth>
                <InputLabel>Category</InputLabel>
                <Select
                  name="category"
                  value={formData.category}
                  label="Category"
                  onChange={handleChange}
                  disabled={!editMode && selectedExpense}
                >
                  {categories.map((category) => (
                    <MenuItem key={category} value={category}>
                      {category}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="Amount"
                name="amount"
                type="number"
                value={formData.amount}
                onChange={handleChange}
                disabled={!editMode && selectedExpense}
                InputProps={{
                  startAdornment: '$',
                }}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Date"
                name="date"
                type="date"
                value={formData.date}
                onChange={handleChange}
                disabled={!editMode && selectedExpense}
                InputLabelProps={{
                  shrink: true,
                }}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                multiline
                rows={3}
                label="Description"
                name="description"
                value={formData.description}
                onChange={handleChange}
                disabled={!editMode && selectedExpense}
              />
            </Grid>
          </Grid>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          {(editMode || !selectedExpense) && (
            <Button onClick={handleSubmit} variant="contained">
              {editMode ? 'Update' : 'Submit'}
            </Button>
          )}
        </DialogActions>
      </Dialog>
    </Container>
  );
}

export default MyExpenses; 