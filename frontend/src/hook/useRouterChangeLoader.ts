"use client";

import { useBoundStore } from "@/store/store";
import { usePathname } from "next/navigation";
import { useEffect } from "react";
/**
 * Hook que oculta el loader cuando la ruta cambia.
 *
 * Es útil para situaciones donde se realiza una petición que, una vez completada,
 * redirige a una nueva ruta. Asegura que el loader se mantenga visible hasta que
 * la redirección se haya completado, evitando que se oculte prematuramente.
 */
export function useRouteChangeLoader() {
  const pathname = usePathname();
  const hideLoader = useBoundStore((state) => state.hideLoader);
  const isShowLoader = useBoundStore((state) => state.isShowLoader);
  useEffect(() => {
    if (isShowLoader) hideLoader();
  }, [pathname]);
}
