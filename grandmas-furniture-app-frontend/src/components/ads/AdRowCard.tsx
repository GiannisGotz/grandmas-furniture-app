import type { Ad } from "@/api/ads";
import Button from "@/components/Button";
import { useAuth } from "@/hooks/useAuth";
import { Edit, Trash, Phone  } from "lucide-react";
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

/**
 * Individual ad card component displaying ad information and action buttons.
 * Shows different controls based on user permissions and ownership.
 */
type Props = {
    ad: Ad;
    onChoose?: (id: number) => void;
    onEdit?: (id: number) => void;
    onDelete?: (id: number) => void;
    ownerView?: boolean; // true when showing "My Items"
};

const currency = new Intl.NumberFormat("en", { style: "currency", currency: "EUR" });

export default function AdRowCard({ ad, onChoose, onEdit, onDelete, ownerView }: Props) {
    const { isAuthorized } = useAuth();
    const categoryLabel = ad.category;
    const cityLabel = ad.city;
    const conditionLabel = ad.condition;
    const hasImage = !!ad.imageUrl && ad.imageUrl.trim() !== "";

    const isAdmin = isAuthorized("ADMIN");
    const canManage = isAdmin || !!ownerView;

    return (
        <div className="w-full h-48 flex gap-4 p-6 rounded-2xl border bg-white">
            {/* Image */}
            <div className="w-1/4 h-full flex items-center justify-center">
                {hasImage ? (
                    <img
                        src={ad.imageUrl}
                        alt={ad.title}
                        className="max-w-full max-h-full object-contain"
                    />
                ) : (
                    <div className="w-full h-full bg-gray-100 flex items-center justify-center rounded-lg">
                        <span className="text-gray-400 text-sm">No Image</span>
                    </div>
                )}
            </div>

            {/* Main */}
            <div className="w-2/5 h-full flex flex-col">
                <div className="flex-shrink-0 space-y-1">
                    <h3 className="text-lg font-semibold">{ad.title}</h3>
                    <div className="text-sm text-muted-foreground">
                        {[categoryLabel, cityLabel, conditionLabel].filter(Boolean).join(" • ")}
                    </div>
                    <div className="text-lg font-bold">{currency.format(ad.price ?? 0)}</div>
                    {ad.isAvailable !== undefined && (
                        <div className="text-sm">{ad.isAvailable ? "Available" : "Unavailable"}</div>
                    )}
                </div>

                <div className="flex-1 min-h-0 mt-2 overflow-hidden">
                    {ad.description && (
                        <div className="text-sm text-muted-foreground leading-5">
                            <p>{ad.description}</p>
                        </div>
                    )}
                </div>
            </div>

            {/* Right */}
            <div className="w-1/3 h-full flex flex-col justify-between">
                <div className="flex-shrink-0">
                    <div className="flex align-text-bottom text-xs text-gray-500  border-b py-2">
                        <div className="mr-1" >Posted by:</div>
                        <div className="mr-1">{ad.ownerName || "Unknown"}</div> < Phone size={12} />
                        {ad.ownerPhone && (
                            <div className="ml-1">{ad.ownerPhone}</div>
                        )}
                    </div>
                </div>

                <div className="flex-shrink-0 space-y-2">
                    <Button
                        variant="secondary"
                        size="small"
                        onClick={() => onChoose?.(ad.id)}
                        className="w-full"
                    >
                        View Details
                    </Button>

                    <div className="flex gap-2">
                        {canManage && (
                            <Button
                                variant="neutral"
                                size="small"
                                onClick={() => onEdit?.(ad.id)}
                                className="flex items-center justify-center gap-1 flex-1 hover:bg-gray-100 text-center"
                            >
                                <Edit className="w-3 h-3 " />
                                <span>Edit</span>
                            </Button>
                        )}

                        {canManage && (
                            <AlertDialog>
                                <AlertDialogTrigger asChild>
                                    <Button
                                        variant="neutral"
                                        size="small"
                                        className="flex items-center justify-center gap-1 flex-1 text-gfa-error hover:bg-red-50 border-red-300 flex-1"
                                    >
                                        <Trash className="w-3 h-3" />
                                        <span>Delete</span>
                                    </Button>
                                </AlertDialogTrigger>
                                <AlertDialogContent>
                                    <AlertDialogHeader>
                                        <AlertDialogTitle>Delete Ad</AlertDialogTitle>
                                        <AlertDialogDescription>
                                            Are you sure you want to delete "{ad.title}"?
                                            This action cannot be undone and will permanently remove the ad from the marketplace.
                                        </AlertDialogDescription>
                                    </AlertDialogHeader>
                                    <AlertDialogFooter>
                                        <AlertDialogCancel>Cancel</AlertDialogCancel>
                                        <AlertDialogAction
                                            onClick={() => onDelete?.(ad.id)}
                                            className="bg-red-600 hover:bg-red-700"
                                        >
                                            Delete
                                        </AlertDialogAction>
                                    </AlertDialogFooter>
                                </AlertDialogContent>
                            </AlertDialog>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}