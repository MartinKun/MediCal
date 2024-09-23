export const daysOfWeek = [
    "Lun",
    "Mar",
    "Mie",
    "Jue",
    "Vie",
    "Sáb",
    "Dom",
];

export const getMonthName = (date: Date): string => {
    const months = [
        'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
        'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'
    ];

    const monthIndex = date.getMonth();
    return months[monthIndex];
};

export const getYearAsString = (date: Date): string => {
    return date.getFullYear().toString();
};

export const getDaysInMonth = (date: Date): string[] => {
    const year = date.getFullYear();
    const month = date.getMonth();

    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);

    const days: string[] = [];

    for (let day = firstDay; day <= lastDay; day.setDate(day.getDate() + 1)) {
        days.push(day.getDate().toString().padStart(2, '0'));
    }

    return days;
};

export const getFirstDayOfMonthIndex = (date: Date): number => {
    const firstDayOfMonth = new Date(date.getFullYear(), date.getMonth(), 1);

    const dayOfWeek = firstDayOfMonth.getDay();

    return (dayOfWeek === 0 ? 6 : dayOfWeek - 1);
};

export const getLastDayOfMonthIndex = (date: Date): number => {
    const lastDayOfMonth = new Date(date.getFullYear(), date.getMonth() + 1, 0);

    const dayOfWeek = lastDayOfMonth.getDay();

    return (dayOfWeek === 0 ? 6 : dayOfWeek - 1);
};

export const getLastNDaysOfPreviousMonth = (currentDate: Date, n: number): string[] => {
    const firstDayOfCurrentMonth = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1);

    const lastDayOfPreviousMonth = new Date(firstDayOfCurrentMonth.getTime() - 1);

    const lastNDays: string[] = [];

    for (let i = 0; i < n; i++) {
        lastNDays.push(lastDayOfPreviousMonth.getDate().toString().padStart(2, '0'));

        lastDayOfPreviousMonth.setDate(lastDayOfPreviousMonth.getDate() - 1);
    }

    return lastNDays.reverse();
};

export const getFirstDayOfPreviousMonth = (currentDay: Date): Date => {
    const previousMonth = new Date(currentDay);

    previousMonth.setMonth(previousMonth.getMonth() - 1);

    previousMonth.setDate(1);

    return previousMonth;
};

export const getFirstDayOfNextMonth = (currentDay: Date): Date => {
    const nextMonth = new Date(currentDay);

    nextMonth.setMonth(nextMonth.getMonth() + 1);

    nextMonth.setDate(1);

    return nextMonth;
};

export const generateDaysArray = (endIndex: number): string[] => {
    const daysArray = Array.from({ length: endIndex }, (_, i) => i + 1);

    return daysArray.map(day => day.toString().padStart(2, '0'));
};