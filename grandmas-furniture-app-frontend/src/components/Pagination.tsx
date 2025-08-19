import Button from "../components/Button.tsx";

interface PaginationProps {
    page: number;
    size: number;
    total: number;
    onPageChange: (page: number) => void;
}

const Pagination = ({ page, size, total, onPageChange }: PaginationProps) => {
    const totalPages = Math.max(1, Math.ceil(total / size));
    const start = Math.max(1, page - 2);
    const end = Math.min(totalPages, page + 2);
    const pages = [] as number[];
    for (let p = start; p <= end; p++) pages.push(p);

    return (
        <div className="flex items-center justify-center gap-2 p-4">
            <Button
                variant="neutral"
                className="px-3 py-1 rounded border disabled:opacity-50"
                size="small"
                disabled={page <= 1}
                onClick={() => onPageChange(Math.max(1, page - 10))}
            >
                -10
            </Button>
            <Button
                variant="neutral"
                className="px-3 py-1 rounded border disabled:opacity-50"
                size="small"
                disabled={page <= 1}
                onClick={() => onPageChange(page - 1)}
            >
                Prev
            </Button>

            {pages.map((p) => (
                <Button
                    variant="neutral"
                    size="small"
                    key={p}
                    className={`px-3 py-1 rounded border ${
                        p === page
                            ? "bg-black text-white"
                            : "hover:bg-gray-100"
                    }`}
                    onClick={() => onPageChange(p)}
                >
                    {p}
                </Button>
            ))}

            <Button
                variant="neutral"
                className="px-3 py-1 rounded border disabled:opacity-50"
                size="small"
                disabled={page >= totalPages}
                onClick={() => onPageChange(page + 1)}
            >
                Next
            </Button>
            <Button
                variant="neutral"
                className="px-3 py-1 rounded border disabled:opacity-50"
                size="small"
                disabled={page + 10 > totalPages}
                onClick={() => onPageChange(Math.min(totalPages, page + 10))}
            >
                +10
            </Button>
        </div>
    );
};

export default Pagination;
