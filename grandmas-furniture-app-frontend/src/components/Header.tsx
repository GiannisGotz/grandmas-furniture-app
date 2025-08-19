import GrandmaLogo from "./GrandmaLogo.tsx";
import AuthButton from "./AuthButton.tsx";
import Button from "@/components/Button.tsx";
import {useAuth} from "@/hooks/useAuth.ts";
import { useNavigate, useLocation } from "react-router";

/**
 * Application header with logo, navigation, and authentication controls.
 * Shows admin controls when user has appropriate permissions.
 */
const Header = () => {
    const { isAuthorized } = useAuth();
    const navigate = useNavigate();
    const location = useLocation();
    
    const isOnUsersPage = location.pathname === "/users";

    
    return (
        <>
            <header className="bg-gfa-primary fixed w-full">
                <div className="container mx-auto px-4 flex items-center justify-between">
                    <GrandmaLogo />
                    <nav className="flex gap-4">
                        {isAuthorized("ADMIN") && (
                            <Button 
                                variant="secondary"
                                onClick={() => navigate(isOnUsersPage ? "/ads" : "/users")}
                            >
                                {isOnUsersPage ? "Back to Marketplace" : "Manage Users"}
                            </Button>
                        )}
                        <AuthButton />
                    </nav>
                </div>
            </header>
        </>
    )
}
export default Header;