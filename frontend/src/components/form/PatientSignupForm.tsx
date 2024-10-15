"use client";
import useFormState from "@/hook/useForm";
import { InputWithIcon } from "./InputWithIcon";
import { SubmitButton } from "./SubmitButton";
import { Select } from "./Select";
import { signupInputFields } from "@/util/inputFields";
import services from "@/services";
import { useBoundStore } from "@/store/store";
import { useRouter } from "next/navigation";

export const PatientSignupForm = () => {
  const showLoader = useBoundStore((state) => state.showLoader);
  const hideLoader = useBoundStore((state) => state.hideLoader);
  const { formState, setFormState } = useFormState({
    firstName: "",
    lastName: "",
    birthDate: "",
    gender: "",
    address: "",
    phone: "",
    email: "",
    password: "",
  });

  const router = useRouter();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const newPatient = {
      firstName: formState.firstName,
      lastName: formState.lastName,
      birthDate: new Date(formState.birthDate),
      gender: formState.gender,
      address: formState.address,
      phone: formState.phone,
      email: formState.email,
      password: formState.password,
      role: "PATIENT",
    };

    try {
      showLoader();
      const response = await services.register(newPatient);
      if (response) {
        router.push("/signup/success");
      } else {
        router.push("/signup/failure");
      }
    } catch (error) {
      router.push("/signup/failure");
      console.error("Signup failed:", error);
    } finally {
      hideLoader();
    }
  };

  return (
    <form className="space-y-6" onSubmit={handleSubmit}>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {signupInputFields
          .filter((_, index) => ![5, 6, 10].includes(index))
          .map((field, index) =>
            field.type === "select" ? (
              <Select
                key={index}
                name={field.name}
                label={field.label}
                options={field.options || []}
                value={formState[field.name as keyof typeof formState]}
                handleChange={setFormState}
                icon={field.icon}
              />
            ) : (
              <InputWithIcon
                key={index}
                name={field.name}
                label={field.label}
                type={field.type}
                placeholder={field.placeholder || ""}
                icon={field.icon}
                value={formState[field.name as keyof typeof formState]}
                handleChange={setFormState}
              />
            )
          )}
      </div>
      <SubmitButton value={"Registrarse"} />
    </form>
  );
};
