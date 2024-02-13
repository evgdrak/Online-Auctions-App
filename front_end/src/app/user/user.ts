enum Role{
  Admin, User
}

export interface User {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  address: string;
  city: string;
  country: string;
  tin: string;
  role: Role;
  verified: boolean;
}
