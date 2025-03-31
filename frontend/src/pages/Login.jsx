import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import {
  Container,
  Box,
  Paper,
  Typography,
  TextField,
  Button,
  IconButton,
  InputAdornment,
  Alert,
  CircularProgress,
} from '@mui/material';
import {
  Visibility,
  VisibilityOff,
  Google as GoogleIcon,
  GitHub as GitHubIcon,
} from '@mui/icons-material';
import { login } from '../store/slices/authSlice';

// Mock users for development
const mockUsers = [
  { email: 'admin@zidio.com', password: 'admin123', role: 'ADMIN' },
  { email: 'manager@zidio.com', password: 'manager123', role: 'MANAGER' },
  { email: 'employee@zidio.com', password: 'employee123', role: 'EMPLOYEE' },
];

function Login() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { loading, error } = useSelector((state) => state.auth);
  const [showPassword, setShowPassword] = useState(false);
  const [formData, setFormData] = useState({
    email: '',
    password: '',
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    try {
      const result = await dispatch(login(formData)).unwrap();
      const role = result.user.role.toLowerCase();
      navigate(`/${role}/dashboard`);
    } catch (err) {
      // Error will be handled by the auth slice
      console.error('Login failed:', err);
    }
  };

  return (
    <Container component="main" maxWidth="xs">
      <Box
        sx={{
          marginTop: 8,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
        }}
      >
        <Paper
          elevation={3}
          sx={{
            padding: 4,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            width: '100%',
          }}
        >
          <Typography component="h1" variant="h5" gutterBottom>
            ZIDIO EMS
          </Typography>
          <Typography variant="body2" color="text.secondary" gutterBottom>
            Enterprise Expense Management System
          </Typography>

          {error && (
            <Alert severity="error" sx={{ width: '100%', mb: 2 }}>
              {error}
            </Alert>
          )}

          <Box component="form" onSubmit={handleSubmit} sx={{ width: '100%' }}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="email"
              label="Email Address"
              name="email"
              autoComplete="email"
              autoFocus
              value={formData.email}
              onChange={handleChange}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type={showPassword ? 'text' : 'password'}
              id="password"
              autoComplete="current-password"
              value={formData.password}
              onChange={handleChange}
              InputProps={{
                endAdornment: (
                  <InputAdornment position="end">
                    <IconButton
                      aria-label="toggle password visibility"
                      onClick={() => setShowPassword(!showPassword)}
                      edge="end"
                    >
                      {showPassword ? <VisibilityOff /> : <Visibility />}
                    </IconButton>
                  </InputAdornment>
                ),
              }}
            />
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
              disabled={loading}
            >
              {loading ? <CircularProgress size={24} /> : 'Sign In'}
            </Button>

            <Box sx={{ display: 'flex', justifyContent: 'center', gap: 2, mb: 2 }}>
              <Button
                variant="outlined"
                startIcon={<GoogleIcon />}
                onClick={() => alert('Google login not implemented')}
              >
                Google
              </Button>
              <Button
                variant="outlined"
                startIcon={<GitHubIcon />}
                onClick={() => alert('GitHub login not implemented')}
              >
                GitHub
              </Button>
            </Box>

            <Typography variant="body2" color="text.secondary" align="center">
              For development, use these credentials:
            </Typography>
            <Typography variant="caption" color="text.secondary" align="center" display="block">
              Admin: admin@zidio.com / admin123
            </Typography>
            <Typography variant="caption" color="text.secondary" align="center" display="block">
              Manager: manager@zidio.com / manager123
            </Typography>
            <Typography variant="caption" color="text.secondary" align="center" display="block">
              Employee: employee@zidio.com / employee123
            </Typography>
          </Box>
        </Paper>
      </Box>
    </Container>
  );
}

export default Login; 