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

const confirmUser = async (body: ServiceI["confirmUser"]) =>
  api.put("/auth/confirm-user", body);

const login = async (body: ServiceI["loginUser"]) =>
  api.post("/auth/login", body);

const recoverPass = async (body: ServiceI["recoverPass"]) =>
  api.put("/auth/forgot-password", body);

const resetPass = async (body: ServiceI["resetPass"]) =>
  api.put("/auth/reset-password", body);

const services = {
  register,
  confirmUser,
  login,
  recoverPass,
  resetPass,
};

export default services;
