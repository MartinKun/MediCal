export interface ServiceI {
  registerUser: {
    firstName: string;
    lastName: string;
    birthDate: Date;
    gender: string;
    address?: string;
    specialty?: string;
    officeAddress?: string;
    license?: string;
    phone: string;
    email: string;
    password: string;
  };
}
