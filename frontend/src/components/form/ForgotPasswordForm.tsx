"use client";
import useFormState from "@/hook/useForm";
import { MailIcon } from "lucide-react";
import { InputWithIcon } from "./InputWithIcon";
import { SubmitButton } from "./SubmitButton";
import { useRouter } from "next/navigation";
import { useBoundStore } from "@/store/store";
import services from "@/services";

export const ForgotPasswordForm = () => {
  const { formState, setFormState } = useFormState({ email: "" });
  const showLoader = useBoundStore((state) => state.showLoader);
  const hideLoader = useBoundStore((state) => state.hideLoader);
  const router = useRouter();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      showLoader();
      const response = await services.recoverPass(formState);
      if (response) router.push("/forgot-password/success");
    } catch (error) {
      console.error("Recover password failed:", error);
    } finally {
      hideLoader();
    }
  };

  return (
    <form className="space-y-6" onSubmit={handleSubmit}>
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
