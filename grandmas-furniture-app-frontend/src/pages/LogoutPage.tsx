import { useNavigate } from "react-router";
import Button from "@/components/Button";
import grandmawaving from "../assets/grandawaving.svg";


const LogoutPage = () => {
    const navigate = useNavigate();


    const handleBackToHome = () => {
        navigate("/");
    };

    const handleLogin = () => {
        navigate("/auth/login");
    };

    return (
        <div className="container mx-auto mt-32 flex items-center justify-center">
            <div className="max-w-md w-full mx-auto p-8 text-center">
                <div className="mb-6">
                    <img src={grandmawaving}
                         className="mx-4"
                         style={{width: '500px', height: '300px'}}
                         alt="Grandma's saying goodbye"
                    />
                </div>
                
                <div className="mb-6">
                    <h1 className="text-2xl font-bold text-gray-800 mb-2">
                        See You Soon!
                    </h1>
                    <p className="text-gray-600">
                        You have been successfully logged out from Grandma's Furniture App.
                    </p>
                </div>

                <div className="space-y-4">
                    <Button 
                        onClick={handleLogin}
                        className="w-full"
                        variant="primary"
                    >
                        Sign In Again
                    </Button>
                    
                    <Button 
                        onClick={handleBackToHome}
                        className="w-full"
                        variant="secondary"
                    >
                        Back to Home
                    </Button>
                </div>
            </div>
        </div>
    );
};

export default LogoutPage;