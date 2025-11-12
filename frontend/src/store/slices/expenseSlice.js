import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import apiClient from '../../api/axios';

// Thunk to fetch all expenses for the logged-in user
export const fetchExpenses = createAsyncThunk(
  'expenses/fetchExpenses',
  async (_, { rejectWithValue }) => {
    try {
      const response = await apiClient.get('/expenses');
      return response.data;
    } catch (error) {
      return rejectWithValue(error.response?.data || { message: 'Failed to fetch expenses' });
    }
  }
);

// Thunk to fetch expenses pending approval for the current manager/admin
export const fetchPendingApprovals = createAsyncThunk(
  'expenses/fetchPendingApprovals',
  async (_, { rejectWithValue }) => {
    try {
      const response = await apiClient.get('/expenses/for-approval');
      return response.data;
    } catch (error) {
      return rejectWithValue(error.response?.data);
    }
  }
);

// Thunk to submit a new expense
export const submitExpense = createAsyncThunk(
  'expenses/submitExpense',
  async (expenseData, { rejectWithValue }) => {
    try {
      const formData = new FormData();
      // Append all key-value pairs from expenseData to formData
      for (const key in expenseData) {
        if (expenseData.hasOwnProperty(key) && expenseData[key] !== null) {
          formData.append(key, expenseData[key]);
        }
      }

      const response = await apiClient.post('/expenses', formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      });
      return response.data;
    } catch (error) {
      return rejectWithValue(error.response?.data || { message: 'Submission failed' });
    }
  }
);

// Thunk to update an expense's status (approve/reject)
export const updateExpenseStatus = createAsyncThunk(
  'expenses/updateExpenseStatus',
  async ({ expenseId, status, rejectionReason = '', remarks = '' }, { rejectWithValue }) => {
    try {
      const payload = { status, rejectionReason, remarks };
      const response = await apiClient.put(`/expenses/${expenseId}/status`, payload);
      return response.data;
    } catch (error) {
      return rejectWithValue(error.response?.data);
    }
  }
);

const initialState = {
  items: [],
  loading: false,
  error: null,
};

const expenseSlice = createSlice({
  name: 'expenses',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      // Reducers for fetchExpenses
      .addCase(fetchExpenses.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchExpenses.fulfilled, (state, action) => {
        state.loading = false;
        state.items = action.payload;
      })
      .addCase(fetchExpenses.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
      // Reducers for fetchPendingApprovals
      .addCase(fetchPendingApprovals.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchPendingApprovals.fulfilled, (state, action) => {
        state.loading = false;
        state.items = action.payload;
      })
      .addCase(fetchPendingApprovals.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
      // Reducers for submitExpense
      .addCase(submitExpense.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(submitExpense.fulfilled, (state, action) => {
        state.loading = false;
        state.items.push(action.payload);
      })
      .addCase(submitExpense.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
      // Reducers for updateExpenseStatus
      .addCase(updateExpenseStatus.pending, (state) => {
        // We might not want a full-page loader for this, but can set a specific flag if needed
        state.error = null;
      })
      .addCase(updateExpenseStatus.fulfilled, (state, action) => {
        // Remove the processed expense from the list of pending items
        state.items = state.items.filter(item => item.id !== action.payload.id);
      })
      .addCase(updateExpenseStatus.rejected, (state, action) => {
        state.error = action.payload;
      });
  },
});

export const { clearError } = expenseSlice.actions;
export default expenseSlice.reducer;