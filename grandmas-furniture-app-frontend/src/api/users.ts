import { type RegistrationFields } from "./registration";

export interface User extends Omit<RegistrationFields, "password"> {
    id: number;
    firstname: string;
    lastname: string;
    isActive: boolean;
}

export interface PageResponse<T> {
    content: T[];
    totalElements: number;
    number: number;
    size: number;
}

export async function fetchUsers(options?: { token?: string }): Promise<User[]> {
    const headers: Record<string, string> = {};
    if (options?.token) headers["Authorization"] = `Bearer ${options.token}`;
    
    const response = await fetch(`${import.meta.env.VITE_API_URL}/users/paginated?page=0&size=100`, {
        headers
    });
    
    if (!response.ok) {
        throw new Error("Failed to fetch users");
    }
    
    const data: PageResponse<User> = await response.json();
    return data.content || [];
}

export async function deleteUser(username: string, options?: { token?: string }): Promise<void> {
    const headers: Record<string, string> = {};
    if (options?.token) headers["Authorization"] = `Bearer ${options.token}`;

    const response = await fetch(`${import.meta.env.VITE_API_URL}/users/${encodeURIComponent(username)}`, {
        method: "DELETE",
        headers
    });

    if (!response.ok) {
        switch (response.status) {
            case 404:
                throw new Error("User not found");
            case 403:
                throw new Error("Not authorized to delete this user");
            case 409:
            case 400:
                throw new Error("Cannot delete user who has active ads. Please delete their ads first.");
            default:
                throw new Error("Failed to delete user");
        }
    }
}

export async function updateUserRole(userId: number, newRole: "USER" | "ADMIN", options?: { token?: string }): Promise<void> {
    const headers: Record<string, string> = {
        "Content-Type": "application/json"
    };
    if (options?.token) headers["Authorization"] = `Bearer ${options.token}`;

    const response = await fetch(`${import.meta.env.VITE_API_URL}/users/${userId}/role`, {
        method: "PUT",
        headers,
        body: JSON.stringify({ role: newRole })
    });

    if (!response.ok) {
        switch (response.status) {
            case 404:
                throw new Error("User not found");
            case 403:
                throw new Error("Not authorized to update user role");
            case 400:
                throw new Error("Invalid role specified");
            default:
                throw new Error("Failed to update user role");
        }
    }
}

