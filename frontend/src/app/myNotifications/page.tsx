"use client";

import { Card, NotificationList } from "@/components";
import withAuth from "../auth/withAuth";

const MyNotifications = () => {
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

export default withAuth(MyNotifications);
