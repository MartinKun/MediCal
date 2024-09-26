type Props = {
  setUserType: (value: "patient" | "doctor") => void;
  userType: "patient" | "doctor";
};

export const UserTypeToggle = ({ setUserType, userType }: Props) => {
  return (
    <div
      className="mb-6
                 flex
                 justify-center
                 space-x-4"
    >
      <button
        onClick={() => setUserType("patient")}
        className={`px-4 py-2
                    rounded-md
                    transition
                    duration-300
                    ease-in-out ${
                      userType === "patient"
                        ? "bg-white text-purple-600"
                        : "text-white hover:bg-white hover:bg-opacity-20"
                    }`}
      >
        Paciente
      </button>
      <button
        onClick={() => setUserType("doctor")}
        className={`px-4 py-2
                    rounded-md
                    transition
                    duration-300
                    ease-in-out ${
                      userType === "doctor"
                        ? "bg-white text-purple-600"
                        : "text-white hover:bg-white hover:bg-opacity-20"
                    }`}
      >
        Médico
      </button>
    </div>
  );
};
