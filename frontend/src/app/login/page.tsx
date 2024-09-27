"use client";
import { LoginForm } from "@/components/form/LoginForm";
import { Card } from "@/components/others/Card";
import Link from "next/link";

export default function Login() {
  return (
    <section
      className="max-w-xl
                 mx-auto
                 px-8
                 py-8
                 md:py-20"
    >
      <Card title={"Iniciar Sesión"}>
        <LoginForm />
        <div className="mt-4 text-center">
          <Link
            href="/recoveryPassword"
            className="text-white
                       hover:underline
                       text-sm"
          >
            ¿Olvidaste tu contraseña?
          </Link>
        </div>
      </Card>
    </section>
  );
}
