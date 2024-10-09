"use client";

import { Card, NotificationList } from "@/components";

export default function MyNotifications() {
  return (
    <section
      className="max-w-2xl
                 mx-auto
                 px-8
                 py-8
                 md:py-20"
    >
      <Card title={"Mis Notificaciones"}>
        <NotificationList />
      </Card>
    </section>
  );
}
