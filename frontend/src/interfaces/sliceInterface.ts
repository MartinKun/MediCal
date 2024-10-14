interface LoaderSliceI {
  isShowLoader: boolean;
  showLoader: () => void;
  hideLoader: () => void;
}

interface UserI {
  token: string | null;
  userData: {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    type: "DOCTOR" | "PATIENT";
  } | null;
}

interface UserSliceI {
  user: UserI | null;
  setUser: (user: UserI) => void;
}
