import {useEffect, useState} from "react";
import { useNavigate } from "react-router";
import AdsTable from "@/components/ads/AdsTable";
import { deleteAd, type Ad, searchAdsPaginated } from "@/api/ads";
import { useAuth } from "@/hooks/useAuth";
import {CirclePlus} from "lucide-react";
import Button from "@/components/Button.tsx";
import grandmacleaningtable from "../assets/grandmacleaningtable.svg";
import grandmamirror from "../assets/grandmamirror.svg";
import { toast } from "sonner";
import { CATEGORIES, CITIES, CONDITIONS } from "@/config/constants";

/**
 * Dashboard page for managing and searching furniture ads.
 * Supports filtering, pagination, and CRUD operations on ads.
 */
const DashboardAdsPage = () => {
    const navigate = useNavigate();
    const [page, setPage] = useState(1);
    const size = 10;
    const { accessToken } = useAuth();
    const [items, setItems] = useState<Ad[]>([]);
    const [total, setTotal] = useState(0);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [showMyAds, setShowMyAds] = useState(false);
    const [showSearch, setShowSearch] = useState(false);
    const [search, setSearch] = useState({ 
        title: "", 
        categoryName: "", 
        cityName: "", 
        minPrice: "", 
        maxPrice: "", 
        onlyAvailable: false 
    });

    useEffect(()=>{
        document.title = "Grandma's Furniture"
    }, []);

    useEffect(() => {
        let cancelled = false;
        setLoading(true);
        setError(null);

        const run = async () => {
            try {
                // Always use server-side search for consistent behavior
                const searchFilters = {
                    title: search.title || undefined,
                    categoryName: search.categoryName || undefined,
                    cityName: search.cityName || undefined,
                    minPrice: search.minPrice ? Number(search.minPrice) : undefined,
                    maxPrice: search.maxPrice ? Number(search.maxPrice) : undefined,
                    isAvailable: search.onlyAvailable ? true : undefined,
                    myAds: showMyAds ? true : undefined,
                    page,
                    pageSize: size,
                    sortBy: "id",
                    sortDirection: "asc" as const,
                };

                // Add user filter if showing "My Items"
                if (showMyAds) {
                    // For "My Items", we'll filter by the current user
                    // This will be handled by the backend based on authentication
                    // The backend should automatically filter by the authenticated user
                }

                console.log('Search filters:', searchFilters);
                console.log('Access token:', accessToken);
                
                const res = await searchAdsPaginated(searchFilters, { 
                    token: accessToken ?? undefined 
                });
                
                console.log('Search response:', res);
                
                if (cancelled) return;
                setItems(res.items);
                setTotal(res.total);
            } catch (err) {
                if (cancelled) return;
                console.error("Search error:", err);
                
                // Fallback: try to fetch all ads without filters
                try {
                    console.log("Trying fallback: fetch all ads");
                    const fallbackRes = await searchAdsPaginated({
                        page,
                        pageSize: size,
                        sortBy: "id",
                        sortDirection: "asc" as const,
                    }, { 
                        token: accessToken ?? undefined 
                    });
                    
                    if (cancelled) return;
                    setItems(fallbackRes.items);
                    setTotal(fallbackRes.total);
                    setError(null);
                } catch (fallbackErr) {
                    if (cancelled) return;
                    setError(fallbackErr instanceof Error ? fallbackErr.message : "Failed to load ads");
                    console.error("Fallback also failed:", fallbackErr);
                }
            } finally {
                if (cancelled) return;
                setLoading(false);
            }
        };

        run();
        return () => { cancelled = true; };
    }, [page, size, accessToken, showMyAds, search]);

    const handleDeleteAd = async (adId: number) => {
        try {
            await deleteAd(adId, { token: accessToken ?? undefined });
            setItems(items.filter(ad => ad.id !== adId));
            toast.success("Ad deleted successfully");
        } catch (err) {
            toast.error(err instanceof Error ? err.message : "Failed to delete ad");
        }
    };

    return (
        <div className="max-w-2/3 mx-auto p-8 space-y-4 mt-2">
            <div className="flex gap-2">
                <Button className="w-1/3 hover:bg-gray-50"
                        variant="neutral"
                        size="small"
                        onClick={() => {
                            setShowMyAds(!showMyAds);
                            setPage(1);
                        }}>
                    {showMyAds ? "All Items" : "My Items"}
                </Button>
                <Button className="w-1/3 hover:bg-gray-50"
                        variant="neutral"
                        size="small"
                        onClick={() => setShowSearch((v) => !v)}>
                    Search
                </Button>
            </div>
            
            {showSearch && (
                <div className="bg-gray-50 p-4 rounded-lg mb-4">
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-3">
                        <input
                            type="text"
                            placeholder="Search by title..."
                            value={search.title}
                            onChange={(e) => setSearch(prev => ({ ...prev, title: e.target.value }))}
                            className="px-3 py-2 border rounded"
                        />
                        <select
                            value={search.categoryName}
                            onChange={(e) => setSearch(prev => ({ ...prev, categoryName: e.target.value }))}
                            className="px-3 py-2 border rounded"
                        >
                            <option value="">All Categories</option>
                            {CATEGORIES.map(category => (
                                <option key={category} value={category}>{category}</option>
                            ))}
                        </select>
                        <select
                            value={search.cityName}
                            onChange={(e) => setSearch(prev => ({ ...prev, cityName: e.target.value }))}
                            className="px-3 py-2 border rounded"
                        >
                            <option value="">All Cities</option>
                            {CITIES.map(city => (
                                <option key={city} value={city}>{city}</option>
                            ))}
                        </select>
                        <input
                            type="number"
                            placeholder="Min price..."
                            value={search.minPrice}
                            onChange={(e) => setSearch(prev => ({ ...prev, minPrice: e.target.value }))}
                            className="px-3 py-2 border rounded"
                        />
                        <input
                            type="number"
                            placeholder="Max price..."
                            value={search.maxPrice}
                            onChange={(e) => setSearch(prev => ({ ...prev, maxPrice: e.target.value }))}
                            className="px-3 py-2 border rounded"
                        />
                    </div>
                    <div className="flex items-center mt-3">
                        <label className="flex items-center">
                            <input
                                type="checkbox"
                                checked={search.onlyAvailable}
                                onChange={(e) => setSearch(prev => ({ ...prev, onlyAvailable: e.target.checked }))}
                                className="mr-2"
                            />
                            Show only available items
                        </label>
                    </div>
                </div>
            )}
            
            <div className="flex justify-between items-center mb-6">
                <img src={grandmacleaningtable}
                     style={{width: '100px', height: '80px'}}
                     alt="Grandma is cleaning table."/>
                <h1 className="text-2xl font-bold">Furniture Marketplace</h1>
                <Button
                    className="flex items-center gap-2"
                    variant="secondary"
                    size="medium"
                    onClick={() => navigate("/ads/new")}>
                    <CirclePlus size={24}/>
                    New
                </Button>
            </div>

            {loading && <div>Loading...</div>}
            {error && <div className="text-gfa-error">{error}</div>}
            {!loading && !error && items.length === 0 && (
                <div className="flex flex-col items-center justify-center py-12 text-center">
                    <img 
                        src={grandmamirror}
                        alt="No furniture found" 
                        className="w-48 h-48 mb-6 opacity-60"
                    />
                    <h3 className="text-xl font-semibold text-gray-600 mb-2">
                        {showMyAds ? "No Items Posted Yet" : "No Furniture Found"}
                    </h3>
                    <p className="text-gray-500">
                        {showMyAds 
                            ? "Start by creating your first furniture listing!" 
                            : "Try adjusting your search criteria."}
                    </p>
                </div>
            )}
            {!loading && !error && items.length > 0 && (
                <AdsTable
                    items={items}
                    total={total}
                    page={page}
                    size={size}
                    onPageChange={setPage}
                    onChoose={(id) => navigate(`/ads/${id}`)}
                    onEdit={(id) => navigate(`/ads/${id}/edit`)}
                    onDelete={handleDeleteAd}
                    ownerView={showMyAds}
                />
            )}
        </div>
    );
};

export default DashboardAdsPage;