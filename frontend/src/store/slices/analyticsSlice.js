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

export const fetchDashboardSummary = createAsyncThunk(
  'analytics/fetchDashboardSummary',
  async (_, { rejectWithValue }) => {
    try {
      const response = await apiClient.get('/analytics/dashboard-summary');
      return response.data;
    } catch (error) {
      return rejectWithValue(error.response?.data);
    }
  }
);

export const fetchTeamSummary = createAsyncThunk(
  'analytics/fetchTeamSummary',
  async (_, { rejectWithValue }) => {
    try {
      const response = await apiClient.get('/analytics/summary/by-team');
      // Map to the format the chart expects { name, value }
      return response.data.map(item => ({
          name: item.fullName,
          value: item.totalAmount
      }));
    } catch (error) {
      return rejectWithValue(error.response?.data);
    }
  }
);

const initialState = {
  summaryByCategory: [],
  dashboardSummary: {},
  teamSummary: [],
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
      })
      .addCase(fetchDashboardSummary.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchDashboardSummary.fulfilled, (state, action) => {
        state.loading = false;
        state.dashboardSummary = action.payload;
      })
      .addCase(fetchDashboardSummary.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
    .addCase(fetchTeamSummary.pending, (state) => { 
        state.loading = true; state.error = null; 
    })
    .addCase(fetchTeamSummary.fulfilled, (state, action) => {
        state.loading = false;
        state.teamSummary = action.payload;
    })
    .addCase(fetchTeamSummary.rejected, (state, action) => { 
        state.loading = false; state.error = action.payload; 
    });
  },
});

export default analyticsSlice.reducer;