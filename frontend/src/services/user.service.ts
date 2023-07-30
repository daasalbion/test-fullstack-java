import axios from "axios";
import authHeader from "./auth-header";
import {Filter, User} from "../common/User";

const API_URL = "/api/users";

export const getUserBoard = (filterObject: Filter) => {
    let filter: any = {};
    for (const [key, value] of Object.entries(filterObject)) {
        if (value) filter[key] = value;
    }
    console.log(filter);
    return axios.get(API_URL, { params: { filter }, headers: authHeader()});
};

export const createUser = (user: User) => {
    return axios.post(`${API_URL}`, user, {headers: authHeader()});
};

export const updateUser = (id: string | undefined, user: User) => {
    return axios.put(`${API_URL}/${id}`, user, {headers: authHeader()});
};

export const getUser = (id: string | undefined) => {
    console.log(id);
    return axios.get(`${API_URL}/${id}`, {headers: authHeader()});
};

export const deleteUser = (id: number) => {
    return axios.delete(`${API_URL}/${id}`, {headers: authHeader()});
};