import { useAuth } from "@/hooks/useAuth.ts";
import { Navigate, Outlet } from "react-router";

/**
 * Route guard that requires user authentication.
 * Redirects to login if user is not authenticated.
 */
const ProtectedRoute = () => {
    const { isAuthenticated } = useAuth();

    if (!isAuthenticated) {
        return <Navigate to="/auth/login" replace />;
    }

    return <Outlet />;
};

export default ProtectedRoute;