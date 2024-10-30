import { create } from "zustand";
import { createLoaderSlice } from "./loaderSlice";
import { createUserSlice } from "./userSlice";
import { createJSONStorage, persist } from "zustand/middleware";
import { LoaderSliceI, ToastSliceI, UserSliceI } from "@/interfaces/sliceInterface";
import { createToastSlice } from "./toastSlice";

export const useBoundStore = create<LoaderSliceI & UserSliceI & ToastSliceI>()((...a) => ({
  ...createLoaderSlice(...a),
  ...createToastSlice(...a),

  ...persist(createUserSlice, {
    name: "user-store",
    partialize: (state) => ({ user: state.user }),
    storage: createJSONStorage(() => sessionStorage),
  })(...a),
}));
