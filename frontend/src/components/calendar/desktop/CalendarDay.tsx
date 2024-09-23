import { PlusIcon } from "lucide-react";

type Props = {
  index: number;
  day: string;
};

export const CalendarDay = ({ day, index }: Props) => {
  return (
    <td
      key={index}
      className="border
                 border-gray-200
                 p-2 h-36
                 overflow-y-auto
                 bg-white
                 bg-opacity-20
                 backdrop-blur-sm
                 rounded-lg"
    >
      <div
        className="font-bold
                   mb-1
                   text-white"
      >
        {day}
      </div>
      <button
        className="w-full
                   mt-1
                   text-white
                   hover:bg-white
                   hover:bg-opacity-20
                   hover:text-black
                   flex
                   justify-center
                   py-3
                   rounded-lg"
      >
        <PlusIcon className="h-4 w-4" />
      </button>
    </td>
  );
};
