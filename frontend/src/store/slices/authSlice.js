import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';

// Mock users for development
const mockUsers = {
  'admin@zidio.com': {
    password: 'admin123',
    user: {
      id: 1,
      name: 'Admin User',
      email: 'admin@zidio.com',
      role: 'ADMIN'
    }
  },
  'manager@zidio.com': {
    password: 'manager123',
    user: {
      id: 2,
      name: 'Manager User',
      email: 'manager@zidio.com',
      role: 'MANAGER'
    }
  },
  'employee@zidio.com': {
    password: 'employee123',
    user: {
      id: 3,
      name: 'Employee User',
      email: 'employee@zidio.com',
      role: 'EMPLOYEE'
    }
  }
};

// Async thunks for authentication actions
export const login = createAsyncThunk(
  'auth/login',
  async (credentials, { rejectWithValue }) => {
    try {
      // Simulate API delay
      await new Promise(resolve => setTimeout(resolve, 500));
      
      const mockUser = mockUsers[credentials.email];
      if (!mockUser || mockUser.password !== credentials.password) {
        throw new Error('Invalid credentials');
      }

      const token = btoa(JSON.stringify(mockUser.user)); // Base64 encode user data as mock token
      localStorage.setItem('token', token);
      
      return {
        user: mockUser.user,
        token
      };
    } catch (error) {
      return rejectWithValue({ message: error.message });
    }
  }
);

export const logout = createAsyncThunk(
  'auth/logout',
  async () => {
    localStorage.removeItem('token');
  }
);

export const checkAuth = createAsyncThunk(
  'auth/checkAuth',
  async (_, { rejectWithValue }) => {
    try {
      // Simulate API delay
      await new Promise(resolve => setTimeout(resolve, 500));
      
      const token = localStorage.getItem('token');
      if (!token) throw new Error('No token found');
      
      try {
        const user = JSON.parse(atob(token)); // Decode the mock token
        return user;
      } catch {
        throw new Error('Invalid token');
      }
    } catch (error) {
      return rejectWithValue({ message: error.message });
    }
  }
);

const initialState = {
  user: null,
  token: localStorage.getItem('token'),
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
      // Login
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
        state.error = action.payload?.message || 'Login failed';
      })
      // Logout
      .addCase(logout.fulfilled, (state) => {
        state.user = null;
        state.token = null;
        state.isAuthenticated = false;
      })
      // Check Auth
      .addCase(checkAuth.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(checkAuth.fulfilled, (state, action) => {
        state.loading = false;
        state.isAuthenticated = true;
        state.user = action.payload;
      })
      .addCase(checkAuth.rejected, (state, action) => {
        state.loading = false;
        state.isAuthenticated = false;
        state.user = null;
        state.token = null;
        state.error = action.payload?.message || 'Authentication failed';
      });
  },
});

export const { clearError } = authSlice.actions;
export default authSlice.reducer; 