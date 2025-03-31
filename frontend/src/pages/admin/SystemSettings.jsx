import React, { useState } from 'react';
import {
  Container,
  Paper,
  Typography,
  Grid,
  TextField,
  Button,
  Switch,
  FormControlLabel,
  Divider,
  Box,
  Alert,
} from '@mui/material';
import { Save as SaveIcon } from '@mui/icons-material';

// Mock data for demo
const mockSettings = {
  company: {
    name: 'ZIDIO',
    email: 'support@zidio.com',
    phone: '+1 (555) 123-4567',
    address: '123 Business Ave, Suite 100, San Francisco, CA 94107',
  },
  expense: {
    maxAmount: 10000,
    requireReceipt: true,
    categories: ['Travel', 'Meals', 'Office Supplies', 'Equipment', 'Training'],
    defaultCurrency: 'USD',
  },
  notifications: {
    emailNotifications: true,
    slackNotifications: false,
    approvalReminders: true,
    budgetAlerts: true,
  },
  security: {
    twoFactorAuth: true,
    sessionTimeout: 30,
    passwordExpiry: 90,
    loginAttempts: 5,
  },
};

function SystemSettings() {
  const [settings, setSettings] = useState(mockSettings);
  const [saved, setSaved] = useState(false);

  const handleChange = (section, field) => (event) => {
    const value = event.target.type === 'checkbox' ? event.target.checked : event.target.value;
    setSettings(prev => ({
      ...prev,
      [section]: {
        ...prev[section],
        [field]: value,
      },
    }));
  };

  const handleSave = () => {
    // Simulate API call
    setTimeout(() => {
      setSaved(true);
      setTimeout(() => setSaved(false), 3000);
    }, 1000);
  };

  const renderSection = (title, section, fields) => (
    <Paper sx={{ p: 3, mb: 3 }}>
      <Typography variant="h6" gutterBottom>
        {title}
      </Typography>
      <Grid container spacing={3}>
        {fields.map((field) => (
          <Grid item xs={12} md={6} key={field.name}>
            {field.type === 'switch' ? (
              <FormControlLabel
                control={
                  <Switch
                    checked={settings[section][field.name]}
                    onChange={handleChange(section, field.name)}
                  />
                }
                label={field.label}
              />
            ) : (
              <TextField
                fullWidth
                label={field.label}
                value={settings[section][field.name]}
                onChange={handleChange(section, field.name)}
                type={field.type || 'text'}
                multiline={field.multiline}
                rows={field.rows}
              />
            )}
          </Grid>
        ))}
      </Grid>
    </Paper>
  );

  return (
    <Container maxWidth="lg">
      <Box sx={{ mb: 4 }}>
        <Typography variant="h5">System Settings</Typography>
        <Typography variant="body2" color="text.secondary">
          Configure system-wide settings and preferences
        </Typography>
      </Box>

      {saved && (
        <Alert severity="success" sx={{ mb: 2 }}>
          Settings saved successfully!
        </Alert>
      )}

      {renderSection('Company Information', 'company', [
        { name: 'name', label: 'Company Name' },
        { name: 'email', label: 'Support Email' },
        { name: 'phone', label: 'Contact Phone' },
        { name: 'address', label: 'Company Address', multiline: true, rows: 2 },
      ])}

      {renderSection('Expense Settings', 'expense', [
        { name: 'maxAmount', label: 'Maximum Expense Amount', type: 'number' },
        { name: 'requireReceipt', label: 'Require Receipt for All Expenses', type: 'switch' },
        { name: 'defaultCurrency', label: 'Default Currency' },
      ])}

      {renderSection('Notification Settings', 'notifications', [
        { name: 'emailNotifications', label: 'Enable Email Notifications', type: 'switch' },
        { name: 'slackNotifications', label: 'Enable Slack Notifications', type: 'switch' },
        { name: 'approvalReminders', label: 'Send Approval Reminders', type: 'switch' },
        { name: 'budgetAlerts', label: 'Send Budget Alerts', type: 'switch' },
      ])}

      {renderSection('Security Settings', 'security', [
        { name: 'twoFactorAuth', label: 'Enable Two-Factor Authentication', type: 'switch' },
        { name: 'sessionTimeout', label: 'Session Timeout (minutes)', type: 'number' },
        { name: 'passwordExpiry', label: 'Password Expiry (days)', type: 'number' },
        { name: 'loginAttempts', label: 'Maximum Login Attempts', type: 'number' },
      ])}

      <Box sx={{ mt: 3, display: 'flex', justifyContent: 'flex-end' }}>
        <Button
          variant="contained"
          startIcon={<SaveIcon />}
          onClick={handleSave}
        >
          Save Changes
        </Button>
      </Box>
    </Container>
  );
}

export default SystemSettings; 