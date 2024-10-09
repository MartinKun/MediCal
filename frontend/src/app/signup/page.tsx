"use client";
import {
  Card,
  DoctorSignupForm,
  PatientSignupForm,
  UserTypeToggle,
} from "@/components";
import { useState } from "react";

export default function Signup() {
  const [userType, setUserType] = useState<"patient" | "doctor">("patient");

  return (
    <section
      className="max-w-2xl
                 mx-auto
                 px-8
                 py-8
                 md:py-20"
    >
      <Card title={"Registrarme"}>
        <UserTypeToggle setUserType={setUserType} userType={userType} />
        {userType === "patient" ? <PatientSignupForm /> : <DoctorSignupForm />}
      </Card>
    </section>
  );
}
