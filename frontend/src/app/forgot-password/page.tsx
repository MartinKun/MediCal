"use client";
import { Card, ForgotPasswordForm } from "@/components";
import Link from "next/link";

export default function ForgotPassword() {
  return (
    <section
      className="max-w-xl
                 mx-auto
                 px-8
                 py-8
                 md:py-20"
    >
      <Card
        backTo={{ href: "/login", value: "Volver al inicio de sesión" }}
        title={"Recuperar Contraseña"}
      >
        <ForgotPasswordForm />
        <p
          className="mt-6
                     text-center
                     text-white
                     text-sm"
        >
          ¿Recordaste tu contraseña?{" "}
          <Link
            href="/login"
            className="font-medium
                       hover:underline"
          >
            Iniciar sesión
          </Link>
        </p>
      </Card>
    </section>
  );
}
