"use client";
import {
  AddAppointmentForm,
  AppointmentDetail,
  AppointmentsList,
  Calendar,
  Card,
  Modal,
} from "@/components";
import useWindowWidth from "@/hook/useWindowWidth";
import { AppointmentI } from "@/interfaces/appointmentInterface";
import { useState } from "react";
import withAuth from "../auth/withAuth";

const MyAppointments = () => {
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

      <Card title={"Calendario de Citas Médicas"}>
        <Calendar
          setAppointmentSelected={setAppointmentSelected}
          setShowModal={setShowModal}
          dateSelected={dateSelected}
          setDateSelected={setDateSelected}
          desktopView={windowWidth > 1020}
        />
      </Card>
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

export default withAuth(MyAppointments);
