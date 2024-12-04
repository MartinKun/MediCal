"use client";

import { Card, DoctorProfileForm, PatientProfileForm } from "@/components";

import React from 'react'
import withAuth from "../auth/withAuth";

const Profile = () => {
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

export default withAuth(Profile);
