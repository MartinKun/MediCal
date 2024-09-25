import { AppointmentCard } from "@/components/appointment/AppointmentCard";
import { appointmentsData } from "@/util/data";
import { PlusIcon, Trash2Icon } from "lucide-react";

type Props = {
  index: number;
  day: number;
  month: number;
  year: number;
};

export const CalendarDay = ({ day, index, month, year }: Props) => {
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

      {appointmentsData
        .filter(
          (appointment) =>
            appointment.date.getDate() === day &&
            appointment.date.getMonth() === month &&
            appointment.date.getFullYear() === year
        )
        .map((appointment) => (
          <AppointmentCard appointment={appointment} />
        ))}

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
