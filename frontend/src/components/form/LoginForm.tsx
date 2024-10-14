"use client";
import { InputWithIcon } from "./InputWithIcon";
import { loginInputFields } from "@/util/inputFields";
import { SubmitButton } from "./SubmitButton";
import useFormState from "@/hook/useForm";
import { useBoundStore } from "@/store/store";
import services from "@/services";
import { useRouter } from "next/navigation";

export const LoginForm = () => {
  const showLoader = useBoundStore((state) => state.showLoader);
  const hideLoader = useBoundStore((state) => state.hideLoader);
  const setUser = useBoundStore((state) => state.setUser);
  const { formState, setFormState } = useFormState({ email: "", password: "" });
  const router = useRouter();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      showLoader();
      const response = await services.login(formState);
      if (response) {
        setUser({
          userData: null,
          token: response.data.token,
        });
        router.push("/myAppointments");
      }
    } catch (error) {
      console.error("Login failed:", error);
    } finally {
      hideLoader();
    }
  };

  return (
    <form className="space-y-6" onSubmit={handleSubmit}>
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
