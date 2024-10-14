import { create } from "zustand";
import { createLoaderSlice } from "./loaderSlice";
import { createUserSlice } from "./userSlice";
import { createJSONStorage, persist } from "zustand/middleware";

export const useBoundStore = create<LoaderSliceI & UserSliceI>()((...a) => ({
  ...createLoaderSlice(...a),

  ...persist(createUserSlice, {
    name: "user-store",
    partialize: (state) => ({ user: state.user }),
    storage: createJSONStorage(() => sessionStorage),
  })(...a),
}));
