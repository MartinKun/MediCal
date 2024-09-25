import { generateDaysArray } from "@/util/dateUtils";
import { PlusIcon, Trash2Icon } from "lucide-react";
import { OutOfMonthDay } from "./OutOfMonthDay";
import { CalendarDay } from "./CalendarDay";
import { appointmentsData } from "@/util/data";

type Props = {
  month: number;
  year: number;
  daysInMonth: number[];
  firstDayOfMonthIndex: number;
  lastDayOfMonthIndex: number;
  lastSixDaysOfPreviousMonth: string[];
};

export const DesktopCalendarBody = ({
  month,
  year,
  daysInMonth,
  firstDayOfMonthIndex,
  lastDayOfMonthIndex,
  lastSixDaysOfPreviousMonth,
}: Props) => {
  return (
    <tbody className="w-full">
      <tr
        className="grid
                   grid-cols-7
                   gap-2"
      >
        {firstDayOfMonthIndex > 0 &&
          lastSixDaysOfPreviousMonth
            .slice(-firstDayOfMonthIndex)
            .map((day, index) => <OutOfMonthDay index={index} day={day} />)}

        {daysInMonth.map((day, index) => (
          <CalendarDay index={index} day={day} month={month} year={year} />
        ))}

        {lastDayOfMonthIndex >= 0 &&
          lastDayOfMonthIndex < 6 &&
          generateDaysArray(6 - lastDayOfMonthIndex).map((day, index) => (
            <OutOfMonthDay index={index} day={day} />
          ))}
      </tr>
    </tbody>
  );
};
