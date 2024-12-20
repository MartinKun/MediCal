import {
  CalendarIcon,
  LockIcon,
  MailIcon,
  PhoneIcon,
  UserCircleIcon,
  UserIcon,
} from "lucide-react";

export const loginInputFields = [
  {
    name: "email",
    label: "Correo Electrónico",
    type: "email",
    placeholder: "tu@ejemplo.com",
    icon: MailIcon,
  },
  {
    name: "password",
    label: "Contraseña",
    type: "password",
    placeholder: "••••••••",
    icon: LockIcon,
  },
];

export const signupInputFields = [
  {
    name: "firstName",
    label: "Nombre",
    type: "text",
    placeholder: "",
    icon: UserIcon,
  },
  {
    name: "lastName",
    label: "Apellido",
    type: "text",
    placeholder: "",
    icon: UserIcon,
  },
  {
    name: "birthDate",
    label: "Fecha de Nacimiento",
    type: "date",
    placeholder: "",
    icon: CalendarIcon,
  },
  {
    name: "gender",
    label: "Género",
    type: "select",
    icon: UserCircleIcon,
    options: [
      {
        value: "",
        label: "Seleccionar",
      },
      {
        value: "MALE",
        label: "Masculino",
      },
      {
        value: "FEMALE",
        label: "Femenino",
      },
      {
        value: "OTHER",
        label: "Otro",
      },
    ],
  },
  {
    name: "address",
    label: "Dirección",
    type: "text",
    placeholder: "",
    icon: PhoneIcon,
  },
  {
    name: "speciality",
    label: "Especialidad",
    type: "text",
    placeholder: "",
    icon: UserIcon,
  },
  {
    name: "license",
    label: "Matrícula",
    type: "text",
    placeholder: "",
    icon: UserIcon,
  },
  {
    name: "phone",
    label: "Teléfono",
    type: "text",
    placeholder: "",
    icon: PhoneIcon,
  },
  {
    name: "email",
    label: "Correo Electrónico",
    type: "email",
    placeholder: "tu@ejemplo.com",
    icon: MailIcon,
  },
  {
    name: "password",
    label: "Contraseña",
    type: "password",
    placeholder: "••••••••",
    icon: LockIcon,
  },
  {
    name: "officeAddress",
    label: "Dirección del Consultorio",
    type: "text",
    placeholder: "",
    icon: PhoneIcon,
  },
];

export const resetPassInputFields = [
  {
    name: "password",
    label: "Nueva Contraseña",
    type: "password",
    placeholder: "••••••••",
    icon: LockIcon,
  },
  {
    name: "confirmPassword",
    label: "Confirmar Contraseña",
    type: "password",
    placeholder: "••••••••",
    icon: LockIcon,
  },
];
