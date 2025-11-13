import React, { useState, useEffect } from 'react'; // Import useEffect
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import {
  Container, Box, Paper, Typography, TextField, Button, IconButton,
  InputAdornment, Alert, CircularProgress,
} from '@mui/material';
import { Visibility, VisibilityOff } from '@mui/icons-material';
import { login, clearError } from '../store/slices/authSlice'; // Import clearError

function Login() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  
  // Get the full auth state from Redux
  const { loading, error, isAuthenticated, user } = useSelector((state) => state.auth);
  
  const [showPassword, setShowPassword] = useState(false);
  const [formData, setFormData] = useState({ email: '', password: '' });

  // *** CHANGE #1: useEffect to handle navigation ***
  // This effect runs whenever 'isAuthenticated' or 'user' changes.
  useEffect(() => {
    // If the user is successfully authenticated and we have their user object...
    if (isAuthenticated && user) {
      // ...navigate to their specific dashboard.
      const role = user.role.toLowerCase();
      navigate(`/${role}/dashboard`);
    }

    // Cleanup function to clear any login errors if the component is unmounted
    return () => {
      dispatch(clearError());
    };

  }, [isAuthenticated, user, navigate, dispatch]);
  // =================================================

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  // *** CHANGE #2: Simplified handleSubmit ***
  // This function now only dispatches the login action.
  // It no longer handles navigation directly.
  const handleSubmit = async (e) => {
    e.preventDefault();
    dispatch(login(formData));
  };
  // ==========================================

  return (
    <Container component="main" maxWidth="xs">
      <Box sx={{ marginTop: 8, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <Paper elevation={3} sx={{ padding: 4, display: 'flex', flexDirection: 'column', alignItems: 'center', width: '100%' }}>
          <Typography component="h1" variant="h5" gutterBottom>ZIDIO EMS</Typography>
          <Typography variant="body2" color="text.secondary" gutterBottom>Enterprise Expense Management System</Typography>

          {error && (
            <Alert severity="error" sx={{ width: '100%', mb: 2 }}>
              {error.message || 'Invalid credentials. Please try again.'}
            </Alert>
          )}

          <Box component="form" onSubmit={handleSubmit} sx={{ width: '100%' }}>
            <TextField margin="normal" required fullWidth id="email" label="Email Address" name="email" autoComplete="email" autoFocus value={formData.email} onChange={handleChange} />
            <TextField
              margin="normal" required fullWidth name="password" label="Password"
              type={showPassword ? 'text' : 'password'}
              id="password" autoComplete="current-password"
              value={formData.password} onChange={handleChange}
              InputProps={{
                endAdornment: (
                  <InputAdornment position="end">
                    <IconButton aria-label="toggle password visibility" onClick={() => setShowPassword(!showPassword)} edge="end">
                      {showPassword ? <VisibilityOff /> : <Visibility />}
                    </IconButton>
                  </InputAdornment>
                ),
              }}
            />
            <Button type="submit" fullWidth variant="contained" sx={{ mt: 3, mb: 2 }} disabled={loading}>
              {loading ? <CircularProgress size={24} color="inherit" /> : 'Sign In'}
            </Button>

            <Typography variant="body2" color="text.secondary" align="center">For development, use these credentials:</Typography>
            <Typography variant="caption" color="text.secondary" align="center" display="block">Admin: admin@zidio.com / admin123</Typography>
            <Typography variant="caption" color="text.secondary" align="center" display="block">Manager: manager@zidio.com / manager123</Typography>
            <Typography variant="caption" color="text.secondary" align="center" display="block">Employee: employee@zidio.com / employee123</Typography>
          </Box>
        </Paper>
      </Box>
    </Container>
  );
}

export default Login;