import { LockIcon, MailIcon } from "lucide-react";
import { InputWithIcon } from "./InputWithIcon";

export const LoginForm = () => {
  return (
    <form className="space-y-6">
      <InputWithIcon
        name={"email"}
        label={"Correo Electrónico"}
        type={"email"}
        placeholder={"tu@ejemplo.com"}
        icon={MailIcon}
      />
      <InputWithIcon
        name={"password"}
        label={"Contraseña"}
        type={"email"}
        placeholder={"••••••••"}
        icon={LockIcon}
      />
      <button
        type="submit"
        className="w-full
                   py-2 px-4
                   bg-white
                   bg-opacity-20
                   hover:bg-opacity-30
                   text-white
                   font-semibold
                   rounded-md
                   transition
                   duration-300
                   ease-in-out
                   focus:outline-none
                   focus:ring-2
                   focus:ring-purple-600
                   focus:ring-opacity-50"
      >
        Iniciar Sesión
      </button>
    </form>
  );
};
