import { LoginForm } from "@/components/form/LoginForm";
import { Card } from "@/components/others/Card";
import { LockIcon, MailIcon } from "lucide-react";

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
          <a
            href="#"
            className="text-white
                       hover:underline
                       text-sm"
          >
            ¿Olvidaste tu contraseña?
          </a>
        </div>
      </Card>
    </section>
  );
}
