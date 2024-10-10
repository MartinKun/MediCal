import { create } from "zustand";
import { createLoaderSlice } from "./loaderSlice";

export const useBoundStore = create<LoaderSliceI>()((...a) => ({
  ...createLoaderSlice(...a),
}));
