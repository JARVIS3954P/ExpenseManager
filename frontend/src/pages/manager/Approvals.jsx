import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { fetchPendingApprovals, updateExpenseStatus } from '../../store/slices/expenseSlice';
import {
  Container, Paper, Typography, Table, TableBody, TableCell, TableContainer,
  TableHead, TableRow, Button, Box, Dialog, DialogTitle, DialogContent,
  DialogActions, TextField, Grid, CircularProgress, Alert, IconButton,
} from '@mui/material';
import { CheckCircle as CheckCircleIcon, Cancel as CancelIcon, Visibility as VisibilityIcon } from '@mui/icons-material';

function Approvals() {
  const dispatch = useDispatch();
  const { items: pendingExpenses, loading, error } = useSelector((state) => state.expenses);

  const [selectedExpense, setSelectedExpense] = useState(null);
  const [isReviewOpen, setReviewOpen] = useState(false);
  const [remarks, setRemarks] = useState('');
  const [actionType, setActionType] = useState(null); // 'APPROVE' or 'REJECT'

  // Fetch pending approvals when the component mounts
  useEffect(() => {
    dispatch(fetchPendingApprovals());
  }, [dispatch]);

  const handleOpenReview = (expense, type) => {
    setSelectedExpense(expense);
    setActionType(type);
    setReviewOpen(true);
  };

  const handleCloseReview = () => {
    setReviewOpen(false);
    setSelectedExpense(null);
    setRemarks('');
    setActionType(null);
  };

  const handleSubmitReview = async () => {
    if (!selectedExpense) return;

    const payload = {
      expenseId: selectedExpense.id,
      status: actionType === 'APPROVE' ? 'APPROVED' : 'REJECTED',
      remarks: remarks,
    };

    try {
      await dispatch(updateExpenseStatus(payload)).unwrap();
      // The slice now removes the item from the state, so we don't need to re-fetch manually.
    } catch (err) {
      // Error will be handled by the slice's rejected state
      console.error("Failed to update status:", err);
    } finally {
      handleCloseReview();
    }
  };

  const renderContent = () => {
    if (loading) {
      return <Box display="flex" justifyContent="center" p={5}><CircularProgress /></Box>;
    }
    if (error) {
      return <Alert severity="error">Failed to load approvals: {error.message || 'Server Error'}</Alert>;
    }
    if (pendingExpenses.length === 0) {
      return <Typography align="center" p={3}>No pending approvals.</Typography>;
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
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {pendingExpenses.map((expense) => (
              <TableRow key={expense.id}>
                <TableCell>{expense.userId}</TableCell>
                <TableCell>{expense.title}</TableCell>
                <TableCell>{expense.category}</TableCell>
                <TableCell align="right">${expense.amount.toLocaleString()}</TableCell>
                <TableCell>{expense.expenseDate}</TableCell>
                <TableCell>
                  <IconButton size="small" color="success" onClick={() => handleOpenReview(expense, 'APPROVE')}>
                    <CheckCircleIcon />
                  </IconButton>
                  <IconButton size="small" color="error" onClick={() => handleOpenReview(expense, 'REJECTE')}>
                    <CancelIcon />
                  </IconButton>
                  <IconButton size="small" /* onClick={showReceipt} */>
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
        <Typography variant="h5">Pending Approvals</Typography>
        <Typography variant="body2" color="text.secondary">Review and action expense submissions from your team.</Typography>
      </Box>

      <Paper>
        {renderContent()}
      </Paper>

      <Dialog open={isReviewOpen} onClose={handleCloseReview} maxWidth="sm" fullWidth>
        <DialogTitle>{actionType === 'APPROVE' ? 'Approve' : 'Reject'} Expense: {selectedExpense?.title}</DialogTitle>
        <DialogContent>
          <Grid container spacing={2} sx={{ mt: 1 }}>
            <Grid item xs={12}>
              <Typography variant="subtitle2">Employee ID:</Typography>
              <Typography variant="body1">{selectedExpense?.userId}</Typography>
            </Grid>
            <Grid item xs={6}>
              <Typography variant="subtitle2">Amount:</Typography>
              <Typography variant="body1">${selectedExpense?.amount.toLocaleString()}</Typography>
            </Grid>
            <Grid item xs={6}>
              <Typography variant="subtitle2">Category:</Typography>
              <Typography variant="body1">{selectedExpense?.category}</Typography>
            </Grid>
            <Grid item xs={12}>
              <TextField fullWidth multiline rows={3} label="Remarks (Optional)" value={remarks} onChange={(e) => setRemarks(e.target.value)} />
            </Grid>
          </Grid>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseReview}>Cancel</Button>
          <Button onClick={handleSubmitReview} color={actionType === 'APPROVE' ? 'success' : 'error'} variant="contained">
            Confirm {actionType === 'APPROVE' ? 'Approval' : 'Rejection'}
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
}

export default Approvals;