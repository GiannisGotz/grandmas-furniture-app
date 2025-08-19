import { z } from "zod";

/**
 * API functions and types for furniture ad management.
 * Handles CRUD operations, search, and image uploads for ads.
 */
export const adSchema = z.object({
    id: z.coerce.number().int(),
    title: z.string().min(2, "Title is required").max(30, "Title must be at most 30 characters"),
    category: z.string().min(1, "Category is required"),
    city: z.string().min(1, "City is required"),
    condition: z.string().min(1, "Condition is required"),
    price: z.coerce.number().nonnegative("Must be a non-negative number"),
    isAvailable: z.boolean(),
    description: z.string().min(2,"Description is required").max(100,"Description must be at most 100 characters"),
    imagePath: z.string().optional(),
    userFirstName: z.string(),
    userLastName: z.string(),
    userPhone: z.string(),
});

export const adFormSchema = adSchema.omit({ id: true });

/// Multipart upsert payload (matches backend @RequestPart("ad"))
export type AdUpsertPayload = {
    title: string;
    categoryName: string;
    cityName: string;
    condition: string;
    price: number;
    isAvailable: boolean;
    description: string;
    image?: File | null;
};

// PUT /ads/:id with multipart form-data (ad JSON + optional image)
export async function updateAdMultipart(
    id: number,
    data: AdUpsertPayload,
    options?: { token?: string }
): Promise<Ad> {
    const base = import.meta.env.VITE_API_URL;
    const headers: Record<string, string> = {};
    if (options?.token) headers["Authorization"] = `Bearer ${options.token}`;

    const { image, ...adJson } = data;
    const form = new FormData();
    form.append("ad", new Blob([JSON.stringify(adJson)], { type: "application/json" }));
    if (image) form.append("image", image);

    const res = await fetch(`${base}/ads/${id}`, { method: "PUT", headers, body: form });
    if (!res.ok) throw new Error("Failed to update ad");
    return mapAd(await res.json());
}

// POST /ads/save with multipart form-data (ad JSON + optional image)
export async function createAdMultipart(
    data: AdUpsertPayload,
    options?: { token?: string }
): Promise<Ad> {
    const base = import.meta.env.VITE_API_URL;
    const headers: Record<string, string> = {};
    if (options?.token) headers["Authorization"] = `Bearer ${options.token}`;

    const { image, ...adJson } = data;
    const form = new FormData();
    form.append("ad", new Blob([JSON.stringify(adJson)], { type: "application/json" }));
    if (image) form.append("image", image);

    const res = await fetch(`${base}/ads/save`, { method: "POST", headers, body: form });
    if (!res.ok) throw new Error("Failed to create ad");
    return mapAd(await res.json());
}


export async function getAvailableAds(options?: { token?: string }): Promise<Ad[]> {
      const base = import.meta.env.VITE_API_URL;
      const headers: Record<string, string> = {};
      if (options?.token) headers["Authorization"] = `Bearer ${options.token}`;
      const res = await fetch(`${base}/ads/available`, { headers });
      if (!res.ok) throw new Error("Failed to fetch available ads");
     const data = await res.json();
     return (data || []).map(mapAd);
}


export async function getAd(id: number, options?: { token?: string }): Promise<Ad> {
    const base = import.meta.env.VITE_API_URL;
    const headers: Record<string, string> = {};
    if (options?.token) headers["Authorization"] = `Bearer ${options.token}`;
    
    const res = await fetch(`${base}/ads/${id}`, { headers });
    if (!res.ok) {
        const errorText = await res.text();
        console.error('Failed to fetch ad:', res.status, errorText);
        throw new Error(`Failed to fetch ad: ${res.status}`);
    }
    const apiAd = await res.json();
    return mapAd(apiAd);
}


// Raw API shapes (mirror backend)
export type ApiCategory = { id: number; category: string }
export type ApiCity = { id: number; cityName: string }

export interface ApiAd {
    id: number;
    title: string;
    category?: ApiCategory | null;
    city?: ApiCity | null;
    condition?: string; // backend enum as string
    price: number;
    isAvailable?: boolean;
    description?: string;
    imagePath?: string | null;
    // Add user info for permissions
    userFirstName?: string;
    userLastName?: string;
    userPhone?: string;
}

export interface PageResponse<T> {
    content: T[];
    totalElements: number;
    number: number;
    size: number;
}

// UI-friendly model
export type Ad = {
    id: number;
    title: string;
    category: string;
    categoryId?: number; // Keep ID for editing
    city: string;
    cityId?: number; // Keep ID for editing
    condition: string;
    price: number;
    isAvailable?: boolean;
    description?: string;
    imageUrl?: string;
    // Add user info
    ownerName?: string;
    ownerPhone?: string;
}

export function mapAd(a: ApiAd): Ad {
    return {
        id: a.id,
        title: a.title,
        category: a.category?.category ?? "",
        categoryId: a.category?.id,
        city: a.city?.cityName ?? "",
        cityId: a.city?.id,
        condition: a.condition ?? "",
        price: Number(a.price),
        isAvailable: a.isAvailable,
        description: a.description,
        // with Vite dev proxy, the relative imagePath is resolvable directly
        imageUrl: a.imagePath || undefined,
        ownerName: a.userFirstName && a.userLastName 
            ? `${a.userFirstName} ${a.userLastName}` 
            : undefined,
        ownerPhone: a.userPhone,
    };
}

export async function fetchAdsPage(params: {
    page?: number;
    size?: number;
    sortBy?: string;
    sortDirection?: "asc" | "desc";
    myAds?: boolean;
}, options?: { token?: string }): Promise<{ items: Ad[]; total: number; page: number; size: number }>{
    const { page = 1, size = 10, sortBy = "id", sortDirection = "asc", myAds = false } = params;
    const base = import.meta.env.VITE_API_URL;
    const endpoint = myAds ? "/ads/my-ads" : "/ads";
    const url = `${base}${endpoint}?page=${page - 1}&size=${size}&sortBy=${encodeURIComponent(sortBy)}&sortDirection=${encodeURIComponent(sortDirection)}`;
    const headers: Record<string, string> = {};
    if (options?.token) headers["Authorization"] = `Bearer ${options.token}`;
    const res = await fetch(url, { headers });
    if (!res.ok) throw new Error("Failed to fetch ads");
    const data = await res.json();
    
    // Handle different response structures
    const content = data.content || data.ads || data || [];
    const total = data.totalElements || data.total || content.length;
    
    return {
        items: content.map(mapAd),
        total: total,
        page: (data.number ?? 0) + 1,
        size: data.size ?? size,
    };
}

export async function deleteAd(adId: number, options?: { token?: string }): Promise<void> {
    const headers: Record<string, string> = {};
    if (options?.token) headers["Authorization"] = `Bearer ${options.token}`;
    
    const response = await fetch(`${import.meta.env.VITE_API_URL}/ads/${adId}`, {
        method: "DELETE",
        headers
    });
    
    if (!response.ok) {
        switch (response.status) {
            case 404:
                throw new Error("Ad not found");
            case 403:
                throw new Error("Not authorized to delete this ad");
            case 401:
                throw new Error("Please log in to delete this ad");
            default:
                throw new Error("Failed to delete ad");
        }
    }
}

export async function searchAdsPaginated(filters: {
    title?: string;
    description?: string;
    categoryName?: string;
    cityName?: string;
    condition?: string;
    minPrice?: number;
    maxPrice?: number;
    isAvailable?: boolean;
    myAds?: boolean;
    page?: number;
    pageSize?: number;
    sortBy?: string;
    sortDirection?: "asc" | "desc";
}, options?: { token?: string }): Promise<{ items: Ad[]; total: number; page: number; size: number }> {
    const base = import.meta.env.VITE_API_URL;
    const headers: Record<string, string> = {};
    if (options?.token) headers["Authorization"] = `Bearer ${options.token}`;
    
    // Build query parameters
    const params = new URLSearchParams();
    if (filters.title) params.append('title', filters.title);
    if (filters.categoryName) params.append('categoryName', filters.categoryName);
    if (filters.cityName) params.append('cityName', filters.cityName);
    if (filters.condition) params.append('condition', filters.condition);
    if (filters.minPrice) params.append('minPrice', filters.minPrice.toString());
    if (filters.maxPrice) params.append('maxPrice', filters.maxPrice.toString());
    if (filters.isAvailable !== undefined) params.append('isAvailable', filters.isAvailable.toString());
    if (filters.myAds !== undefined) params.append('myAds', filters.myAds.toString());
    if (filters.page) params.append('page', ((filters.page ?? 1) - 1).toString());
    if (filters.pageSize) params.append('pageSize', filters.pageSize.toString());
    if (filters.sortBy) params.append('sortBy', filters.sortBy);
    if (filters.sortDirection) params.append('sortDirection', filters.sortDirection);
    
    const url = `${base}/ads/search/paginated?${params.toString()}`;
    
    const res = await fetch(url, { method: "GET", headers });
    if (!res.ok) throw new Error("Failed to search ads");
    
    const data = await res.json();
    
    // Use the correct field names from the backend Paginated class
    const content = data.data || data.content || data.items || data || [];
    
    const total = data.totalElements || data.total || content.length;
    return {
        items: content.map(mapAd),
        total,
        page: (data.currentPage ?? data.number ?? (filters.page ?? 1) - 1 ?? 0) + 1,
        size: data.pageSize ?? data.size ?? filters.pageSize ?? 10,
    };
}