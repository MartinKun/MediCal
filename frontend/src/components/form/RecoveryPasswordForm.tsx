import useFormState from "@/hook/useForm";
import { MailIcon } from "lucide-react";
import { InputWithIcon } from "./InputWithIcon";
import { SubmitButton } from "./SubmitButton";

export const RecoveryPasswordForm = () => {
  const { formState, setFormState } = useFormState({ email: "" });
  return (
    <form className="space-y-6">
      <InputWithIcon
        name={"email"}
        label={"Correo Electrónico"}
        type={"email"}
        placeholder={"tu@ejemplo.com"}
        icon={MailIcon}
        value={formState.email}
        handleChange={setFormState}
      />

      <SubmitButton value={"Enviar Instrucciones"} />
    </form>
  );
};
