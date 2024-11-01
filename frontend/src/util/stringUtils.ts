export const capitalizeWords = (str: string | undefined) => {
    if (!str) return "";
    return str
        .split(" ")
        .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
        .join(" ");
};