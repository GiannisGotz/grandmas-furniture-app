# Grandma's Furniture App - Frontend

React TypeScript frontend for the furniture marketplace application with unified search functionality and improved user experience.

## 🚀 Quick Start

### Prerequisites
- Node.js 18 or higher
- npm, yarn, or pnpm

### Installation
```bash
# Using npm
npm install
npm install lucide-react@latest

# Using yarn
yarn install

# Using pnpm
pnpm install
```

### Development
```bash
npm run dev
```
Application will start on `http://localhost:5173`

### Build
```bash
npm run build
```

### Preview Production Build
```bash
npm run preview
```

## 🔐 Test Credentials

Use these credentials for testing different user roles:

### Admin User
- **Username:** `admin1`
- **Password:** `Cosmote1@`
- **Features:** Can manage all ads and users, access admin panel

### Regular User
- **Username:** `user1`
- **Password:** `Cosmote1@`
- **Features:** Can create and manage own ads only

## ✨ Features

### 🔐 Authentication & Security
- JWT-based authentication with secure cookie storage
- Role-based access control (Admin/User)
- Protected routes and components
- Automatic token refresh handling
- Secure HTTP-only cookie storage

### 🪑 Furniture Marketplace
- **Unified Dashboard** - Single interface for browsing and managing ads
- **Smart Search** - Advanced filtering with server-side processing
- **Context-Aware Views** - "All Items" vs "My Items" with automatic filtering
- **Create & Edit** - Add new furniture listings with image upload
- **Delete Management** - Remove listings with confirmation dialogs
- **Real-time Updates** - Immediate feedback for all user actions

### 🔍 Advanced Search & Filtering
- **Text Search** - Search by title, city, or description
- **Price Range** - Filter by minimum and maximum price
- **Availability Filter** - Show all items or only available ones
- **Smart Context** - Automatic user filtering for "My Items"
- **Server-Side Processing** - Efficient filtering for large datasets
- **Pagination** - Navigate through results with customizable page sizes

### 🎨 User Interface
- **Responsive Design** - Works seamlessly on desktop, tablet, and mobile
- **Modern UI** - Built with Tailwind CSS and shadcn/ui components
- **Toast Notifications** - Real-time feedback for user actions
- **Loading States** - Smooth loading indicators and skeleton screens
- **Error Handling** - User-friendly error messages with recovery options
- **Accessibility** - ARIA labels and keyboard navigation support

### 👑 Admin Features (Admin users only)
- **User Management** - View all registered users with pagination
- **Delete Users** - Remove users from the system with confirmation
- **Manage All Ads** - Edit/delete any furniture listing
- **System Overview** - Full administrative access and control

## 🛠️ Technology Stack

- **React 19** - Modern React with hooks and functional components
- **TypeScript** - Type-safe development with strict type checking
- **Vite** - Fast build tool and development server with HMR
- **React Router 7** - Client-side routing with protected routes
- **Tailwind CSS** - Utility-first CSS framework for rapid UI development
- **shadcn/ui** - High-quality, accessible component library
- **Sonner** - Elegant toast notification system
- **JWT Decode** - Secure JWT token handling and validation
- **Zod** - Runtime type validation and schema definition

## 📁 Project Structure

```
src/
├── api/                     # API functions and service calls
│   ├── ads.ts              # Ad-related API calls
│   ├── login.ts            # Authentication API calls
│   ├── registration.ts     # User registration API calls
│   └── users.ts            # User management API calls
├── assets/                  # Static assets (images, icons, SVGs)
│   ├── furniturelogin.svg  # Login page illustration
│   ├── grandawaving.svg    # Various grandma illustrations
│   ├── grandmabroom.svg    # Broom illustration
│   ├── grandmacleaningtable.svg # Cleaning table illustration
│   ├── grandmaflowers.svg  # Flowers illustration
│   ├── grandmalogo.svg     # Application logo
│   ├── grandmamirror.svg   # Mirror illustration
│   └── grandmasitting.svg  # Sitting illustration
├── components/              # Reusable UI components
│   ├── ads/                # Ad-related components
│   │   ├── AdRowCard.tsx   # Individual ad display card
│   │   └── AdsTable.tsx    # Paginated ads table
│   ├── ui/                 # shadcn/ui components
│   │   ├── alert-dialog.tsx # Alert dialog component
│   │   ├── button.tsx      # UI button component
│   │   └── sonner.tsx      # Toast notifications
│   ├── AdminProtectedRoute.tsx # Admin route protection
│   ├── AuthButton.tsx      # Authentication button
│   ├── Button.tsx          # Custom button component
│   ├── Footer.tsx          # Application footer
│   ├── GrandmaLogo.tsx     # Logo component
│   ├── Header.tsx          # Navigation header
│   ├── Input.tsx           # Form input component
│   ├── Label.tsx           # Form label component
│   ├── Layout.tsx          # Page layout wrapper
│   ├── Pagination.tsx      # Pagination component
│   └── ProtectedRoute.tsx  # Route protection component
├── config/                  # Configuration files
│   └── constants.ts        # Application constants
├── context/                 # React context providers
│   ├── AuthContext.ts      # Authentication state
│   └── AuthProvider.tsx    # Auth provider component
├── hooks/                   # Custom React hooks
│   └── useAuth.ts          # Authentication hook
├── lib/                     # Utility functions and helpers
│   ├── cookies.ts          # Cookie management utilities
│   └── utils.ts            # General utility functions
├── pages/                   # Page components
│   ├── AdDetailedPage.tsx  # Individual ad details page
│   ├── AdPage.tsx          # Ad creation/editing page
│   ├── DashboardAdsPage.tsx # Main dashboard with search
│   ├── HomePage.tsx        # Application home page
│   ├── LoginPage.tsx       # User authentication page
│   ├── LogoutPage.tsx      # Logout confirmation page
│   ├── NotFoundPage.tsx    # 404 error page
│   ├── RegistrationPage.tsx # User registration page
│   └── UsersPage.tsx       # User management (admin only)
├── App.tsx                  # Main application component
├── index.css               # Global styles
├── main.tsx                # Application entry point
└── vite-env.d.ts           # Vite environment types
```

## 🔧 Configuration

### Environment Variables
Create a `.env.local` file in the root directory:

```env
# API Configuration
VITE_API_URL=http://localhost:8080/api

# Optional: Development settings
VITE_DEBUG_MODE=true
VITE_LOG_LEVEL=info
```

### API Integration
The frontend communicates with the Spring Boot backend through RESTful APIs:

- **Base URL**: `http://localhost:8080/api`
- **Authentication**: Bearer token in Authorization header
- **Content Type**: JSON for requests, multipart for file uploads
- **CORS**: Configured for seamless frontend-backend communication

## 🎨 UI Components

### Core Components
- **Button** - Custom button with multiple variants (primary, secondary, neutral)
- **Pagination** - Navigate through paginated content with page size options
- **AdRowCard** - Display individual furniture listings with actions
- **AdsTable** - Paginated table with search and filtering capabilities

### Layout Components
- **Header** - Navigation with user authentication and role-based menu
- **AuthButton** - Login/logout functionality with user context
- **ProtectedRoute** - Route protection based on user roles and authentication
- **Layout** - Consistent page structure and spacing

### Form Components
- **LoginForm** - User authentication with validation
- **AdForm** - Create/edit furniture listings with image upload
- **Input** - Reusable form input components
- **Label** - Accessible form labels

## 🔒 Authentication & Authorization

### Authentication Flow
1. User logs in with username/password
2. Backend validates credentials and returns JWT token
3. Token stored securely in HTTP-only cookie
4. All API requests automatically include Authorization header
5. Protected routes check authentication status and user role

### Permission System
```typescript
// Check user authorization
const { isAuthorized } = useAuth();

// Admin-only features
const isAdmin = isAuthorized("ADMIN");

// User ownership checks
const canEdit = isAdmin || showMyAds; // Simple ownership via "My Items" view

// Route protection
<ProtectedRoute requiredRole="ADMIN">
  <UsersPage />
</ProtectedRoute>
```

## 🚦 Routing & Navigation

```typescript
// Public routes
/login          - Authentication page
/register       - User registration

// Protected routes (require login)
/               - Furniture marketplace dashboard
/ads/new        - Create new furniture listing
/ads/:id        - View individual ad details
/ads/:id/edit   - Edit existing ad
/users          - User management (admin only)

// Dynamic routes with parameters
/ads/:id        - Dynamic ad ID routing
```

## 📱 Responsive Design

The application is fully responsive with comprehensive breakpoints:

- **Mobile**: < 640px (sm)
- **Tablet**: 640px - 1024px (md-lg)
- **Desktop**: > 1024px (xl+)

### Responsive Features
- Adaptive grid layouts that stack on mobile
- Mobile-optimized navigation with hamburger menu
- Touch-friendly interactions and button sizes
- Optimized image loading and lazy rendering
- Responsive typography and spacing

## 🔍 Search & Filtering

### Unified Dashboard
The main dashboard provides a single interface for all furniture browsing:

- **"All Items" Mode**: Browse entire marketplace with search filters
- **"My Items" Mode**: View and manage your own listings
- **Search Panel**: Advanced filtering with real-time results
- **Availability Toggle**: Show all items or only available ones

### Search Capabilities
```typescript
// Search filters object
const searchFilters = {
  title: "vintage chair",        // Text search
  cityName: "Athens",            // Location filter
  minPrice: 50,                  // Price range
  maxPrice: 500,                 // Price range
  isAvailable: true,             // Availability filter
  myAds: false,                  // User context
  page: 1,                       // Pagination
  pageSize: 10                   // Page size
};
```

### Filter Options
- **Text Search**: Title, city, and description
- **Price Range**: Minimum and maximum price filters
- **Availability**: Toggle between all items and available only
- **User Context**: Automatic filtering for personal items
- **Pagination**: Configurable page sizes and navigation

## 🚀 Performance Features

### Optimization Strategies
- **Server-Side Filtering**: Efficient database queries with JPA specifications
- **Pagination**: Limit result sets for better performance
- **Lazy Loading**: Load images and content as needed
- **Debounced Search**: Reduce API calls during typing
- **Memoization**: Cache expensive calculations and API responses

### Code Splitting
- Route-based code splitting for better initial load times
- Lazy loading of admin components
- Optimized bundle sizes with tree shaking

## 🔧 Development

### Adding New Components
```bash
# Use shadcn/ui CLI to add components
npx shadcn@latest add button
npx shadcn@latest add dialog
npx shadcn@latest add form
```

### API Integration
```typescript
// Create new API functions in src/api/
export async function createFurniture(data: FurnitureData) {
  const response = await fetch(`${API_URL}/furniture`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(data)
  });
  
  if (!response.ok) {
    throw new Error('Failed to create furniture');
  }
  
  return response.json();
}
```

### Type Safety
```typescript
// Define types for all API responses
export type Ad = {
  id: number;
  title: string;
  price: number;
  isAvailable: boolean;
  category: string;
  city: string;
  condition: string;
  description?: string;
  imageUrl?: string;
  ownerName?: string;
  ownerPhone?: string;
}

// Use Zod for runtime validation
export const adSchema = z.object({
  title: z.string().min(1, "Title is required").max(30, "Title too long"),
  price: z.number().min(0, "Price must be positive"),
  categoryName: z.string().min(1, "Category is required"),
  cityName: z.string().min(1, "City is required"),
  condition: z.string().min(1, "Condition is required"),
  description: z.string().min(1, "Description is required").max(1000, "Description too long"),
  isAvailable: z.boolean(),
  image: z.instanceof(File).optional()
});
```

## 📋 Available Scripts

```bash
npm run dev          # Start development server
npm run build        # Build for production
npm run preview      # Preview production build
npm run lint         # Run ESLint for code quality
npm run type-check   # TypeScript type checking
npm run test         # Run unit tests (if configured)
```

## 📝 Recent Improvements

### v2.0 - Unified Search & Dashboard
- **Consolidated Interface**: Single dashboard for all furniture browsing
- **Smart Search**: Server-side filtering with advanced options
- **Context Awareness**: Automatic user filtering for personal items
- **Better Performance**: Efficient pagination and lazy loading
- **Enhanced UX**: Clear availability indicators and responsive design
- **Cleaner Code**: Simplified component logic and better state management



## 📄 License

This project is licensed under the MIT License.
