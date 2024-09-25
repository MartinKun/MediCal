import { appointmentsData } from "@/util/data";

type Props = {
  index: number;
  day: number;
  dateSelected: {
    day: undefined | number;
    month: number;
    year: number;
  };
  setDateSelected: (value: {
    day: undefined | number;
    month: number;
    year: number;
  }) => void;
};

export const CalendarDay = ({
  day,
  index,
  dateSelected,
  setDateSelected,
}: Props) => {
  return (
    <td
      key={index}
      className={`col-span-1 
                  w-full
                  h-[36px]
                  text-center
                  text-2xl
                  text-inter
                  flex
                  justify-center
                  text-[#323232]
                  relative
                  ${dateSelected.day === day && "bg-white bg-opacity-20"}`}
    >
      <button
        className={`inline-block
                    whitespace-nowrap
                    px-[4px] relative`}
        onClick={() =>
          setDateSelected({
            ...dateSelected,
            day: day,
          })
        }
      >
        {day}
      </button>
      {appointmentsData.some(
        (appointment) =>
          appointment.date.getDate() === day &&
          appointment.date.getMonth() === dateSelected.month &&
          appointment.date.getFullYear() === dateSelected.year
      ) && (
        <span
          className="absolute
                     top-0 right-0
                     h-2 w-2
                     bg-[#00B4D8]
                     rounded-full"
        ></span>
      )}
    </td>
  );
};
