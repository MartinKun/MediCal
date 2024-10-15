"use client";

import services from "@/services";
import { Loader2 } from "lucide-react";
import { useRouter } from "next/navigation";
import { useEffect } from "react";

export default function ConfirmUser({ params }: { params: { token: string } }) {
  const router = useRouter();

  useEffect(() => {
    const confirmUser = async () => {
      if (params.token) {
        try {
          const response = await services.confirmUser(params.token);

          if (response) {
            router.push("/confirm-user/success");
          }
        } catch (error) {
          console.error("Confirm user failed:", error);
          router.push("/confirm-user/failure");
        }
      }
    };

    confirmUser();
  }, [params.token, router]);

  return (
    <div
      className="fixed inset-0
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
          <Loader2
            className="w-20 h-20
                       text-white
                       animate-spin
                       mb-6"
          />
          <h1
            className="text-3xl
                       font-bold
                       text-white
                       mb-4"
          >
            Procesando Confirmación
          </h1>
          <p
            className="text-xl
                       text-white
                       mb-8"
          >
            Estamos verificando tu cuenta. Por favor, espera un momento mientras
            procesamos tu solicitud.
          </p>
        </div>
      </div>
    </div>
  );
}
