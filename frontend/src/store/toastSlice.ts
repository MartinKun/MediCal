import { ToastSliceI } from "@/interfaces/sliceInterface";
import { StateCreator } from "zustand";

export const createToastSlice: StateCreator<ToastSliceI> = (set) => ({
    toast: {
        isShowing: false,
        message: "",
        status: "error"
    },
    showError: (message: string) => set({toast: {
        isShowing: true,
        message: message,
        status: "error"
    }}),
    showWarning: (message: string) => set({toast: {
        isShowing: true,
        message: message,
        status: "warning"
    }}),
    showSuccess: (message: string) => set({toast: {
        isShowing: true,
        message: message,
        status: "success"
    }}),
    hideToast: () => set({ toast: {
        isShowing: false,
        message: "",
        status: "error"
    } }),
  });