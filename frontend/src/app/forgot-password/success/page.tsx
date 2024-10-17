"use client";
import { ArrowLeft, CheckCircle } from "lucide-react";
import Link from "next/link";

export default function ForgotPasswordSuccess() {
  return (
    <div
      className="fixed
                 inset-0
                 z-[400]
                 flex
                 items-center
                 justify-center"
    >
      <div
        className="absolute
                   inset-0
                   bg-gradient-to-br
                   from-purple-400
                   via-pink-500
                   to-red-500
                   bg-opacity-75
                   backdrop-blur-sm"
      ></div>
      <div
        className="relative
                   w-full
                   h-full
                   flex
                   items-center
                   justify-center
                   bg-white
                   bg-opacity-20
                   backdrop-blur-lg
                   shadow-lg"
      >
        <div
          className="flex
                     flex-col
                     items-center
                     max-w-md
                     px-4 py-8
                     text-center"
        >
          <CheckCircle
            className="w-20 h-20
                       text-green-400
                       mb-6"
          />
          <h1
            className="text-3xl
                       font-bold
                       text-white
                       mb-4"
          >
            Instrucciones Enviadas
          </h1>
          <p
            className="text-xl
                       text-white
                       mb-8"
          >
            Hemos enviado las instrucciones para recuperar tu contraseña a tu
            dirección de correo electrónico. Por favor, revisa tu bandeja de
            entrada y sigue los pasos indicados.
          </p>
          <p
            className="text-md
                       text-white
                       mb-8"
          >
            Si no recibes el correo en unos minutos, revisa tu carpeta de spam o
            correo no deseado.
          </p>
          <Link
            href={"/login"}
            className="flex
                       items-center
                       justify-center
                       px-6 py-3
                       bg-white
                       text-purple-600
                       font-semibold
                       rounded-md
                       shadow-md
                       hover:bg-purple-100
                       transition
                       duration-300
                       ease-in-out"
          >
            <ArrowLeft className="w-5 h-5 mr-2" />
            Volver al Inicio de Sesión
          </Link>
        </div>
      </div>
    </div>
  );
}
