export type User = {
    id?: number;
    fullName: string;
    email: string;
    password: string;
    status: string;
    role: string
}

export const initialValues: User = {
    fullName: "",
    email: "",
    password: "",
    status: "ACTIVO",
    role: "CONSULTOR"
};

export type Filter = {
    fullName?: string;
    email?: string;
    status?: string;
}