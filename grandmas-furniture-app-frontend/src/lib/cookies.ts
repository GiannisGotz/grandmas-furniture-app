import Cookies from "js-cookie"

/**
 * Cookie management utilities for authentication tokens.
 * Handles setting, getting, and deleting cookies with proper options.
 */
export function setCookie(
    name: string,
    value: string,
    options?: Cookies.CookieAttributes
){
    Cookies.set(name, value, options);
}

export function getCookie(name: string){
    return Cookies.get(name);
}

export function deleteCookie(name: string, options?: Cookies.CookieAttributes){
    Cookies.remove(name, options);
}

