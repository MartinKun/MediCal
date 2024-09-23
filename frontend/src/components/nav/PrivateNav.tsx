import { BellIcon, CalendarIcon, UserIcon } from "lucide-react";
import Link from "next/link";
import MenuButton from "./MenuButton";
import { useState } from "react";

export const PrivateNav = () => {
  const [isMenuVisible, setIsMenuVisible] = useState(false);
  return (
    <nav
      className="flex
                 items-center
                 md:space-x-4
                 relative
                 md:w-fit
                 w-full
                 h-[40px]
                 "
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
          href={"/myAppointments"}
        >
          <CalendarIcon className="h-5 w-5 mr-2" />
          Mis citas
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
          href={"/myProfile"}
        >
          <UserIcon className="h-5 w-5 mr-2" />
          Mi Perfil
        </Link>
      </div>
      <Link
        className="text-white
                   hover:bg-white
                   hover:bg-opacity-20
                   hover:text-black
                   px-4
                   py-2
                   rounded-lg
                   md:relative
                   absolute
                   right-0"
        href={"/myNotifications"}
      >
        <BellIcon className="h-5 w-5" />
        <span
          className="absolute
                     top-0 right-0
                     h-2 w-2
                     bg-red-500
                     rounded-full"
        ></span>
      </Link>
    </nav>
  );
};
