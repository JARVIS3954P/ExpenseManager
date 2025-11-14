import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import apiClient from '../../api/axios';

export const fetchUsers = createAsyncThunk('users/fetchUsers', async (_, { rejectWithValue }) => {
    try {
        const response = await apiClient.get('/users');
        return response.data;
    } catch (error) { return rejectWithValue(error.response.data); }
});

export const updateUser = createAsyncThunk('users/updateUser', async (userData, { rejectWithValue }) => {
    try {
        const response = await apiClient.put(`/users/${userData.id}`, userData);
        return response.data;
    } catch (error) { return rejectWithValue(error.response.data); }
});

const initialState = { users: [], loading: false, error: null };

const userSlice = createSlice({
    name: 'users',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(fetchUsers.pending, (state) => { state.loading = true; })
            .addCase(fetchUsers.fulfilled, (state, action) => {
                state.loading = false;
                state.users = action.payload;
            })
            .addCase(fetchUsers.rejected, (state, action) => { state.loading = false; state.error = action.payload; })
            .addCase(updateUser.fulfilled, (state, action) => {
                const index = state.users.findIndex(user => user.id === action.payload.id);
                if (index !== -1) {
                    state.users[index] = action.payload;
                }
            });
    },
});

export default userSlice.reducer;