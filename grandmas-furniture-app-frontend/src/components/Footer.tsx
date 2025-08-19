import { Facebook, Instagram, Twitter, Youtube } from "lucide-react";

const Footer = () => {
    const currentYear: number = new Date().getFullYear();

    return (
        <>
            <footer className="bg-gfa-primary text-shadow-gfa-secondary w-full mt-auto mt">
            <div className="container mx-auto py-4 flex justify-between items-center">
                <div className="text-gfa-secondary">
                    &copy; {currentYear} Copyright: GrandmasFurnitureApp.com
                </div>
                <div className="flex items-center gap-3">
                    <a href="https://www.facebook.com/GrandmasFurnitureApp"
                       className="inline-block hover:opacity-80 hover:scale-150 transition-all duration-300"
                       target="_blank"
                       rel="noopener noreferrer"
                       aria-label="Facebook profile"
                    >
                        <Facebook
                            className="w-6 h-6 fill-gfa-secondary stroke-gfa-primary"
                        />
                    </a>

                    <a href="https://www.instagram.com/grandmasfurnitureapp"
                       className="inline-block hover:opacity-80 hover:scale-150 transition-all duration-300"
                       target="_blank"
                       rel="noopener noreferrer"
                       aria-label="Instagram profile"
                    >
                        <Instagram
                            className="w-6 h-6 fill-gfa-secondary stroke-gfa-primary"
                        />
                    </a>

                    <a href="https://www.twitter.com/grandmasfurniture"
                       className="inline-block hover:opacity-80 hover:scale-150 transition-all duration-300"
                       target="_blank"
                       rel="noopener noreferrer"
                       aria-label="Twitter profile"
                    >
                        <Twitter
                            className="w-6 h-6 fill-gfa-secondary stroke-gfa-primary"
                        />
                    </a>

                    <a href="https://www.youtube.com/grandmasfurnitureapp"
                       className="inline-block hover:opacity-80 hover:scale-150 transition-all duration-300"
                       target="_blank"
                       rel="noopener noreferrer"
                       aria-label="YouTube channel"
                    >
                        <Youtube
                            className="w-6 h-6 fill-gfa-secondary stroke-gfa-primary"
                        /></a>
                </div>
            </div>
            </footer>
        </>
    );
};

export default Footer;
