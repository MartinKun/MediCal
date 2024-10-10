"use client";
import { InputWithIcon } from "./InputWithIcon";
import { loginInputFields } from "@/util/inputFields";
import { SubmitButton } from "./SubmitButton";
import useFormState from "@/hook/useForm";

export const LoginForm = () => {
  const { formState, setFormState } = useFormState({ email: "", password: "" });

  return (
    <form className="space-y-6">
      {loginInputFields.map((field, index) => (
        <InputWithIcon
          key={index}
          name={field.name}
          label={field.label}
          type={field.type}
          placeholder={field.placeholder}
          icon={field.icon}
          value={formState[field.name as keyof typeof formState]}
          handleChange={setFormState}
        />
      ))}
      <SubmitButton value={"Iniciar Sesión"} />
    </form>
  );
};
