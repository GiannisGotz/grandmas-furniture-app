import { useAuth } from "@/hooks/useAuth.ts";
import { Navigate, Outlet } from "react-router";

/**
 * Route guard that requires admin privileges.
 * Redirects to ads page if user is not an admin.
 */
const AdminProtectedRoute = () => {
    const { isAuthenticated, isAuthorized } = useAuth();

    if (!isAuthenticated) {
        return <Navigate to="auth/login" replace />;
    }

    if (!isAuthorized("ADMIN")) {
        return <Navigate to="auth/login" replace />;
    }

    return <Outlet />;
};

export default AdminProtectedRoute;