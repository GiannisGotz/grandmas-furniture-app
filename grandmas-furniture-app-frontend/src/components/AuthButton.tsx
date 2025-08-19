import { useAuth } from "@/hooks/useAuth";
import  Button  from "./Button.tsx";
import { useNavigate } from "react-router";
import { toast } from "sonner";

export function AuthButton() {
    const { isAuthenticated, logoutUser } = useAuth();
    const navigate = useNavigate();

    const handleLogin = () => {
        navigate("auth/login");
        toast.info("Please sign in to access your account");
    };

    const handleLogout = () => {
        navigate("auth/logout");
        setTimeout(() => {
            logoutUser();
            toast.success("Logged out!");
        }, 100);
    };

    return isAuthenticated ? (
        <Button variant="secondary" onClick={handleLogout}>
            Logout
        </Button>
    ) : (
        <Button variant="tertiary" onClick={handleLogin}>
            Login
        </Button>
    );

}

export default AuthButton;