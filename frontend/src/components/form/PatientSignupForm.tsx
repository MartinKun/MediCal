import useFormState from "@/hook/useForm";
import { InputWithIcon } from "./InputWithIcon";
import { SubmitButton } from "./SubmitButton";
import { Select } from "./Select";
import { signupInputFields } from "@/util/inputFields";

export const PatientSignupForm = () => {
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

  return (
    <form className="space-y-6">
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
