import { StateCreator } from "zustand";

export const createUserSlice: StateCreator<UserSliceI> = (set) => ({
  user: null,
  setUser: (user) => set(() => ({ user: user })),
});
