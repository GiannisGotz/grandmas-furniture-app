import type { InputHTMLAttributes } from "react";
import { forwardRef } from "react";
import { twMerge } from "tailwind-merge";
import clsx from "clsx";

/**
 * Reusable input component with size variants and error states.
 * Supports all standard HTML input attributes with consistent styling.
 */
export interface InputProps extends Omit<InputHTMLAttributes<HTMLInputElement>, "size"> {
    size?: "sm" | "md" | "lg";
    variant?: "default" | "error";
}

const sizeClasses: Record<NonNullable<InputProps["size"]>, string> = {
    sm: "px-3 py-2 text-sm",
    md: "px-4 py-2.5 text-base",
    lg: "px-4 py-3 text-lg"
};

const variantClasses: Record<NonNullable<InputProps["variant"]>, string> = {
    default: "border-input",
    error: "border-[var(--color-gfa-error)] focus:ring-[var(--color-gfa-error)]/40 focus:border-[var(--color-gfa-error)]"
};

const disabledClasses = "opacity-60 cursor-not-allowed bg-muted";

const Input = forwardRef<HTMLInputElement, InputProps>(function Input(
    { className, size = "md", variant = "default", disabled, ...props },
    ref
) {
    const base = "w-full rounded-lg border bg-background text-foreground placeholder-muted-foreground transition-colors focus:outline-none focus:ring-2 focus:ring-[var(--color-gfa-primary)]/40 focus:border-[var(--color-gfa-primary)]";

    const classes = twMerge(
        clsx(base, sizeClasses[size], variantClasses[variant], disabled && disabledClasses, className)
    );

    return <input ref={ref} className={classes} disabled={disabled} {...props} />;
});

export default Input;


