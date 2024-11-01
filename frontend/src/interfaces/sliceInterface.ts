import { ToastI } from "./toastInterface";

export interface LoaderSliceI {
  isShowLoader: boolean;
  showLoader: () => void;
  hideLoader: () => void;
}

export interface ToastSliceI {
  toast: ToastI
  showError: (message: string) => void,
  showWarning: (message: string) => void,
  showSuccess: (message: string) => void,
  hideToast: () => void;
}

export interface UserI {
  token: string | null;
  userData: {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    type: "DOCTOR" | "PATIENT";
  } | null;
}

export interface UserSliceI {
  user: UserI | null;
  setUser: (user: UserI) => void;
}
