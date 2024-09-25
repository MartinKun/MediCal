"use client";
import { AppointmentCard } from "@/components/appointment/AppointmentCard";
import { Calendar } from "@/components/calendar/Calendar";
import { appointmentsData } from "@/util/data";
import { useEffect, useState } from "react";

export default function MyAppointments() {
  const [dateSelected, setDateSelected] = useState<{
    day: undefined | number;
    month: number;
    year: number;
  }>({
    day: undefined,
    month: new Date().getMonth(),
    year: new Date().getFullYear(),
  });
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

  return (
    <section
      className="max-w-8xl
                 mx-auto
                 p-8"
    >
      <div
        className="bg-white
                   bg-opacity-20
                   backdrop-blur-lg
                   rounded-xl
                   p-8
                   shadow-lg"
      >
        <h1
          className="text-3xl
                     font-bold
                     text-white
                     mb-6
                     text-center"
        >
          Calendario de Citas Médicas
        </h1>
        <Calendar
          dateSelected={dateSelected}
          setDateSelected={setDateSelected}
          desktopView={windowWidth > 1020}
        />
      </div>
      {windowWidth < 1020 && dateSelected.day && (
        <div
          className="flex
                     flex-col
                     gap-y-6"
        >
          <h2
            className="text-xl
                       font-semibold
                       text-white
                       mt-[32px]"
          >
            Mis citas:
          </h2>

          <ul className="w-full">
            {!appointmentsData.some(
              (appointment) =>
                appointment.date.getDate() === dateSelected.day &&
                appointment.date.getMonth() === dateSelected.month &&
                appointment.date.getFullYear() === dateSelected.year
            ) && (
              <li>
                <h2>No hay citas para este día</h2>
              </li>
            )}

            {dateSelected.day &&
              appointmentsData
                .filter(
                  (appointment) =>
                    appointment.date.getDate() === dateSelected.day &&
                    appointment.date.getMonth() === dateSelected.month &&
                    appointment.date.getFullYear() === dateSelected.year
                )
                .map((appointment) => (
                  <li>
                    <AppointmentCard appointment={appointment} />
                  </li>
                ))}
          </ul>
        </div>
      )}
    </section>
  );
}
