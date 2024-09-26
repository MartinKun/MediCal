"use client";
import { DoctorSignupForm } from "@/components/form/DoctorSignupForm";
import { PatientSignupForm } from "@/components/form/PatientSignupForm";
import { UserTypeToggle } from "@/components/form/UserTypeToggle";
import { Card } from "@/components/others/Card";
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
