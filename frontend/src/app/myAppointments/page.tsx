"use client";
import { Calendar } from "@/components/calendar/Calendar";

export default function MyAppointments() {
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
        <Calendar />
      </div>
    </section>
  );
}
