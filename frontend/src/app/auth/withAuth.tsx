"use client";

import { Loader } from "@/components";
import { useBoundStore } from "@/store/store";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

/**
 * Higher-Order Component (HOC) que envuelve un componente y verifica si el usuario está autenticado.
 * Si el usuario está autenticado (es decir, tiene un token en su estado global), el componente envuelto se renderiza.
 * Si el usuario no está autenticado, se redirige a la página de login y muestra un Loader mientras tanto.
 *
 * @param WrappedComponent - El componente que será envuelto y protegido por autenticación.
 * @returns Un nuevo componente que verifica la autenticación del usuario antes de renderizar el componente envuelto.
 */

const withAuth = (WrappedComponent: React.ComponentType<{ children: React.ReactNode }>) => {
    const Wrapper = (props: { children: React.ReactNode }) => {
        const router = useRouter();
        const [isAuth, setIsAuth] = useState(false);

        useEffect(() => {
            const user = useBoundStore.getState().user;
            const showError = useBoundStore.getState().showError;
            if (user?.token) {
                setIsAuth(true);
            } else {
                showError("No tiene permiso para acceder a esa ruta.");
                router.push("/login");
            }
        }, [router]);

        return isAuth ? <WrappedComponent {...props} /> : <Loader />;
    };

    return Wrapper;
};

export default withAuth;