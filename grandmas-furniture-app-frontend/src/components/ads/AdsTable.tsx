import type { Ad } from "@/api/ads";
import AdRowCard from "./AdRowCard";
import Pagination from "@/components/Pagination";

/**
 * Table component for displaying ads with pagination and action handlers.
 * Supports view, edit, and delete operations for each ad.
 */
type Props = {
    items: Ad[];
    total: number;
    page: number;
    size: number;
    onPageChange: (page: number) => void;
    onChoose?: (id: number) => void;
    onEdit?: (id: number) => void;
    onDelete?: (id: number) => void;
    ownerView?: boolean; // true when showing "My Items"
};

export default function AdsTable({ items, total, page, size, onPageChange, onChoose, onEdit, onDelete, ownerView }: Props) {
    return (
        <div className="space-y-4">
            <Pagination page={page} size={size} total={total} onPageChange={onPageChange} />
            <div className="space-y-4">
                {items.map((ad) => (
                    <AdRowCard key={ad.id} ad={ad} onChoose={onChoose} onEdit={onEdit} onDelete={onDelete} ownerView={ownerView} />
                ))}
            </div>
            <Pagination page={page} size={size} total={total} onPageChange={onPageChange} />
        </div>
    );
}