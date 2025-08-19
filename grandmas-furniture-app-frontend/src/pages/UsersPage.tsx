import { useEffect, useState } from "react";
import { useAuth } from "@/hooks/useAuth";
import { fetchUsers, deleteUser, updateUserRole, type User } from "@/api/users";
import { Trash, UserPen } from "lucide-react";
import Button from "@/components/Button.tsx";
import { toast } from "sonner";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle,
    AlertDialogTrigger,
} from "@/components/ui/alert-dialog";

const UsersPage = () => {
    const { accessToken } = useAuth();
    const [users, setUsers] = useState<User[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [deleting, setDeleting] = useState<number | null>(null);
    const [updatingRole, setUpdatingRole] = useState<number | null>(null);

    useEffect(() => {
        document.title = "GFA Users Management Page";
    }, []);


    useEffect(() => {
        const loadUsers = async () => {
            try {
                setLoading(true);
                const data = await fetchUsers({ token: accessToken ?? undefined });
                setUsers(data);
            } catch (err) {
                setError(err instanceof Error ? err.message : "Failed to load users");
            } finally {
                setLoading(false);
            }
        };

        if (accessToken) {
            loadUsers();
        }
    }, [accessToken]);

    const handleDelete = async (user: User) => {
        try {
            setDeleting(user.id);
            await deleteUser(user.username, { token: accessToken ?? undefined });
            setUsers(users.filter(u => u.id !== user.id));
            toast.success(`User "${user.username}" deleted successfully`);
        } catch (err) {
            toast.error(err instanceof Error ? err.message : "Failed to delete user");
        } finally {
            setDeleting(null);
        }
    };

    const handleRoleToggle = async (user: User) => {
        try {
            setUpdatingRole(user.id);
            const newRole = user.role === "ADMIN" ? "USER" : "ADMIN";
            await updateUserRole(user.id, newRole, { token: accessToken ?? undefined });

            // Update the user in the local state
            setUsers(users.map(u =>
                u.id === user.id
                    ? { ...u, role: newRole }
                    : u
            ));
            
            toast.success(`User "${user.username}" role updated to ${newRole}`);
        } catch (err) {
            toast.error(err instanceof Error ? err.message : "Failed to update user role");
        } finally {
            setUpdatingRole(null);
        }
    };

    if (loading) return <div className="p-8">Loading users...</div>;
    if (error) return <div className="p-8 text-gfa-error">Error: {error}</div>;

    return (
        <div className="max-w-full mx-auto p-8">

            <h1 className="text-3xl font-bold mb-6">Manage Users</h1>
            
            <div className="bg-white shadow-md rounded-2xl">
                <table className="min-w-full">
                    <thead className="bg-gray-50">
                        <tr>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 ">
                                ID
                            </th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 r">
                                USERNAME
                            </th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 ">
                                NAME
                            </th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500">
                                EMAIL
                            </th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500">
                                CONTACT NUMBER
                            </th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500">
                                ROLE
                            </th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500">
                                STATUS
                            </th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500">
                                ACTIONS
                            </th>
                        </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                        {users.map((user) => (
                            <tr key={user.id} className="hover:bg-gray-50">
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                    {user.id}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                                    {user.username}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                    {user.firstname} {user.lastname}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                    {user.email}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                    {user.phone}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap">
                                    <span className={`px-2 py-1 text-xs font-semibold rounded-full ${
                                        user.role === "ADMIN" 
                                            ? "bg-red-100 text-red-800" 
                                            : "bg-green-100 text-green-800"
                                    }`}>
                                        {user.role}
                                    </span>
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap">
                                    <span className={`px-2 py-1 text-xs font-semibold rounded-full ${
                                        user.isActive 
                                            ? "bg-green-100 text-green-800" 
                                            : "bg-gray-100 text-gray-800"
                                    }`}>
                                        {user.isActive ? "Active" : "Inactive"}
                                    </span>
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap">
                                    <div className="flex gap-2">
                                        {/* Role Toggle Button */}
                                        <AlertDialog>
                                            <AlertDialogTrigger asChild>
                                                <Button
                                                    className={`flex items-center border-none ${
                                                        user.role === "ADMIN" 
                                                            ? "bg-red-100 hover:bg-green-100" 
                                                            : "bg-green-100 hover:bg-red-100"
                                                    } text-black`}
                                                    variant="neutral"
                                                    size="small"
                                                    disabled={updatingRole === user.id}
                                                    aria-label="Toggle user role"
                                                >
                                                    <UserPen className="w-4 h-4" />
                                                </Button>
                                            </AlertDialogTrigger>
                                            <AlertDialogContent>
                                                <AlertDialogHeader>
                                                    <AlertDialogTitle>Change User Role</AlertDialogTitle>
                                                    <AlertDialogDescription>
                                                        Are you sure you want to change "{user.username}"'s role from {user.role} to {user.role === "ADMIN" ? "USER" : "ADMIN"}?
                                                    </AlertDialogDescription>
                                                </AlertDialogHeader>
                                                <AlertDialogFooter>
                                                    <AlertDialogCancel>Cancel</AlertDialogCancel>
                                                    <AlertDialogAction 
                                                        onClick={() => handleRoleToggle(user)}
                                                        className={`${
                                                            user.role === "ADMIN" 
                                                                ? "bg-gfa-primary hover:bg-gfa-accent"
                                                                : "bg-gfa-primary hover:bg-gfa-accent"
                                                        }`}
                                                    >
                                                        Change to {user.role === "ADMIN" ? "USER" : "ADMIN"}
                                                    </AlertDialogAction>
                                                </AlertDialogFooter>
                                            </AlertDialogContent>
                                        </AlertDialog>

                                        {/* Delete Button */}
                                        <AlertDialog>
                                            <AlertDialogTrigger asChild>
                                                <Button
                                                    className="flex items-center border-none bg-gfa-error hover:bg-red-800 text-white"
                                                    variant="neutral"
                                                    size="small"
                                                    disabled={deleting === user.id}
                                                    aria-label="Delete user"
                                                >
                                                    <Trash className="w-4 h-4" />
                                                </Button>
                                            </AlertDialogTrigger>
                                            <AlertDialogContent>
                                                <AlertDialogHeader>
                                                    <AlertDialogTitle>Delete User</AlertDialogTitle>
                                                    <AlertDialogDescription>
                                                        Are you sure you want to delete user "{user.username}"? 
                                                        This action cannot be undone and will permanently remove the user from the system.
                                                    </AlertDialogDescription>
                                                </AlertDialogHeader>
                                                <AlertDialogFooter>
                                                    <AlertDialogCancel>Cancel</AlertDialogCancel>
                                                    <AlertDialogAction 
                                                        onClick={() => handleDelete(user)}
                                                        className="bg-gfa-error hover:bg-red-800"
                                                    >
                                                        Delete
                                                    </AlertDialogAction>
                                                </AlertDialogFooter>
                                            </AlertDialogContent>
                                        </AlertDialog>
                                    </div>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
            
            {users.length === 0 && (
                <div className="text-center py-8">
                    No users found
                </div>
            )}
        </div>
    );
};

export default UsersPage;