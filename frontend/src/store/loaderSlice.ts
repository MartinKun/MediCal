import { StateCreator } from "zustand";

export const createLoaderSlice: StateCreator<LoaderSliceI> = (set) => ({
  isShowLoader: false,
  showLoader: () => set({ isShowLoader: true }),
  hideLoader: () => set({ isShowLoader: false }),
});
