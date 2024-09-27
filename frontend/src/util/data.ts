import { NotificationI } from "@/interfaces/notificationInterface"

export const appointmentsData = [
    { id: 1, date: new Date(2024, 8, 21, 9, 0), name: "Juan Pérez", chiefComplaintReason: "Revisión anual", address: "Calle Principal 123", image: "/placeholder.svg?height=40&width=40" },
    { id: 2, date: new Date(2024, 8, 25, 11, 30), name: "María García", chiefComplaintReason: "Consulta dermatología", address: "Avenida Central 456", image: "/placeholder.svg?height=40&width=40" },
    { id: 3, date: new Date(2024, 8, 25, 16, 0), name: "Carlos Rodríguez", chiefComplaintReason: "Chequeo cardiológico", address: "Plaza Mayor 789", image: "/placeholder.svg?height=40&width=40" },
]

export const notificationsData: NotificationI[] = [
    { id: 1, type: 'appointment', content: 'Cita con Dr. García mañana a las 10:00 AM', date: '2023-09-26 09:00', isRead: false },
    { id: 2, type: 'reminder', content: 'Tomar medicamento para la presión arterial', date: '2023-09-26 12:00', isRead: false },
    { id: 3, type: 'message', content: 'El Dr. López ha respondido a tu consulta', date: '2023-09-25 15:30', isRead: true },
    { id: 4, type: 'appointment', content: 'Cita de seguimiento con Dra. Martínez el próximo lunes', date: '2023-09-24 11:00', isRead: false },
]