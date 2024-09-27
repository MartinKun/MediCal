"use client";
import { DoctorProfileForm } from "@/components/form/DoctorProfileForm";
import { PatientProfileForm } from "@/components/form/PatientProfileForm";
import { Card } from "@/components/others/Card";

export default function Profile() {
  let userType = "patient";

  return (
    <section
      className="max-w-2xl
                 mx-auto
                 px-8
                 py-8
                 md:py-20"
    >
      <Card title={"Mi perfil"}>
        {userType === "patient" ? (
          <PatientProfileForm />
        ) : (
          <DoctorProfileForm />
        )}
      </Card>
    </section>
  );
}
