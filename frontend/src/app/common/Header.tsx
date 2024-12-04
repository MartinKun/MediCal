"use client";

import { PrivateNav, PublicNav } from "@/components";
import { useBoundStore } from "@/store/store";

const Header: React.FC = () => {
  const user = useBoundStore((state) => state.user)
  return (
    <header>
      <div
        className="bg-white
                   bg-opacity-20
                   backdrop-blur-lg
                   p-4
                   z-[100]
                   relative"
      >
        <div
          className="max-w-7xl
                     mx-auto
                     flex
                     justify-between
                     relative
                     items-center"
        >
          <div
            className="text-2xl
                       font-bold
                       text-white
                       md:relative
                       absolute
                       w-full
                       md:w-min
                       text-center"
          >
            MediCal
          </div>
          {user ? <PrivateNav /> : <PublicNav />}
        </div>
      </div>
    </header>
  );
};

export default Header;
