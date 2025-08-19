import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";
import Button from "@/components/Button";
import { useAuth } from "@/hooks/useAuth";
import { getAd, type Ad } from "@/api/ads";

const currency = new Intl.NumberFormat("en", { style: "currency", currency: "EUR" });

const AdDetailedPage = () => {
    const navigate = useNavigate();
    const { id } = useParams();
    const { accessToken } = useAuth();

    const [ad, setAd] = useState<Ad | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        let cancelled = false;
        const load = async () => {
            try {
                setLoading(true);
                const data = await getAd(Number(id), { token: accessToken ?? undefined });
                if (!cancelled) {
                    setAd(data);
                    document.title = `Ad Details • ${data.title}`;
                }
            } catch (e) {
                if (!cancelled) setError(e instanceof Error ? e.message : "Failed to load ad");
            } finally {
                if (!cancelled) setLoading(false);
            }
        };
        if (id) load();
        return () => { cancelled = true; };
    }, [id, accessToken]);

    if (loading) {
        return (
            <div className="max-w-sm mx-auto p-8 text-center mt-24">
                <div>Loading ad...</div>
            </div>
        );
    }

    if (error || !ad) {
        return (
            <div className="max-w-sm mx-auto p-8 text-center mt-24">
                <div className="text-gfa-error">{error ?? "Ad not found"}</div>
                <div className="flex justify-center mt-4">
                    <Button variant="neutral" size="small" className="w-3/4" onClick={() => navigate("/ads")}>
                        ← Back to Dashboard
                    </Button>
                </div>
            </div>
        );
    }

    return (
        <div className="max-w-5xl mx-auto p-6 mt-6 border rounded-2xl  bg-white">
            <div className="w-full mb-6 rounded-xl overflow-hidden bg-gray-100 flex items-center justify-center">
                {ad.imageUrl ? (
                    <img src={ad.imageUrl} alt={ad.title} className="w-full object-contain" />
                ) : (
                    <div className="w-full min-h-[360px] flex items-center justify-center text-gray-400">
                        No Image
                    </div>
                )}
            </div>

            <div className="flex flex-col md:flex-row md:items-start md:justify-between gap-4">
                <div className="flex-1">
                    <h1 className="text-3xl font-bold">{ad.title}</h1>
                    <div className="mt-2 text-sm text-muted-foreground flex flex-wrap gap-2">
                        {ad.category && <span className="px-2 py-1 bg-gray-100 rounded-md">{ad.category}</span>}
                        {ad.city && <span className="px-2 py-1 bg-gray-100 rounded-md">{ad.city}</span>}
                        {ad.condition && <span className="px-2 py-1 bg-gray-100 rounded-md">{ad.condition}</span>}
                        {ad.isAvailable !== undefined && (
                            <span className={`px-2 py-1 rounded-md ${ad.isAvailable ? "bg-green-100 text-green-700" : "bg-red-100 text-red-700"}`}>
								{ad.isAvailable ? "Available" : "Unavailable"}
							</span>
                        )}
                    </div>
                </div>
                <div className="flex-shrink-0">
                    <div className="text-2xl font-bold">{currency.format(ad.price ?? 0)}</div>
                </div>
            </div>

            {ad.description && (
                <div className="mt-6">
                    <h2 className="text-lg font-semibold mb-2">Description</h2>
                    <p className="text-sm leading-6 text-muted-foreground">{ad.description}</p>
                </div>
            )}

            {(ad.ownerName || ad.ownerPhone) && (
                <div className="mt-6">
                    <h2 className="text-lg font-semibold mb-2">Seller</h2>
                    <div className="text-sm">
                        {ad.ownerName && <div>{ad.ownerName}</div>}
                        {ad.ownerPhone && <div className="text-gray-600">{ad.ownerPhone}</div>}
                    </div>
                </div>
            )}

            <div className="flex justify-center mt-8">
                <Button variant="neutral" size="small" className="w-3/4" onClick={() => navigate("/ads")}>
                    ← Back to Dashboard
                </Button>
            </div>
        </div>
    );
};

export default AdDetailedPage;