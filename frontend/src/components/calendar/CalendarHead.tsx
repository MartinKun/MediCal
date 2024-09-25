import { daysOfWeek } from "@/util/dateUtils";

export const CalendarHead = () => {
  return (
    <thead
      className="grid
                 grid-cols-7
                 gap-2
                 mb-4"
    >
      {daysOfWeek.map((day) => (
        <th
          key={day}
          className="text-center
                     font-bold
                     text-white"
        >
          {day}
        </th>
      ))}
    </thead>
  );
};
