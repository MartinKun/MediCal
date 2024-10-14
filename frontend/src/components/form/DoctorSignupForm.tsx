"use client";
import useFormState from "@/hook/useForm";
import { SubmitButton } from "./SubmitButton";
import { InputWithIcon } from "./InputWithIcon";
import { Select } from "./Select";
import { signupInputFields } from "@/util/inputFields";
import { useRouter } from "next/navigation";
import services from "@/services";
import { useBoundStore } from "@/store/store";

export const DoctorSignupForm = () => {
  const showLoader = useBoundStore((state) => state.showLoader);
  const hideLoader = useBoundStore((state) => state.hideLoader);
  const { formState, setFormState } = useFormState({
    firstName: "",
    lastName: "",
    birthDate: "",
    gender: "",
    specialty: "",
    license: "",
    phone: "",
    email: "",
    password: "",
    officeAddress: "",
  });

  const router = useRouter();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const newDoctor = {
      firstName: formState.firstName,
      lastName: formState.lastName,
      birthDate: new Date(formState.birthDate),
      gender: formState.gender,
      specialty: formState.specialty,
      license: formState.license,
      phone: formState.phone,
      email: formState.email,
      password: formState.password,
      officeAddress: formState.officeAddress,
      role: "DOCTOR",
    };

    try {
      showLoader();
      const response = await services.signup(newDoctor);
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
          .filter((_, index) => index !== 4)
          .map((field, index) =>
            field.type === "select" ? (
              <Select
                key={index}
                name={field.name}
                label={field.label}
                options={field.options || []}
                icon={field.icon}
                value={formState[field.name as keyof typeof formState]}
                handleChange={setFormState}
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
