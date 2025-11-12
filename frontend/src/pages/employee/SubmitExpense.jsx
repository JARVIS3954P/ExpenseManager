import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { submitExpense } from '../../store/slices/expenseSlice';
import {
  Container,
  Paper,
  Typography,
  Grid,
  Box,
  TextField,
  Button,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Alert,
  CircularProgress,
} from '@mui/material';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { CloudUpload as CloudUploadIcon } from '@mui/icons-material';

const categories = [
  'TRAVEL',
  'FOOD',
  'ACCOMMODATION',
  'OFFICE_SUPPLIES',
  'OTHER',
];

function SubmitExpense() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  
  // Get state from Redux store
  const { user } = useSelector((state) => state.auth);
  const { loading, error: reduxError } = useSelector((state) => state.expenses);

  // Local state for the form
  const [success, setSuccess] = useState('');
  const [formError, setFormError] = useState('');
  const [formData, setFormData] = useState({
    title: '',
    amount: '',
    category: '',
    date: new Date(),
    description: '',
    receipt: null,
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleDateChange = (date) => {
    setFormData(prev => ({ ...prev, date }));
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setFormData(prev => ({ ...prev, receipt: file }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setFormError('');
    setSuccess('');

    if (!formData.title || !formData.amount || !formData.category || !formData.date) {
      setFormError('Please fill in all required fields.');
      return;
    }
    if (!user || !user.id) {
      setFormError('Could not identify the current user. Please log in again.');
      return;
    }

    const expensePayload = {
      title: formData.title,
      amount: formData.amount,
      category: formData.category,
      expenseDate: new Date(formData.date).toISOString().split('T')[0],
      description: formData.description,
      attachment: formData.receipt,
      userId: user.id,
    };

    try {
      await dispatch(submitExpense(expensePayload)).unwrap();
      setSuccess('Expense submitted successfully! Redirecting...');
      
      setFormData({
        title: '', amount: '', category: '', date: new Date(), description: '', receipt: null,
      });
      
      setTimeout(() => navigate('/employee/expenses'), 2000);
    } catch (err) {
      setFormError(err.message || 'An unexpected error occurred.');
    }
  };

  return (
    <Container maxWidth="md">
      <Box sx={{ mb: 4 }}>
        <Typography variant="h5">Submit New Expense</Typography>
        <Typography variant="body2" color="text.secondary">
          Fill in the details of your expense
        </Typography>
      </Box>

      <Paper sx={{ p: 3 }}>
        <form onSubmit={handleSubmit}>
          <Grid container spacing={3}>
            {(formError || reduxError) && (
              <Grid item xs={12}>
                <Alert severity="error">{formError || reduxError.message}</Alert>
              </Grid>
            )}
            {success && (
              <Grid item xs={12}>
                <Alert severity="success">{success}</Alert>
              </Grid>
            )}

            <Grid item xs={12}>
              <TextField fullWidth required label="Expense Title" name="title" value={formData.title} onChange={handleChange} />
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField fullWidth required label="Amount" name="amount" type="number" value={formData.amount} onChange={handleChange} InputProps={{ startAdornment: '$' }} />
            </Grid>
            <Grid item xs={12} md={6}>
              <FormControl fullWidth required>
                <InputLabel>Category</InputLabel>
                <Select name="category" value={formData.category} label="Category" onChange={handleChange}>
                  {categories.map((category) => (
                    <MenuItem key={category} value={category}>{category.replace('_', ' ')}</MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={12} md={6}>
              <LocalizationProvider dateAdapter={AdapterDateFns}>
                <DatePicker label="Date" value={formData.date} onChange={handleDateChange} renderInput={(params) => <TextField {...params} fullWidth required />} />
              </LocalizationProvider>
            </Grid>
            <Grid item xs={12} md={6}>
              <Button fullWidth component="label" variant="outlined" startIcon={<CloudUploadIcon />} sx={{ height: '100%' }}>
                Upload Receipt
                <input type="file" hidden accept="image/*,.pdf" onChange={handleFileChange} />
              </Button>
              {formData.receipt && <Typography variant="body2" sx={{ mt: 1 }}>Selected: {formData.receipt.name}</Typography>}
            </Grid>
            <Grid item xs={12}>
              <TextField fullWidth multiline rows={4} label="Description (Optional)" name="description" value={formData.description} onChange={handleChange} />
            </Grid>
            <Grid item xs={12}>
              <Box sx={{ display: 'flex', justifyContent: 'flex-end' }}>
                <Button type="submit" variant="contained" disabled={loading} sx={{ minWidth: 150 }}>
                  {loading ? <CircularProgress size={24} color="inherit" /> : 'Submit Expense'}
                </Button>
              </Box>
            </Grid>
          </Grid>
        </form>
      </Paper>
    </Container>
  );
}

export default SubmitExpense;