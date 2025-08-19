import {useEffect} from "react";
import grandmasitting from "../assets/grandmasitting.svg"

/**
 * Landing page with application introduction and welcome message.
 * Provides overview of the furniture marketplace platform.
 */
const HomePage = () => {
    useEffect(()=>{
        document.title = "Grandma's Furniture HomePage"
    }, []);

    return (
        <>
            <div className="container mx-auto min-h-[95vh] pt-24">
                <div>
                    <h1 className="font-bold text-center mb-10  text-gfa-accent">Welcome to Grandma's Furniture App</h1>
                </div>
                <div className="flex justify-evenly">
                    <img src={grandmasitting}
                         className="mx-4"
                         style={{width: '500px', height: '300px'}}
                         alt="Grandma waiting for you"
                    />
                    <div className="content-center">
                        <p className="mb-4">
                            Moving house? Downsizing? Or simply ready to pass on furniture that's been sitting unused?
                            This platform helps you connect with people in your area who are looking for exactly what you have.
                            No more wondering if your old dining table will end up in a landfill - find someone who actually needs it.
                        </p>

                        <p className="mb-4">
                            Browse listings by location, price, or furniture type. Whether you're furnishing your first apartment on a budget
                            or searching for that specific vintage piece to complete your room, you can search and filter to find what works.
                        </p>

                        <p className="mb-4">
                            Create your listing in minutes with photos and a description. Set your own price and availability.
                            Buyers can contact you through the app, and you handle the rest - payment, pickup, delivery details -
                            however works best for both of you. Simple, direct, and practical.
                        </p>
                        <p className="mb-4">
                            Ready to get started?  <strong>Press the Login button in the top right corner</strong> to sign in to your account
                            or create a new one. Once you're logged in, you can start browsing furniture or list your own items right away.
                        </p>
                    </div>
                </div>
            </div>
        </>
    )
};

export default HomePage;
