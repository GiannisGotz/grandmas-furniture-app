import { type ReactNode, useEffect, useState } from "react";
import { getCookie, setCookie, deleteCookie } from "../lib/cookies.ts";
import { jwtDecode } from "jwt-decode";
import { login, type LoginFields } from "@/api/login.ts";
import { AuthContext } from "./AuthContext";

type JwtPayload = {
    email?: string;
    tenant_id?: string;
};

/**
 * Authentication provider managing user login state, JWT tokens, and role-based access.
 * Handles token storage, decoding, and authorization checks.
 */
export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const [accessToken, setAccessToken] = useState<string | null>(null);
    const [tenantId, setTenantId] = useState<string | null>(null);
    const [userRole, setUserRole] = useState<"USER" | "ADMIN" | null>(null);
    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        const token = getCookie("access_token");
        setAccessToken(token ?? null);
        if (token) {
            try {
                const decoded = jwtDecode<JwtPayload>(token);
                console.log(decoded);
                setTenantId(decoded.tenant_id ?? null);
            } catch {
                setTenantId(null);
            }
        } else {
            setTenantId(null);
        }
        setLoading(false);
    }, []);

    const loginUser = async (fields: LoginFields) => {
        const res = await login(fields);
        setCookie("access_token", res.token, {
            expires: 1,
            sameSite: "Lax",
            secure: false,
            path: "/",
        });

        setAccessToken(res.token);
        setUserRole(res.role);

        try {
            const decoded = jwtDecode<JwtPayload>(res.token);
            setTenantId(decoded.tenant_id ?? null);
        } catch {
            setTenantId(null);
        }
    };

    const logoutUser = () => {
        deleteCookie("access_token");
        setAccessToken(null);
        setTenantId(null);
        setUserRole(null);
    };

    const isAuthorized = (requiredRole: "USER" | "ADMIN") => {
        if (!accessToken) return false; // if no token then no access
        if (requiredRole === "USER") return true; // if user role then user gets basic access
        if (requiredRole === "ADMIN") return userRole === "ADMIN"; // if admin then access to admin content is granted
        return false;
    };

    return (
        <AuthContext.Provider
            value={{
                isAuthenticated: !!accessToken,
                accessToken,
                tenantId,
                userRole,
                isAuthorized,
                loginUser,
                logoutUser,
                loading,
            }}
        >
            {loading ? null : children}
        </AuthContext.Provider>
    );
};
