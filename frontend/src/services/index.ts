import { ServiceI } from "@/interfaces/serviceInterface";
import axios, { InternalAxiosRequestConfig } from "axios";

const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_URL_BASE || "http://localhost:8080/api/v1",
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true,
});

const register = async (body: ServiceI["registerUser"]) =>
  api.post("/auth/register", body);

const confirmUser = async (token: string) =>
  api.put("/auth/confirm", null, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

const login = async (body: ServiceI["loginUser"]) =>
  api.post("/auth/login", body);

const services = {
  register,
  confirmUser,
  login,
};

export default services;
