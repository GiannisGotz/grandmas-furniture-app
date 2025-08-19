import { createContext } from "react";
import type { LoginFields } from "../api/login.ts";

type AuthContextProps = {
    isAuthenticated: boolean;
    accessToken: string | null;
    tenantId: string | null;
    userRole: "USER" | "ADMIN" | null;
    isAuthorized: (requiredRole: "USER" | "ADMIN") => boolean;
    loginUser: (fields: LoginFields) => Promise<void>;
    logoutUser: () => void;
    loading: boolean;
};



export const AuthContext =
    createContext<AuthContextProps | undefined>(
        undefined,
    );

// const { isAdmin, userRole } = useAuth();
//
// return (
//     <div>
//         {isAdmin && <AdminPanel />}
// {userRole === 'moderator' && <ModeratorTools />}
// </div>
// );