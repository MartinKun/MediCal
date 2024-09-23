import { generateDaysArray } from "@/util/dateUtils";
import { CalendarDay } from "./CalendarDay";
import { OutOfMonthDay } from "./OutOfMonthDay";

type Props = {
  daysInMonth: string[];
  firstDayOfMonthIndex: number;
  lastDayOfMonthIndex: number;
  lastSixDaysOfPreviousMonth: string[];
};

export const MobileCalendarBody = ({
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
          <CalendarDay index={index} day={day} />
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
