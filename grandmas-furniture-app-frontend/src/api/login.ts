import { z } from "zod";

export const loginSchema = z.object({
    username: z.string().min(1, "Username is required"),
    password: z.string().min(1, "Password is required"),
});

export type LoginFields = z.infer<typeof loginSchema>;

export type LoginResponse = {
    firstName: string;
    lastName: string;
    token: string;
    role: "USER" | "ADMIN";
};

export async function login({
                                username,
                                password,
                            }: LoginFields): Promise<LoginResponse> {
    const res = await fetch(import.meta.env.VITE_API_URL + "/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }),
    });
    if (!res.ok) {
        if (res.status === 401) throw new Error("Invalid username or password");
        if (res.status === 405) throw new Error("Unexpected method used.")

        let detail = "Login Failed.";
        try {
            const data = await res.json();
            if (typeof data?.description == "string") detail = data.description;
        } catch (error) {
            console.error(error);
        }
        throw new Error(detail);
    }
    return await res.json();
}
