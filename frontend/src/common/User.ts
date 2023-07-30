export type User = {
    id?: number;
    fullName: string;
    email: string;
    password: string;
    status: string;
}

export type Filter = {
    fullName?: string;
    email?: string;
    status?: string;
}