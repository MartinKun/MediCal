"use client";

import { Card, ResetPasswordForm } from "@/components";
import Link from "next/link";

export default function ResetPassword({
  params,
}: {
  params: { token: string };
}) {
  return (
    <section
      className="max-w-xl
                 mx-auto
                 px-8
                 py-8
                 md:py-20"
    >
      <Card title={"Resetear Contraseña"}>
        <ResetPasswordForm token={params.token} />
        <div className="mt-4 text-center">
          <Link
            href="/login"
            className="text-white
                       hover:underline
                       text-sm"
          >
            Volver a Inicio de sesión
          </Link>
        </div>
      </Card>
    </section>
  );
}
