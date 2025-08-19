import { clsx, type ClassValue } from "clsx"
import { twMerge } from "tailwind-merge"

/**
 * Utility function to merge Tailwind CSS classes with conflict resolution.
 * Combines clsx and twMerge for optimal class merging.
 */
export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}
