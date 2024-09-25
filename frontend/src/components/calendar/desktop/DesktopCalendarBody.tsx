import { generateDaysArray } from "@/util/dateUtils";
import { OutOfMonthDay } from "./OutOfMonthDay";
import { CalendarDay } from "./CalendarDay";
import { AppointmentI } from "@/interfaces/appointmentInterface";

type Props = {
  month: number;
  year: number;
  daysInMonth: number[];
  firstDayOfMonthIndex: number;
  lastDayOfMonthIndex: number;
  lastSixDaysOfPreviousMonth: string[];
  setShowModal: (value: boolean) => void;
  setAppointmentSelected: (value: AppointmentI | undefined) => void;
};

export const DesktopCalendarBody = ({
  month,
  year,
  daysInMonth,
  firstDayOfMonthIndex,
  lastDayOfMonthIndex,
  lastSixDaysOfPreviousMonth,
  setShowModal,
  setAppointmentSelected,
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
            month={month}
            year={year}
            setShowModal={setShowModal}
            setAppointmentSelected={setAppointmentSelected}
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
