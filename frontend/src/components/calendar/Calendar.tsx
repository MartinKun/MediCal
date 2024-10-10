"use client";
import {
  getDaysInMonth,
  getFirstDayOfMonthIndex,
  getFirstDayOfNextMonth,
  getFirstDayOfPreviousMonth,
  getLastDayOfMonthIndex,
  getLastNDaysOfPreviousMonth,
  getMonthName,
  getYearAsString,
} from "@/util/dateUtils";
import { ChevronLeftIcon, ChevronRightIcon } from "lucide-react";
import { useState } from "react";
import { CalendarHead } from "./CalendarHead";
import { DesktopCalendarBody } from "./desktop/DesktopCalendarBody";
import { MobileCalendarBody } from "./mobile/MobileCalendarBody";
import { AppointmentI } from "@/interfaces/appointmentInterface";

type Props = {
  setShowModal: (value: boolean) => void;
  setAppointmentSelected: (value: AppointmentI | undefined) => void;
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
  desktopView: boolean;
};

export const Calendar = ({
  setShowModal,
  setAppointmentSelected,
  dateSelected,
  setDateSelected,
  desktopView,
}: Props) => {
  const [day, setDay] = useState(new Date());

  const daysInMonth = getDaysInMonth(day);
  const firstDayOfMonthIndex = getFirstDayOfMonthIndex(day);
  const lastDayOfMonthIndex = getLastDayOfMonthIndex(day);
  const lastSixDaysOfPreviousMonth = getLastNDaysOfPreviousMonth(day, 6);

  return (
    <div>
      <div
        className="flex mb-4
                   justify-between
                   items-center"
      >
        <button
          className="text-white
                     hover:bg-white
                     hover:bg-opacity-20
                     hover:text-black
                     px-4
                     py-2
                     rounded-lg"
          onClick={() => {
            setDay(getFirstDayOfPreviousMonth(day));
            setDateSelected({
              ...dateSelected,
              day: undefined,
              month: day.getMonth() - 1,
              year: day.getFullYear(),
            });
          }}
        >
          <ChevronLeftIcon className="h-6 w-6" />
        </button>
        <h2
          className="text-xl
                     font-semibold
                     text-white"
        >
          {getMonthName(day)} de {getYearAsString(day)}
        </h2>
        <button
          className="text-white
                     hover:bg-white
                     hover:bg-opacity-20
                     hover:text-black
                     px-4
                     py-2
                     rounded-lg"
          onClick={() => {
            setDay(getFirstDayOfNextMonth(day));
            setDateSelected({
              ...dateSelected,
              day: undefined,
              month: day.getMonth() + 1,
              year: day.getFullYear(),
            });
          }}
        >
          <ChevronRightIcon className="h-6 w-6" />
        </button>
      </div>

      <table className="w-full">
        <CalendarHead />

        {desktopView ? (
          <DesktopCalendarBody
            setAppointmentSelected={setAppointmentSelected}
            setShowModal={setShowModal}
            month={dateSelected.month}
            year={dateSelected.year}
            daysInMonth={daysInMonth}
            firstDayOfMonthIndex={firstDayOfMonthIndex}
            lastDayOfMonthIndex={lastDayOfMonthIndex}
            lastSixDaysOfPreviousMonth={lastSixDaysOfPreviousMonth}
          />
        ) : (
          <MobileCalendarBody
            dateSelected={dateSelected}
            setDateSelected={setDateSelected}
            daysInMonth={daysInMonth}
            firstDayOfMonthIndex={firstDayOfMonthIndex}
            lastDayOfMonthIndex={lastDayOfMonthIndex}
            lastSixDaysOfPreviousMonth={lastSixDaysOfPreviousMonth}
          />
        )}
      </table>
    </div>
  );
};
