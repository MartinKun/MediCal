import { ArrowLeftIcon } from "lucide-react";
import Link from "next/link";

type Props = {
  backTo?: {
    href: string;
    value: string;
  };
  title?: string;
} & React.ComponentProps<"div">;

export const Card = ({ backTo, title, children }: Props) => {
  return (
    <div
      className="bg-white
                 bg-opacity-20
                 backdrop-blur-lg
                 rounded-xl
                 p-8
                 shadow-lg"
    >
      {backTo && (
        <Link
          href={backTo.href}
          className="text-white
                     hover:text-gray-200
                     transition
                     duration-300
                     ease-in-out
                     flex
                     items-center
                     mb-6"
        >
          <ArrowLeftIcon className="h-5 w-5 mr-2" />
          {backTo.value}
        </Link>
      )}
      <h1
        className="text-3xl
                   font-bold
                   text-white
                   mb-6
                   text-center"
      >
        {title}
      </h1>
      {children}
    </div>
  );
};
