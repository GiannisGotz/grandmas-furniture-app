import type { LabelHTMLAttributes, ReactNode } from "react";
import { twMerge } from "tailwind-merge";
import clsx from "clsx";

export interface LabelProps extends LabelHTMLAttributes<HTMLLabelElement> {
	children?: ReactNode;
	htmlFor?: string;
	required?: boolean;
	size?: "sm" | "md" | "lg";
	variant?: "default" | "muted" | "error";
	disabled?: boolean;
	className?: string;
}

const sizeClasses: Record<NonNullable<LabelProps["size"]>, string> = {
	sm: "text-xs",
	md: "text-sm",
	lg: "text-base"
};

const variantClasses: Record<NonNullable<LabelProps["variant"]>, string> = {
	default: "text-gfa-text-dark",
	muted: "text-muted-foreground",
	error: "text-gfa-error"
};

const disabledClasses = "opacity-60 cursor-not-allowed";

const Label = ({
	children,
	htmlFor,
	required = false,
	size = "md",
	variant = "default",
	disabled = false,
	className,
	...rest
}: LabelProps) => {
	const classes = twMerge(
		clsx(
			"block font-medium select-none",
			sizeClasses[size],
			variantClasses[variant],
			disabled && disabledClasses,
			className
		)
	);

	return (
		<label htmlFor={htmlFor} className={classes} aria-disabled={disabled} {...rest}>
			{children}
			{required && <span className="ml-0.5 text-gfa-accent" title="Required">*</span>}
		</label>
	);
};

export default Label;
 


