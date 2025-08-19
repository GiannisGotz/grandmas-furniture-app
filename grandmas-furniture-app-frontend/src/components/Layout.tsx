import Footer from "./Footer.tsx";
import Header from "./Header.tsx";
import {Outlet} from "react-router";

/**
 * Main layout wrapper with header, footer, and content area.
 * Provides consistent page structure across the application.
 */
const Layout = () => {
    return (
        <>
            <Header />
            <div className="container mx-auto min-h-[95vh] pt-24 pb-8">
                <Outlet />
            </div>
            <Footer />
        </>
    );
};

export default Layout;
