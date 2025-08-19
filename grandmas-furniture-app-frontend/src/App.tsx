import Layout from "./components/Layout.tsx";
import {Toaster} from "sonner";
import {AuthProvider} from "@/context/AuthProvider.tsx";
import {BrowserRouter, Route, Routes} from "react-router";
import HomePage from "@/pages/HomePage.tsx";
import LoginPage from "@/pages/LoginPage.tsx";
import RegistrationPage from "@/pages/RegistrationPage.tsx";
import LogoutPage from "@/pages/LogoutPage.tsx";
import NotFoundPage from "@/pages/NotFoundPage.tsx";
import ProtectedRoute from "./components/ProtectedRoute.tsx";
import AdminProtectedRoute from "./components/AdminProtectedRoute.tsx";
import DashboardAdsPage from "@/pages/DashboardAdsPage.tsx";
import AdPage from "@/pages/AdPage.tsx";
import UsersPage from "@/pages/UsersPage.tsx";
import AdDetailedPage from "@/pages/AdDetailedPage.tsx";

/**
 * Main application component with routing and authentication.
 * Routes: Home, Auth (login/register/logout), Ads (CRUD), Users (admin only)
 */
function App() {
    return (
        <>
            <AuthProvider>
                <BrowserRouter>
                    <Toaster richColors />
                    <Routes>
                        <Route element={<Layout />}>
                            <Route index element={<HomePage />} />
                            <Route path="auth/login" element={<LoginPage />} />
                            <Route path="auth/register" element={<RegistrationPage />} />
                            <Route path="auth/logout" element={<LogoutPage />} />

                            <Route path="ads" element={<ProtectedRoute />}>
                                <Route index element={<DashboardAdsPage />} />
                                <Route path="new" element={<AdPage />} />
                                <Route path=":id" element={<AdDetailedPage />} />
                                <Route path=":id/edit" element={<AdPage />} />
                            </Route>

                            <Route path="users" element={<AdminProtectedRoute />}>
                                <Route index element={<UsersPage />} />
                            </Route>

                            <Route path="*" element={<NotFoundPage />} />
                        </Route>
                    </Routes>
                </BrowserRouter>
            </AuthProvider>
        </>
    )
}

export default App
