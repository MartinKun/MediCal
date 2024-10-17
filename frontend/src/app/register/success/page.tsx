"use client";
import { Card } from "@/components";
import { ArrowLeftIcon, CheckCircleIcon } from "lucide-react";
import Link from "next/link";

export default function RegisterSuccess() {
  return (
    <section
      className="max-w-2xl
                 mx-auto
                 px-8
                 py-8
                 md:py-20"
    >
      <Card>
        <div className="text-center">
          <CheckCircleIcon
            className="mx-auto
                       h-16 w-16
                       text-green-400"
          />
          <h2
            className="mt-4
                       text-3xl
                       font-bold
                       text-white"
          >
            ¡Registro Exitoso!
          </h2>
          <p
            className="mt-2
                       text-xl
                       text-white"
          >
            Se ha enviado un correo de confirmación
          </p>
          <p
            className="mt-4
                       text-white
                       text-opacity-90"
          >
            Hemos enviado un correo electrónico a tu dirección de registro. Por
            favor, revisa tu bandeja de entrada y sigue las instrucciones para
            activar tu cuenta.
          </p>
          <div className="mt-8">
            <p
              className="text-sm
                         text-white
                         text-opacity-80"
            >
              Si no recibes el correo en unos minutos, revisa tu carpeta de spam
              o correo no deseado.
            </p>
          </div>
          <div className="mt-8">
            <Link
              href="/login"
              className="inline-flex
                         items-center
                         px-4 py-2
                         border
                         border-transparent
                         text-base
                         font-medium
                         rounded-md
                         text-purple-600
                         bg-white
                         hover:bg-purple-50
                         focus:outline-none
                         focus:ring-2
                         focus:ring-offset-2
                         focus:ring-purple-500
                         transition
                         duration-150
                         ease-in-out"
            >
              <ArrowLeftIcon className="mr-2 h-5 w-5" />
              Volver al inicio de sesión
            </Link>
          </div>
        </div>
      </Card>
    </section>
  );
}
