import { z } from "zod";

export const registrationSchema = z.object({
    username: z.string().min(1, "Username is required"),
    password: z
        .string()
        .regex(/^(?=.*\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[^\w\d\s:])([^\s]){8,16}$/gm,"Invalid password. Must contain at least one uppercase letter, one lowercase letter, one number, one special character, and be between 8 and 16 characters in length."),
    firstName: z.string().min(1, "First name is required"),
    lastName: z.string().min(1, "Last name is required"),
    role: z.enum(["USER", "ADMIN"]).default("USER"),
    email: z.string().email("Invalid email"),
    phone: z.string()
        .min(1, "Mobile phone is required")
        .length(10, "Mobile phone must be exactly 10 digits")
        .regex(/^\d+$/, "Mobile phone must contain only digits")
});

export type RegistrationFields = z.infer<typeof registrationSchema>;
export type RegistrationInput = z.input<typeof registrationSchema>;

export type RegistrationResponse = {
    firstname: string;
    lastname: string;
    email: string;
};

export async function register(fields: RegistrationInput): Promise<RegistrationResponse> {
    const payload = registrationSchema.parse(fields);
    const res = await fetch(import.meta.env.VITE_API_URL + "/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
    });
    if (!res.ok) {
        if (res.status === 400) throw new Error("Bad request. Please check your input and try again.");
        if (res.status === 409) throw new Error("Username or email already exist.");

        let detail = "Registration failed.";
        try {
            const data = await res.json();
            if (typeof data?.description === "string") detail = data.description;
        } catch (error) {
            console.error(error);
        }
        throw new Error(detail);
    }
    return await res.json();
}
