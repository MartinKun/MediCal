"use client";

import { ArrowLeft, XCircle } from "lucide-react";
import { useRouter } from "next/navigation";
import React from "react";

export default function ConfirmUserFailure() {
  const router = useRouter();

  const handleVolverRegistro = () => {
    router.push("/signup");
  };

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
          <XCircle
            className="w-20 h-20
                       text-red-400
                       mb-6"
          />
          <h1
            className="text-3xl
                       font-bold
                       text-white
                       mb-4"
          >
            Error de Confirmación
          </h1>
          <p
            className="text-xl
                       text-white
                       mb-8"
          >
            Lo sentimos, ha ocurrido un error al confirmar tu cuenta. Por favor,
            vuelve a la página de registro e intenta nuevamente.
          </p>
          <button
            onClick={handleVolverRegistro}
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
            Volver al Registro
          </button>
        </div>
      </div>
    </div>
  );
}
