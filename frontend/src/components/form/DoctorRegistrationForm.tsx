"use client";
import useFormState from "@/hook/useForm";
import { SubmitButton } from "./SubmitButton";
import { InputWithIcon } from "./InputWithIcon";
import { Select } from "./Select";
import { signupInputFields } from "@/util/inputFields";
import { useRouter } from "next/navigation";
import services from "@/services";
import { useBoundStore } from "@/store/store";
import handleErrorsForm from "@/hook/handleErrorsForm";
import { AxiosError } from "axios";

export const DoctorRegistrationForm = () => {
  const showLoader = useBoundStore((state) => state.showLoader);
  const hideLoader = useBoundStore((state) => state.hideLoader);
  const showError = useBoundStore((state) => state.showError);
  const { formState, setFormState, resetForm } = useFormState({
    firstName: "",
    lastName: "",
    birthDate: "",
    gender: "",
    speciality: "",
    license: "",
    phone: "",
    email: "",
    password: "",
    officeAddress: "",
  });

  const router = useRouter();

  const { errors, validateForm } = handleErrorsForm();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (
      validateForm({
        password: formState.password,
        birthDate: formState.birthDate,
      })
    )
      return;

    const newDoctor = {
      firstName: formState.firstName,
      lastName: formState.lastName,
      birthDate: new Date(formState.birthDate),
      gender: formState.gender,
      speciality: formState.speciality,
      license: formState.license,
      phone: formState.phone,
      email: formState.email,
      password: formState.password,
      officeAddress: formState.officeAddress,
      role: "DOCTOR",
    };

    try {
      showLoader();
      const response = await services.register(newDoctor);
      if (response) {
        router.push("/register/success");
      }
    } catch (error) {
      if (error instanceof AxiosError && error.response?.data.status === 409) {
        showError("El email que ingresó ya está registrado.");
        resetForm();
        hideLoader();
        return;
      }
      showError(
        "Ha ocurrido un error. Vuelve a intentarlo más tarde o contacta con el equipo de soporte."
      );
      resetForm();
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
                errorMessage={
                  errors.find((error) => error.value === field.name)?.message
                }
              />
            )
          )}
      </div>
      <SubmitButton value={"Registrarse"} />
    </form>
  );
};
