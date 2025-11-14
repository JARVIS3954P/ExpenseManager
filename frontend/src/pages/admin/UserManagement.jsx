import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { fetchUsers, updateUser } from '../../store/slices/userSlice';
import {
  Container, Paper, Typography, Table, TableBody, TableCell, TableContainer,
  TableHead, TableRow, Button, IconButton, Dialog, DialogTitle, DialogContent,
  DialogActions, TextField, Grid, MenuItem, Box, CircularProgress, Alert,
} from '@mui/material';
import { Add as AddIcon, Edit as EditIcon, Delete as DeleteIcon } from '@mui/icons-material';

const roles = ['ADMIN', 'MANAGER', 'EMPLOYEE'];

function UserManagement() {
  const dispatch = useDispatch();
  const { users, loading, error } = useSelector((state) => state.users);
  
  const [openDialog, setOpenDialog] = useState(false);
  const [currentUser, setCurrentUser] = useState(null);

  useEffect(() => {
    dispatch(fetchUsers());
  }, [dispatch]);

  const handleOpenDialog = (user = null) => {
    setCurrentUser(user || { name: '', email: '', role: 'EMPLOYEE', managerId: '' });
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setCurrentUser(null);
  };

  const handleSave = () => {
    // Here you would dispatch a createUser or updateUser thunk
    // For now, we'll just handle update
    if (currentUser.id) {
        dispatch(updateUser(currentUser));
    }
    handleCloseDialog();
  };
  
  const handleChange = (e) => {
      const { name, value } = e.target;
      setCurrentUser(prev => ({ ...prev, [name]: value }));
  };

  return (
    <Container maxWidth="lg">
      <Box sx={{ mb: 4, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
         {/* ... Title and Add User Button ... */}
      </Box>

      <Paper>
        <TableContainer>
            {loading ? <CircularProgress /> : error ? <Alert severity="error">Failed to load users.</Alert> : (
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Name</TableCell>
                <TableCell>Email</TableCell>
                <TableCell>Role</TableCell>
                <TableCell>Manager ID</TableCell>
                <TableCell>Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {users.map((user) => (
                <TableRow key={user.id}>
                  <TableCell>{user.username}</TableCell>
                  <TableCell>{user.email}</TableCell>
                  <TableCell>{user.role}</TableCell>
                  <TableCell>{user.managerId || 'N/A'}</TableCell>
                  <TableCell>
                    <IconButton size="small" onClick={() => handleOpenDialog(user)}>
                      <EditIcon />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
            )}
        </TableContainer>
      </Paper>

      {currentUser && (
        <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="sm" fullWidth>
          <DialogTitle>{currentUser.id ? 'Edit User' : 'Add New User'}</DialogTitle>
          <DialogContent>
            <Grid container spacing={2} sx={{ mt: 1 }}>
              <Grid item xs={12}>
                <TextField fullWidth label="Full Name" name="username" value={currentUser.username} onChange={handleChange} />
              </Grid>
              <Grid item xs={12}>
                <TextField fullWidth label="Email" name="email" type="email" value={currentUser.email} onChange={handleChange} />
              </Grid>
              <Grid item xs={12}>
                <TextField select fullWidth label="Role" name="role" value={currentUser.role} onChange={handleChange}>
                  {roles.map((role) => <MenuItem key={role} value={role}>{role}</MenuItem>)}
                </TextField>
              </Grid>
              <Grid item xs={12}>
                <TextField fullWidth label="Manager ID" name="managerId" value={currentUser.managerId || ''} onChange={handleChange} />
              </Grid>
            </Grid>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseDialog}>Cancel</Button>
            <Button onClick={handleSave} variant="contained">Save</Button>
          </DialogActions>
        </Dialog>
      )}
    </Container>
  );
}

export default UserManagement;