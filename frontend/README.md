# ZIDIO Enterprise Expense Management System - Frontend

This is the frontend application for the ZIDIO Enterprise Expense Management System. It's built using React with TypeScript and Material UI.

## Features

- Modern and responsive UI using Material UI components
- Role-based access control
- Expense management with CRUD operations
- Real-time analytics and reporting
- User profile management
- System settings and preferences

## Tech Stack

- React 18
- TypeScript
- Material UI
- Redux Toolkit for state management
- React Router for navigation
- Chart.js and Recharts for analytics
- Formik and Yup for form handling and validation
- Axios for API calls

## Prerequisites

- Node.js (v14 or higher)
- npm (v6 or higher)

## Getting Started

1. Clone the repository:
```bash
git clone <repository-url>
cd zidio-ems-frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm run dev
```

4. Open your browser and navigate to `http://localhost:5173`

## Available Scripts

- `npm run dev` - Start the development server
- `npm run build` - Build the production version
- `npm run preview` - Preview the production build locally
- `npm run lint` - Run ESLint to check code quality

## Project Structure

```
src/
├── components/     # Reusable UI components
├── pages/         # Page components
├── features/      # Redux slices and feature-specific components
├── services/      # API services and utilities
├── utils/         # Helper functions and constants
├── types/         # TypeScript type definitions
├── hooks/         # Custom React hooks
├── assets/        # Static assets (images, fonts, etc.)
├── layouts/       # Layout components
├── App.tsx        # Main application component
├── main.tsx       # Application entry point
└── index.css      # Global styles
```

## Contributing

1. Create a new branch for your feature
2. Make your changes
3. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
