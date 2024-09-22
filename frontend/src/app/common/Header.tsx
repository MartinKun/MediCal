"use client";

import { PrivateNav } from "@/components/nav/PrivateNav";
import { PublicNav } from "@/components/nav/PublicNav";

const Header: React.FC = () => {
  const user = false;
  return (
    <header>
      <div
        className="bg-white
                   bg-opacity-20
                   backdrop-blur-lg
                   p-4"
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
