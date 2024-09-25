"use client";
import { AddAppointmentForm } from "@/components/appointment/AddAppointmentForm";
import { AppointmentDetail } from "@/components/appointment/AppointmentDetail";
import { AppointmentsList } from "@/components/appointment/AppointmentsList";
import { Calendar } from "@/components/calendar/Calendar";
import { Modal } from "@/components/modal/Modal";
import useWindowWidth from "@/hook/useWindowWidth";
import { AppointmentI } from "@/interfaces/appointmentInterface";
import { useState } from "react";

export default function MyAppointments() {
  const [showModal, setShowModal] = useState(false);
  const [appointmentSelected, setAppointmentSelected] = useState<
    AppointmentI | undefined
  >(undefined);

  const [dateSelected, setDateSelected] = useState<{
    day: undefined | number;
    month: number;
    year: number;
  }>({
    day: undefined,
    month: new Date().getMonth(),
    year: new Date().getFullYear(),
  });

  const windowWidth = useWindowWidth();

  return (
    <section
      className="max-w-8xl
                 mx-auto
                 p-8"
    >
      {showModal && (
        <Modal setShowModal={setShowModal}>
          <h2
            className="text-xl
                       font-semibold
                       mb-4"
          >
            {appointmentSelected ? "Detalles de la Cita" : "Agregar Nueva Cita"}
          </h2>
          {appointmentSelected ? (
            <AppointmentDetail
              appointment={appointmentSelected}
              setShowModal={setShowModal}
            />
          ) : (
            <AddAppointmentForm setShowModal={setShowModal} />
          )}
        </Modal>
      )}

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
          setAppointmentSelected={setAppointmentSelected}
          setShowModal={setShowModal}
          dateSelected={dateSelected}
          setDateSelected={setDateSelected}
          desktopView={windowWidth > 1020}
        />
      </div>
      {windowWidth < 1020 && (
        <AppointmentsList
          dateSelected={dateSelected}
          setShowModal={setShowModal}
          setAppointmentSelected={setAppointmentSelected}
        />
      )}
    </section>
  );
}
