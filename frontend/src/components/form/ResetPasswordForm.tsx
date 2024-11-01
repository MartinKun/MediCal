"use client";

import useFormState from "@/hook/useForm";
import React from "react";
import { InputWithIcon } from "./InputWithIcon";
import { SubmitButton } from "./SubmitButton";
import { resetPassInputFields } from "@/util/inputFields";
import { useBoundStore } from "@/store/store";
import { useRouter } from "next/navigation";
import services from "@/services";
import { AxiosError } from "axios";
import handleErrorsForm from "@/hook/handleErrorsForm";

type Props = {
  token: string;
};

export const ResetPasswordForm = ({ token }: Props) => {
  const { formState, setFormState, resetForm } = useFormState({
    password: "",
    confirmPassword: "",
  });
  const showLoader = useBoundStore((state) => state.showLoader);
  const hideLoader = useBoundStore((state) => state.hideLoader);
  const showError = useBoundStore((state) => state.showError);
  const { errors, validateForm } = handleErrorsForm();
  const router = useRouter();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (validateForm(formState)) return;

    try {
      const request = {
        newPassword: formState.password,
        token: token,
      };

      showLoader();
      const response = await services.resetPass(request);
      if (response) {
        router.push("/reset-password/success");
      }
    } catch (error) {
      if (error instanceof AxiosError && error?.response?.data.status === 403) {
        showError(
          "Tu cuenta necesita ser confirmada primero; revisa tu correo para activarla o solicita un nuevo enlace."
        );
        console.error("Reset password failed:", error);
        resetForm();
        hideLoader();
        return;
      }
      showError(
        "Ha ocurrido un error. Vuelve a intentarlo más tarde o contacta con el equipo de soporte."
      );
      resetForm();
      hideLoader();
      console.error("Reset password failed:", error);
    }
  };

  return (
    <form className="space-y-6" onSubmit={handleSubmit}>
      {resetPassInputFields.map((field, index) => (
        <InputWithIcon
          key={index}
          name={field.name}
          label={field.label}
          type={field.type}
          placeholder={field.placeholder}
          icon={field.icon}
          value={formState[field.name as keyof typeof formState]}
          handleChange={setFormState}
          errorMessage={
            errors.find((error) => error.value === field.name)?.message
          }
        />
      ))}
      <SubmitButton value={"Resetear Contraseña"} />
    </form>
  );
};
