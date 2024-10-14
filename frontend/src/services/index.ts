import { ServiceI } from "@/interfaces/serviceInterface";
import axios, { InternalAxiosRequestConfig } from "axios";

const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_URL_BASE || "http://localhost:8080/api/v1",
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true,
});

const signup = async (body: ServiceI["registerUser"]) =>
  api.post("/auth/signup", body);

const confirmUser = async (token: string) =>
  api.put("/auth/confirmUser", null, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

const login = async (body: ServiceI["loginUser"]) =>
  api.post("/auth/login", body);

const services = {
  signup,
  confirmUser,
  login,
};

export default services;
