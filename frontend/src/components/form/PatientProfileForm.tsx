import { signupInputFields } from "@/util/inputFields";
import { InputWithIcon } from "./InputWithIcon";
import { SubmitButton } from "./SubmitButton";
import { Select } from "./Select";
import useFormState from "@/hook/useForm";
import { CameraIcon } from "lucide-react";
import { useRef, useState } from "react";
import { AvatarUploader } from "../others/AvatarUploader";

export const PatientProfileForm = () => {
  const { formState, setFormState } = useFormState({
    firstName: "",
    lastName: "",
    birthDate: "",
    gender: "",
    address: "",
    phone: "",
    email: "",
    password: "",
    image: "",
  });

  const [image, setImage] = useState(formState.image);

  return (
    <form className="space-y-6">
      <AvatarUploader image={image} setImage={setImage} />

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
      <SubmitButton value={"Guardar Cambios"} />
    </form>
  );
};
