import { useEffect, useState } from "react";
import { useNavigate, useParams, useLocation } from "react-router";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { toast } from "sonner";
import { useAuth } from "@/hooks/useAuth";
import { getAd, adSchema, createAdMultipart, updateAdMultipart } from "@/api/ads";
import grandaflowers from "@/assets/grandmaflowers.svg";
import Label from "@/components/Label";
import Input from "@/components/Input";
import Button from "@/components/Button";
import { CATEGORIES, CITIES, CONDITIONS } from "@/config/constants";

/**
 * Form page for creating, editing, and viewing furniture ads.
 * Handles image uploads and form validation using React Hook Form + Zod.
 */
const adFormSchema = adSchema
  .pick({
    title: true,
    condition: true,
    price: true,
    isAvailable: true,
    description: true
  })
  .extend({
    categoryName: z.string().min(1, "Category is required"),
    cityName: z.string().min(1, "City is required"),
  });

type AdFormData = z.infer<typeof adFormSchema>;

const AdPage = () => {
    const navigate = useNavigate();
    const { id } = useParams();
    const location = useLocation();
    const { accessToken } = useAuth();
    const isCreating = location.pathname.includes("/new");
    const isEditing = location.pathname.includes("/edit");
    const isViewing = !isCreating && !isEditing;
    const [loading, setLoading] = useState(false);

    const {
        register,
        handleSubmit,
        reset,
        setValue,
        formState: { errors, isSubmitting },
    } = useForm<AdFormData>({
        resolver: zodResolver(adFormSchema),
        defaultValues: {
            isAvailable: true
        }
    });

    const [imageFile, setImageFile] = useState<File | null>(null);
    const [imagePreview, setImagePreview] = useState<string | null>(null);

    useEffect(() => {
        if (isCreating) {
            document.title = "Create New Ad";
        } else if (isEditing) {
            document.title = "Edit Ad";
        } else {
            document.title = "View Ad";
        }
    }, [isCreating, isEditing]);

    // Load ad data for editing or viewing
    useEffect(() => {
        const loadAdData = async () => {
            if ((isEditing || isViewing) && id && accessToken) {
                try {
                    setLoading(true);
                    const adData = await getAd(Number(id), { token: accessToken ?? undefined });

                    // Populate form with ad data
                    setValue("title", adData.title);
                    setValue("categoryName", adData.category);
                    setValue("cityName", adData.city);
                    setValue("condition", adData.condition as "EXCELLENT" | "GOOD" | "AGE_WORN" | "DAMAGED");
                    setValue("price", adData.price);
                    setValue("description", adData.description || "");
                    setValue("isAvailable", adData.isAvailable ?? true);

                } catch (err) {
                    toast.error(err instanceof Error ? err.message : "Failed to load ad data");
                    navigate("/ads");
                } finally {
                    setLoading(false);
                }
            }
        };

        loadAdData();
    }, [id, isEditing, isViewing, setValue, navigate, accessToken]);

    const onSubmit = async (data: AdFormData) => {
        try {
            const payload = {
                title: data.title,
                categoryName: data.categoryName,
                cityName: data.cityName,
                condition: data.condition,
                price: data.price,
                isAvailable: data.isAvailable,
                description: data.description,
                image: imageFile
            };
            if (isEditing && id) {
                await updateAdMultipart(Number(id), payload, { token: accessToken ?? undefined });
                toast.success("Ad updated successfully");
            } else {
                await createAdMultipart(payload, { token: accessToken ?? undefined });
                toast.success("Ad created successfully");
            }
            navigate("/ads");
        } catch (err) {
            toast.error(err instanceof Error ? err.message : "Operation failed");
        }
    };

    if (loading) {
        return (
            <div className="max-w-sm mx-auto p-8 text-center mt-24">
                <div>Loading ad data...</div>
            </div>
        );
    }

    return (
        <form
            onSubmit={handleSubmit(onSubmit)}
            className="max-w-sm mx-auto p-8 space-y-4 border rounded-2xl mt-24 shadow-gfa-tertiary shadow-lg"
        >
            <div>
                <div className="max-w-sm flex items-center">
                    <img
                        src={grandaflowers}
                        style={{width: '120px', height: '95px'}}
                        alt="Grandma with flowers"
                    />
                    <h1 className="text-2xl">
                        {isCreating ? "Create New Ad" : isEditing ? "Edit Ad" : "View Ad"}
                    </h1>
                </div>

                <div className="mt-4 space-y-4">
                    <div>
                        <Label htmlFor="title" className="mb-1" required>Title</Label>
                        <Input
                            id="title"
                            autoFocus
                            {...register("title")}
                            disabled={isSubmitting}
                        />
                        {errors.title && (
                            <div className="text-gfa-error text-center mt-2">{errors.title.message}</div>
                        )}
                    </div>

                    <div>
                        <Label htmlFor="categoryName" className="mb-1" required>Category</Label>
                        <select
                            id="categoryName"
                            {...register("categoryName")}
                            disabled={isSubmitting}
                            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-gfa-primary focus:border-transparent"
                        >
                            <option value="">Select a category</option>
                            {CATEGORIES.map((category) => (
                                <option key={category} value={category}>
                                    {category}
                                </option>
                            ))}
                        </select>
                        {errors.categoryName && (
                            <div className="text-gfa-error text-center mt-2">{errors.categoryName.message}</div>
                        )}
                    </div>

                    <div>
                        <Label htmlFor="cityName" className="mb-1" required>City</Label>
                        <select
                            id="cityName"
                            {...register("cityName")}
                            disabled={isSubmitting}
                            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-gfa-primary focus:border-transparent"
                        >
                            <option value="">Select a city</option>
                            {CITIES.map((city) => (
                                <option key={city} value={city}>
                                    {city}
                                </option>
                            ))}
                        </select>
                        {errors.cityName && (
                            <div className="text-gfa-error text-center mt-2">{errors.cityName.message}</div>
                        )}
                    </div>

                    <div>
                        <Label htmlFor="condition" className="mb-1" required>Condition</Label>
                        <select
                            id="condition"
                            {...register("condition")}
                            disabled={isSubmitting}
                            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-gfa-primary focus:border-transparent"
                        >
                            <option value="">Select condition</option>
                            {CONDITIONS.map((condition) => (
                                <option key={condition.value} value={condition.value}>
                                    {condition.label}
                                </option>
                            ))}
                        </select>
                        {errors.condition && (
                            <div className="text-gfa-error text-center mt-2">{errors.condition.message}</div>
                        )}
                    </div>

                    <div>
                        <Label htmlFor="price" className="mb-1" required>Price (€)</Label>
                        <Input
                            id="price"
                            type="number"
                            step="0.01"
                            min="0"
                            {...register("price")}
                            disabled={isSubmitting}
                        />
                        {errors.price && (
                            <div className="text-gfa-error text-center mt-2">{errors.price.message}</div>
                        )}
                    </div>

                    <div>
                        <Label htmlFor="description" className="mb-1" required>Description</Label>
                        <textarea
                            id="description"
                            rows={4}
                            {...register("description")}

                            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-gfa-primary focus:border-transparent resize-vertical"
                        />
                        {errors.description && (
                            <div className="text-gfa-error text-center mt-2">{errors.description.message}</div>
                        )}
                    </div>

                    <div>
                        <Label htmlFor="image" className="mb-1">Image (optional)</Label>
                        <input
                            id="image"
                            type="file"
                            accept="image/*"
                            disabled={isSubmitting || isViewing}
                            onChange={(e) => {
                                const f = e.target.files?.[0] ?? null;
                                setImageFile(f);
                                setImagePreview(f ? URL.createObjectURL(f) : imagePreview);
                            }}
                            className="block w-full text-sm text-gray-700 file:mr-4 file:py-2 file:px-4 file:rounded-md file:border-0 file:text-sm file:font-semibold file:bg-gray-100 hover:file:bg-gray-200"
                        />
                        {imagePreview && (
                            <div className="mt-2">
                                <img src={imagePreview} alt="Selected preview" className="max-h-40 object-contain rounded-md border" />
                            </div>
                        )}
                    </div>

                    <div className="flex items-center">
                        <input
                            id="isAvailable"
                            type="checkbox"
                            {...register("isAvailable")}
                            disabled={isSubmitting}
                            readOnly={isViewing}
                            className="mr-2"
                        />
                        <Label htmlFor="isAvailable">Available for sale</Label>
                    </div>

                    {!isViewing && (
                        <>
                            <div className="flex justify-center mt-8">
                                <Button
                                    variant="primary"
                                    size="medium"
                                    type="submit"
                                    className="w-4/4"
                                    disabled={isSubmitting}
                                >
                                    {isSubmitting ? "Saving..." : (isEditing ? "Update Ad" : "Create Ad")}
                                </Button>
                            </div>

                            <div className="flex justify-center mt-1">
                                <Button
                                    variant="secondary"
                                    size="medium"
                                    type="button"
                                    className="w-4/4"
                                    disabled={isSubmitting}
                                    onClick={() => { reset(); setImageFile(null); setImagePreview(null); }}
                                >
                                    {isSubmitting ? "..." : "Clear All"}
                                </Button>
                            </div>
                        </>
                    )}

                    {isViewing && (
                        <div className="flex justify-center mt-8 gap-2">
                            <Button
                                variant="primary"
                                size="medium"
                                type="button"
                                className="flex-1"
                                onClick={() => navigate(`/ads/${id}/edit`)}
                            >
                                Edit Ad
                            </Button>
                            <Button
                                variant="secondary"
                                size="medium"
                                type="button"
                                className="flex-1"
                                onClick={() => navigate("/ads")}
                            >
                                Back to List
                            </Button>
                        </div>
                    )}

                    <div className="flex justify-center mt-2">
                        <Button
                            variant="neutral"
                            size="small"
                            type="button"
                            className="w-3/4"
                            onClick={() => navigate("/ads")}
                        >
                            ← Back to Dashboard
                        </Button>
                    </div>
                </div>
            </div>
        </form>
    );
};

export default AdPage;