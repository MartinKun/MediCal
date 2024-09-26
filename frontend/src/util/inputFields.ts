import { LockIcon, MailIcon } from "lucide-react";

export const loginInputFields = [
    {
        name: "email",
        label: "Correo Electrónico",
        type: "email",
        placeholder: "tu@ejemplo.com",
        icon: MailIcon
    },
    {
        name: "password",
        label: "Contraseña",
        type: "password",
        placeholder: "••••••••",
        icon: LockIcon
    },
];