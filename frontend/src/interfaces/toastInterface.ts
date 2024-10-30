export interface ToastI {
    isShowing: boolean,
    message: string,
    status: "error" | "warning" | "success",
}