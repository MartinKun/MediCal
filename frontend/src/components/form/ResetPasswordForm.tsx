"use client";

import useFormState from "@/hook/useForm";
import React from "react";
import { InputWithIcon } from "./InputWithIcon";
import { SubmitButton } from "./SubmitButton";
import { resetPassInputFields } from "@/util/inputFields";
import { useBoundStore } from "@/store/store";
import { useRouter } from "next/navigation";
import services from "@/services";

type Props = {
  token: string;
};

export const ResetPasswordForm = ({ token }: Props) => {
  const { formState, setFormState } = useFormState({
    password: "",
    confirmPassword: "",
  });
  const showLoader = useBoundStore((state) => state.showLoader);
  const hideLoader = useBoundStore((state) => state.hideLoader);
  const router = useRouter();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

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
      console.error("Reset password failed:", error);
      router.push(`/reset-password/failure/${token}`);
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
        />
      ))}
      <SubmitButton value={"Resetear Contraseña"} />
    </form>
  );
};
