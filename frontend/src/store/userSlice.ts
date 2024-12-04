import { UserSliceI } from "@/interfaces/sliceInterface";
import { StateCreator } from "zustand";

export const createUserSlice: StateCreator<UserSliceI> = (set) => ({
  user: null,
  setUser: (user) => set(() => ({ user: user })),
  logout: () => set(() => ({ user: null }))
});
