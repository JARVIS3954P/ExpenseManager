import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { fetchTeamExpenses } from '../../store/slices/expenseSlice';
import {
  Container, Paper, Typography, Box, Table, TableBody, TableCell, TableContainer,
  TableHead, TableRow, Chip, IconButton, Dialog, DialogTitle, DialogContent,
  DialogActions, Button, Grid, CircularProgress, Alert, Link
} from '@mui/material';
import { Visibility as VisibilityIcon } from '@mui/icons-material';

const statusColors = {
  PENDING_MANAGER_APPROVAL: 'warning',
  PENDING_ADMIN_APPROVAL: 'info',
  APPROVED: 'success',
  REJECTED: 'error',
};

const API_BASE_URL = 'http://localhost:8080/api';

function TeamExpenses() {
  const dispatch = useDispatch();
  const { items: expenses, loading, error } = useSelector((state) => state.expenses);

  // State for the "View Details" dialog
  const [selectedExpense, setSelectedExpense] = useState(null);
  const [isViewOpen, setViewOpen] = useState(false);

  useEffect(() => {
    dispatch(fetchTeamExpenses());
  }, [dispatch]);

  const handleOpenView = (expense) => {
    setSelectedExpense(expense);
    setViewOpen(true);
  };

  const handleCloseView = () => {
    setViewOpen(false);
    setSelectedExpense(null);
  };

  const getStatusLabel = (status) => {
    return status.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, c => c.toUpperCase());
  };

  const renderContent = () => {
    if (loading) {
      return <Box display="flex" justifyContent="center" p={5}><CircularProgress /></Box>;
    }
    if (error) {
      return <Alert severity="error">Failed to load team expenses: {error.message || 'Server Error'}</Alert>;
    }
    if (expenses.length === 0) {
      return <Typography align="center" p={3}>No expenses have been submitted by your team yet.</Typography>;
    }
    return (
      <TableContainer>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Employee ID</TableCell>
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
                <TableCell>{expense.userId}</TableCell>
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
                  <IconButton size="small" onClick={() => handleOpenView(expense)}>
                    <VisibilityIcon />
                  </IconButton>
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
      <Box sx={{ mb: 4 }}>
        <Typography variant="h5">Team Expenses</Typography>
        <Typography variant="body2" color="text.secondary">
          A complete overview of all expenses submitted by your team members.
        </Typography>
      </Box>

      <Paper>
        {renderContent()}
      </Paper>

      {/* Dialog for viewing expense details */}
      <Dialog open={isViewOpen} onClose={handleCloseView} maxWidth="sm" fullWidth>
        <DialogTitle>Expense Details</DialogTitle>
        <DialogContent dividers>
          {selectedExpense && (
            <Grid container spacing={2} sx={{ mt: 1 }}>
              <Grid item xs={12}><Typography variant="subtitle2" color="text.secondary">Title</Typography><Typography variant="h6">{selectedExpense.title}</Typography></Grid>
              <Grid item xs={6}><Typography variant="subtitle2" color="text.secondary">Amount</Typography><Typography variant="body1">${selectedExpense.amount.toLocaleString()}</Typography></Grid>
              <Grid item xs={6}><Typography variant="subtitle2" color="text.secondary">Category</Typography><Typography variant="body1">{selectedExpense.category}</Typography></Grid>
              <Grid item xs={6}><Typography variant="subtitle2" color="text.secondary">Date of Expense</Typography><Typography variant="body1">{selectedExpense.expenseDate}</Typography></Grid>
              <Grid item xs={6}><Typography variant="subtitle2" color="text.secondary">Status</Typography><Chip label={getStatusLabel(selectedExpense.status)} color={statusColors[selectedExpense.status] || 'default'} /></Grid>
              {selectedExpense.attachmentUrl && (
                <Grid item xs={12}>
                  <Button variant="outlined" component={Link} href={`${API_BASE_URL}/expenses/${selectedExpense.id}/attachment`} target="_blank" rel="noopener noreferrer">
                    Download Attached Receipt
                  </Button>
                </Grid>
              )}
            </Grid>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseView}>Close</Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
}

export default TeamExpenses;