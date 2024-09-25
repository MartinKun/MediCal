import { generateDaysArray } from "@/util/dateUtils";
import { CalendarDay } from "./CalendarDay";
import { OutOfMonthDay } from "./OutOfMonthDay";

type Props = {
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
  daysInMonth: number[];
  firstDayOfMonthIndex: number;
  lastDayOfMonthIndex: number;
  lastSixDaysOfPreviousMonth: string[];
};

export const MobileCalendarBody = ({
  dateSelected,
  setDateSelected,
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
          <CalendarDay
            index={index}
            day={day}
            dateSelected={dateSelected}
            setDateSelected={setDateSelected}
          />
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
