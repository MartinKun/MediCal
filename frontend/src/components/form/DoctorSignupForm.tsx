"use client";
import useFormState from "@/hook/useForm";
import { SubmitButton } from "./SubmitButton";
import { InputWithIcon } from "./InputWithIcon";
import { Select } from "./Select";
import { signupInputFields } from "@/util/inputFields";

export const DoctorSignupForm = () => {
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

  return (
    <form className="space-y-6">
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
