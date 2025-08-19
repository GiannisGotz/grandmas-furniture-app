import { useEffect, useState } from "react";

/**
 * Reusable button component with multiple variants, sizes, and states.
 * Supports primary, secondary, tertiary, accent, danger, and neutral styles.
 */
interface ButtonProps {
    children: React.ReactNode;
    onClick?: () => void;
    variant?: "primary" | "secondary" | "tertiary" | "accent" | "danger" | "neutral";
    size?: "small" | "medium" | "large";
    disabled?: boolean;
    className?: string;
    type?: "button" | "submit" | "reset";
    hoverCursor?: "auto" | "default" | "pointer" | "wait" | "text" | "move" | "help" | "not-allowed" | "crosshair" | "grab" | "grabbing" | "zoom-in" | "zoom-out";
}

const Button = ({
                    children,
                    onClick,
                    variant = "primary",
                    size = "medium",
                    disabled = false,
                    className = "",
                    type = "button",
                    hoverCursor = "pointer"
                }: ButtonProps) => {
    const baseStyles = "rounded-lg font-medium transition-colors duration-200 focus:outline-none focus:ring-2 focus:ring-offset-2";

    const sizes = {
        small: "px-3 py-2 text-sm",
        medium: "px-6 py-3 text-base",
        large: "px-8 py-4 text-lg"
    };

    const variants = {
        primary: "bg-[var(--color-gfa-primary)] hover:bg-[var(--color-gfa-accent)] text-white focus:ring-[var(--color-gfa-primary)]/50",
        secondary: "bg-[var(--color-gfa-secondary)] hover:bg-[var(--color-gfa-accent)] text-[var(--color-gfa-text-dark)] focus:ring-[var(--color-gfa-secondary)]/50",
        tertiary: "bg-[var(--color-gfa-tertiary)] hover:bg-[var(--color-gfa-accent)]/90 text-white focus:ring-[var(--color-gfa-accent)]/50",
        accent: "bg-[var(--color-gfa-accent)] hover:bg-[var(--color-gfa-accent)]/90 text-white focus:ring-[var(--color-gfa-accent)]/50",
        danger: "bg-[var(--color-gfa-error)] hover:bg-red-800 text-white focus:ring-[var(--color-gfa-error)]/50" ,
        neutral: "bg-background hover:bg-background/90 text-foreground border border-black focus:ring-background/50"
    };

    const disabledStyles = "opacity-50 cursor-not-allowed";

    const hoverCursorMap: Record<NonNullable<ButtonProps["hoverCursor"]>, string> = {
        auto: "hover:cursor-auto",
        default: "hover:cursor-default",
        pointer: "hover:cursor-pointer",
        wait: "hover:cursor-wait",
        text: "hover:cursor-text",
        move: "hover:cursor-move",
        help: "hover:cursor-help",
        "not-allowed": "hover:cursor-not-allowed",
        crosshair: "hover:cursor-crosshair",
        grab: "hover:cursor-grab",
        grabbing: "hover:cursor-grabbing",
        "zoom-in": "hover:cursor-zoom-in",
        "zoom-out": "hover:cursor-zoom-out"
    };

    const hoverCursorClass = !disabled && hoverCursor ? hoverCursorMap[hoverCursor] : "";

    const buttonStyles = `${baseStyles} ${sizes[size]} ${variants[variant]} ${disabled ? disabledStyles : hoverCursorClass} ${className}`.trim();

    return (
        <button
            type={type}
            className={buttonStyles}
            onClick={disabled ? undefined : onClick}
            disabled={disabled}
        >
            {children}
        </button>
    );
};

export default Button;
export type { ButtonProps };

/**
 * Password visibility toggle button for input fields.
 * Toggles between showing and hiding password text.
 */
interface PasswordToggleButtonProps {
    inputId: string;
    ariaLabels?: { show: string; hide: string };
    className?: string;
    disabled?: boolean;
}

export const PasswordToggleButton = ({
                                        inputId,
                                        ariaLabels,
                                        className = "",
                                        disabled = false,
                                    }: PasswordToggleButtonProps) => {
    const [visible, setVisible] = useState(false);

    useEffect(() => {
        const el = document.getElementById(inputId) as HTMLInputElement | null;
        if (el) setVisible(el.type !== "password");
    }, [inputId]);

    const toggleVisibility = () => {
        const el = document.getElementById(inputId) as HTMLInputElement | null;
        if (!el) return;
        const nextVisible = el.type === "password";
        el.type = nextVisible ? "text" : "password";
        setVisible(nextVisible);
    };

    return (
        <span
            role="button"
            tabIndex={disabled ? -1 : 0}
            onClick={disabled ? undefined : toggleVisibility}
            onKeyDown={(e) => {
                if (disabled) return;
                if (e.key === "Enter" || e.key === " ") {
                    e.preventDefault();
                    toggleVisibility();
                }
            }}
            aria-label={visible ? (ariaLabels?.hide ?? "Hide password") : (ariaLabels?.show ?? "Show password")}
            aria-pressed={visible}
            className={`absolute inset-y-0 right-0 h-full flex items-center justify-center text-center px-3 text-sm text-muted-foreground hover:text-foreground select-none ${disabled ? "opacity-50 pointer-events-none" : "cursor-pointer"} ${className}`}
        >
            {visible ? "Hide" : "Show"}
        </span>
    );
};