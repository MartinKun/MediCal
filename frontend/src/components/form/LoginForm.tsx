"use client";
import { InputWithIcon } from "./InputWithIcon";
import { loginInputFields } from "@/util/inputFields";
import { SubmitButton } from "./SubmitButton";
import useFormState from "@/hook/useForm";
import { useBoundStore } from "@/store/store";
import services from "@/services";
import { useRouter } from "next/navigation";
import { AxiosError } from "axios";

export const LoginForm = () => {
  const showLoader = useBoundStore((state) => state.showLoader);
  const hideLoader = useBoundStore((state) => state.hideLoader);
  const showSuccess = useBoundStore((state) => state.showSuccess);
  const showError = useBoundStore((state) => state.showError);
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
        showSuccess("Has iniciado sesión con éxito!");
      }
    } catch (error) {
      if (error instanceof AxiosError) {
        if (
          error.response?.data.status === 404 ||
          error.response?.data.status === 401
        )
          showError("Usuario o Password incorrectos");
      }
      if (error instanceof AxiosError) {
        if (error.response?.data.status === 403)
          showError(
            "Tu cuenta no está confirmada; revisa tu correo para activarla o solicita un nuevo enlace."
          );
        console.error("Login failed:", error);
        hideLoader();
        return;
      }
      showError(
        "Ha ocurrido un error. Vuelve a intentarlo más tarde o contacta con el equipo de soporte."
      );
      console.error("Login failed:", error);
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
