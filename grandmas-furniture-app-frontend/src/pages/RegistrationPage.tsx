import {useEffect} from "react";
import {useNavigate} from "react-router";
import {useForm} from "react-hook-form";
import {type RegistrationInput, registrationSchema, register as registerUser} from "../api/registration.ts";
import { z } from "zod";
import {zodResolver} from "@hookform/resolvers/zod";
import {toast} from "sonner";
import grandmabroom from "@/assets/grandmabroom.svg";
import Label from "@/components/Label.tsx";
import Input from "@/components/Input.tsx";
import Button from "@/components/Button.tsx";
import { PasswordToggleButton } from "@/components/Button.tsx";


const RegistrationPage = () => {
    const navigate = useNavigate();

    const {
        register,
        handleSubmit,
        reset,
        formState: { errors, isSubmitting },
    } = useForm<z.input<typeof registrationSchema>>({
        resolver: zodResolver(registrationSchema),
    });

    useEffect(()=>{
        document.title = "GFA Registration Page"
    }, []);

    const onSubmit = async (data: RegistrationInput) => {
        try {
            const res = await registerUser(data);
            toast.success(`Registration successfully ${res.firstname}`);
            navigate("/auth/login");
        } catch (err) {
            toast.error(err instanceof Error ? err.message : "Registration failed");
        }
    };



    return (
        <>
        <form
            onSubmit={handleSubmit(onSubmit)}
            className="max-w-sm mx-auto p-8 space-y-4 border rounded-2xl mt-24 shadow-gfa-tertiary shadow-lg"
        >
            <div>
                <div className="max-w-sm flex items-center">
                    <img src={grandmabroom}
                         style={{width: '120px', height: '95px'}}
                         alt="Grandma is sweeping"/>
                    <h1 className="text-2xl">Register for free</h1>
                </div>

                    <div className="mt-4 space-y-4">
                    <Label htmlFor="username" className="mb-1" required>Username</Label>
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
                        <div>
                        <Label htmlFor="password" className="mb-1" required>Password</Label>
                        <div className="relative">
                            <Input
                                id="password"
                                type="password"
                                autoComplete="new-password"
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
                        <div>
                        <Label htmlFor="firstName" className="mb-1" required>First name</Label>
                        <Input
                            id="firstName"
                            type="text"
                            autoComplete="given-name"
                            {...register("firstName")}
                            disabled={isSubmitting}
                        />
                        {errors.firstName && (
                            <div className="text-gfa-error text-center mt-2">{errors.firstName.message}</div>
                        )}
                    </div>
                        <div>
                        <Label htmlFor="lastName" className="mb-1" required>Last name</Label>
                        <Input
                            id="lastName"
                            type="text"
                            autoComplete="family-name"
                            {...register("lastName")}
                            disabled={isSubmitting}
                        />
                        {errors.lastName && (
                            <div className="text-gfa-error text-center mt-2">{errors.lastName.message}</div>
                        )}
                    </div>
                        <div>
                        <Label htmlFor="email" className="mb-1" required>Email</Label>
                        <Input
                            id="email"
                            type="email"
                            autoComplete="email"
                            {...register("email")}
                            disabled={isSubmitting}
                        />
                        {errors.email && (
                            <div className="text-gfa-error text-center mt-2">{errors.email.message}</div>
                        )}
                    </div>
                        <div>
                        <Label htmlFor="phone" className="mb-1" required>Contact phone</Label>
                        <Input
                            id="phone"
                            type="tel"
                            autoComplete="tel"
                            {...register("phone")}
                            disabled={isSubmitting}
                        />
                        {errors.phone && (
                            <div className="text-gfa-error text-center mt-2">{errors.phone.message}</div>
                        )}
                    </div>
                        <div className="flex justify-center mt-8">
                            <Button
                                variant="primary"
                                size="medium"
                                type="submit"
                                className="w-4/4"
                                disabled={isSubmitting}>
                                {isSubmitting ? "Creating ..." : "Submit"}
                            </Button>
                        </div>
                        <div className="flex justify-center mt-1">
                            <Button
                                variant="secondary"
                                size="medium"
                                type="button"
                                className="w-4/4"
                                disabled={isSubmitting}
                                onClick={() => reset()}>
                                {isSubmitting ? "..." : "Clear All"}
                            </Button>
                        </div>


                    </div>
            </div>
        </form>
        </>
    )

};

export default RegistrationPage;