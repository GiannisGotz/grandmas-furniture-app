import { useContext } from "react";
import { AuthContext } from "../context/AuthContext.ts";

/**
 * Hook for accessing authentication context and user state.
 * Must be used within an AuthProvider component.
 */
export function useAuth() {
    const ctx = useContext(AuthContext);
    if (!ctx) throw new Error("useAuth must be used within AuthProvider");
    return ctx;
}