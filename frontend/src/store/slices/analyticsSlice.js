import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import apiClient from '../../api/axios';

export const fetchSummaryByCategory = createAsyncThunk(
  'analytics/fetchSummaryByCategory',
  async (_, { rejectWithValue }) => {
    try {
      const response = await apiClient.get('/analytics/summary/by-category');
      return response.data.map(item => ({
          name: item.category,
          value: item.totalAmount,
      }));
    } catch (error) {
      return rejectWithValue(error.response?.data);
    }
  }
);

const initialState = {
  summaryByCategory: [],
  loading: false,
  error: null,
};

const analyticsSlice = createSlice({
  name: 'analytics',
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchSummaryByCategory.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchSummaryByCategory.fulfilled, (state, action) => {
        state.loading = false;
        state.summaryByCategory = action.payload;
      })
      .addCase(fetchSummaryByCategory.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });
  },
});

export default analyticsSlice.reducer;