import React from 'react';
import {
  Dashboard as DashboardIcon,
  Receipt as ReceiptIcon,
  Analytics as AnalyticsIcon,
  Person as PersonIcon,
  Settings as SettingsIcon,
  People as PeopleIcon,
  Assessment as AssessmentIcon,
  CheckCircle as CheckCircleIcon,
  Group as GroupIcon,
} from '@mui/icons-material';

const createIcon = (Icon) => React.createElement(Icon);

const navigation = {
  ADMIN: [
    { title: 'Dashboard', icon: createIcon(DashboardIcon), path: '/admin/dashboard' },
    { title: 'User Management', icon: createIcon(PeopleIcon), path: '/admin/users' },
    { title: 'System Settings', icon: createIcon(SettingsIcon), path: '/admin/settings' },
    { title: 'Reports', icon: createIcon(AssessmentIcon), path: '/admin/reports' },
  ],
  MANAGER: [
    { title: 'Dashboard', icon: createIcon(DashboardIcon), path: '/manager/dashboard' },
    { title: 'Team Expenses', icon: createIcon(ReceiptIcon), path: '/manager/expenses' },
    { title: 'Approvals', icon: createIcon(CheckCircleIcon), path: '/manager/approvals' },
    { title: 'Team Analytics', icon: createIcon(AnalyticsIcon), path: '/manager/analytics' },
  ],
  EMPLOYEE: [
    { title: 'Dashboard', icon: createIcon(DashboardIcon), path: '/employee/dashboard' },
    { title: 'My Expenses', icon: createIcon(ReceiptIcon), path: '/employee/expenses' },
    { title: 'Submit Expense', icon: createIcon(CheckCircleIcon), path: '/employee/submit' },
    { title: 'Team Overview', icon: createIcon(GroupIcon), path: '/employee/team' },
  ],
};

export default navigation; 