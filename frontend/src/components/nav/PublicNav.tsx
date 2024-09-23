import { LogInIcon } from "lucide-react";
import Link from "next/link";
import { useState } from "react";
import MenuButton from "./MenuButton";

export const PublicNav = () => {
  const [isMenuVisible, setIsMenuVisible] = useState(false);
  return (
    <nav
      className="flex
                 items-center
                 md:space-x-4
                 relative
                 md:w-fit
                 w-full
                 h-[40px]"
    >
      <MenuButton
        setIsMenuVisible={setIsMenuVisible}
        isMenuVisible={isMenuVisible}
      />
      <div
        className={`${isMenuVisible ? "" : "-translate-x-full md:translate-x-0"}
                     transition-transform
                     transition-opacity
                     ease-in-out
                     flex
                     md:flex-row
                     flex-col
                     md:relative
                     md:space-x-4
                     md:space-y-0
                     space-y-4
                     absolute
                     -left-4
                     -top-4
                     md:top-0
                     pt-[80px]
                     md:pt-0
                     bg-white
                     bg-opacity-30
                     md:bg-none
                     md:bg-opacity-0
                     md:pb-0
                     pb-4
                     px-2
                     md:px-0`}
      >
        <Link
          className="text-white
                     hover:bg-white
                     hover:bg-opacity-20
                     hover:text-black
                     flex
                     px-4
                     py-2
                     rounded-lg"
          href={"/login"}
        >
          <LogInIcon className="h-5 w-5" />
          Iniciar sesión
        </Link>
        <Link
          className="text-white
                     hover:bg-white
                     hover:bg-opacity-20
                     hover:text-black
                     flex
                     px-4
                     py-2
                     rounded-lg"
          href={"/signup"}
        >
          Registrarme
        </Link>
      </div>
    </nav>
  );
};
