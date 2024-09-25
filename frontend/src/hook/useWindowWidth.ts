import { useState, useEffect } from "react";

const useWindowWidth = () => {
    const [windowWidth, setWindowWidth] = useState(0);

    const handleResize = () => {
        setWindowWidth(window.innerWidth);
    };

    useEffect(() => {
        // Establecer el ancho inicial cuando el hook se utiliza
        setWindowWidth(window.innerWidth);

        // Añadir el event listener para actualizar el ancho cuando la ventana cambie de tamaño
        window.addEventListener("resize", handleResize);

        // Limpiar el event listener cuando el componente o hook se desmonta
        return () => {
            window.removeEventListener("resize", handleResize);
        };
    }, []);

    return windowWidth;
};

export default useWindowWidth;