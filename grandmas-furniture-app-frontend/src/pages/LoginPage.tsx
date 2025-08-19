import { useForm } from "react-hook-form";
import { useEffect } from "react";
import {useNavigate} from "react-router";
import {useAuth} from "../hooks/useAuth.ts";
import {type LoginFields, loginSchema} from "../api/login.ts";
import { zodResolver } from "@hookform/resolvers/zod";
import {toast} from "sonner";
import furniturelogin from "../assets/furniturelogin.svg"
import Label from "../components/Label.tsx";
import Input from "../components/Input.tsx";
import Button from "../components/Button.tsx";
import { PasswordToggleButton } from "../components/Button.tsx";


const LogInPage = () => {
    const { loginUser } = useAuth();
    const navigate = useNavigate();

    const {
        register,
        handleSubmit,
        formState: { errors, isSubmitting },
    } = useForm<LoginFields>({
        resolver: zodResolver(loginSchema),
    });

    useEffect(()=>{
        document.title = "GFA Login Page"
    }, []);

    const onSubmit = async (data: LoginFields) => {
        try {
            await loginUser(data);
            toast.success("Login successful");
            navigate("/ads");
        } catch (err) {
            toast.error(err instanceof Error ? err.message : "Login failed");
        }
    };

    return (
        <>
            <form
                onSubmit={handleSubmit(onSubmit)}
                className="bg-gfa-primary max-w-sm mx-auto p-8 space-y-4 border rounded-2xl mt-24"
            >
                <img src={furniturelogin}
                     style={{width: '300px', height: '200px'}}
                     alt="Furniture Login"/>
                <h1 className="my-8 text-center text-3xl text-gfa-secondary">Login</h1> <div>
                <Label htmlFor="username" className="mb-1"></Label>
                <Input
                    id="username"
                    autoFocus
                    autoComplete="username"
                    {...register("username")}
                    disabled={isSubmitting}
                />
                {errors.username && (
                    <div className="text-gfa-error text-center mt-2">{errors.username.message}</div>
                )}
            </div>
                <div>
                    <Label htmlFor="password" className="mb-1"></Label>
                    <div className="relative">
                        <Input
                            id="password"
                            type="password"
                            autoComplete="current-password"
                            className="pr-12"
                            {...register("password")}
                            disabled={isSubmitting}
                        />
                        <PasswordToggleButton inputId="password" />
                    </div>
                    {errors.password && (
                        <div className="text-gfa-error text-center mt-2">{errors.password.message}</div>
                    )}
                </div>
                <div className="flex justify-center mt-8">
                <Button
                    variant="secondary"
                    size="medium"
                    type="submit"
                    className="w-4/4"
                    disabled={isSubmitting}>
                    {isSubmitting ? "Logging ..." : "Login"}
                </Button>
                </div>
                <div className="flex justify-center">
                    <Button
                        variant="secondary"
                        size="medium"
                        className="w-4/4"
                        onClick={() => navigate("/auth/register")}
                        disabled={isSubmitting}>
                        {isSubmitting ? "Loading ..." : "Create an account"}
                    </Button>
                </div>

            </form>
        </>
    )



}

export default LogInPage;