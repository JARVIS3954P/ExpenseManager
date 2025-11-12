import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import apiClient from '../../api/axios';

/**
 * Decodes the JWT to extract user information.
 * NOTE: This is a simplified approach. In a production environment with more complex security,
 * you would typically make a separate API call to a '/users/me' endpoint after login
 * to get the full, trusted user profile from the server.
 */
const decodeToken = (token) => {
  try {
    const payloadBase64 = token.split('.')[1];
    const decodedPayload = JSON.parse(atob(payloadBase64));

    // The 'sub' claim in a JWT is the standard for the "subject" (usually the username/email).
    // Spring Security also puts custom claims like 'roles' or 'userId' in the token.
    return {
      id: decodedPayload.userId,
      email: decodedPayload.sub,
      role: decodedPayload.role,
      name: decodedPayload.fullName
    };
  } catch (error) {
    console.error("Failed to decode token:", error);
    return null;
  }
};

export const login = createAsyncThunk(
  'auth/login',
  async (credentials, { rejectWithValue }) => {
    try {
      const response = await apiClient.post('/auth/login', credentials);
      const { token } = response.data;
      localStorage.setItem('token', token);

      const user = decodeToken(token);
      if (!user) {
        throw new Error("Invalid token received from server.");
      }
      
      return { token, user };
    } catch (error) {
      const errorMsg = error.response?.data?.message || 'Invalid credentials. Please try again.';
      return rejectWithValue({ message: errorMsg });
    }
  }
);

export const logout = createAsyncThunk('auth/logout', async () => {
  localStorage.removeItem('token');
});

export const checkAuth = createAsyncThunk(
  'auth/checkAuth',
  async (_, { rejectWithValue }) => {
    const token = localStorage.getItem('token');
    if (!token) {
      return rejectWithValue({ message: 'No token found' });
    }

    const user = decodeToken(token);
    if (!user) {
      localStorage.removeItem('token'); // Clean up invalid token
      return rejectWithValue({ message: 'Invalid or expired token' });
    }
    
    return { user };
  }
);

const initialState = {
  user: null,
  token: localStorage.getItem('token') || null,
  isAuthenticated: false,
  loading: false,
  error: null,
};

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      // Login action states
      .addCase(login.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(login.fulfilled, (state, action) => {
        state.loading = false;
        state.isAuthenticated = true;
        state.user = action.payload.user;
        state.token = action.payload.token;
      })
      .addCase(login.rejected, (state, action) => {
        state.loading = false;
        state.isAuthenticated = false;
        state.user = null;
        state.token = null;
        state.error = action.payload?.message || 'Login failed';
      })
      // Logout action states
      .addCase(logout.fulfilled, (state) => {
        state.user = null;
        state.token = null;
        state.isAuthenticated = false;
        state.loading = false;
      })
      // Check Auth action states
      .addCase(checkAuth.pending, (state) => {
        state.loading = true; // Set loading to true while we verify
      })
      .addCase(checkAuth.fulfilled, (state, action) => {
        state.loading = false;
        state.isAuthenticated = true;
        state.user = action.payload.user;
      })
      .addCase(checkAuth.rejected, (state, action) => {
        state.loading = false;
        state.isAuthenticated = false;
        state.user = null;
        state.token = null;
      });
  },
});

export const { clearError } = authSlice.actions;
export default authSlice.reducer;