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
import { useEffect, useState } from "react";
import { CalendarHead } from "./CalendarHead";
import { DesktopCalendarBody } from "./desktop/DesktopCalendarBody";
import { MobileCalendarBody } from "./mobile/MobileCalendarBody";

export const Calendar = () => {
  const [windowWidth, setWindowWidth] = useState(0);

  // Función para obtener el ancho de la ventana
  const handleResize = () => {
    setWindowWidth(window.innerWidth);
  };

  // Hook para manejar el cambio de tamaño de la ventana
  useEffect(() => {
    // Establecer el ancho inicial cuando el componente se monta
    setWindowWidth(window.innerWidth);

    // Añadir el event listener para actualizar el ancho cuando la ventana cambie de tamaño
    window.addEventListener("resize", handleResize);

    // Limpiar el event listener cuando el componente se desmonte
    return () => {
      window.removeEventListener("resize", handleResize);
    };
  }, []);

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
          onClick={() => setDay(getFirstDayOfPreviousMonth(day))}
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
          onClick={() => setDay(getFirstDayOfNextMonth(day))}
        >
          <ChevronRightIcon className="h-6 w-6" />
        </button>
      </div>

      <table className="w-full">
        <CalendarHead />

        {windowWidth < 1020 ? (
          <MobileCalendarBody
            daysInMonth={daysInMonth}
            firstDayOfMonthIndex={firstDayOfMonthIndex}
            lastDayOfMonthIndex={lastDayOfMonthIndex}
            lastSixDaysOfPreviousMonth={lastSixDaysOfPreviousMonth}
          />
        ) : (
          <DesktopCalendarBody
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
