import { ServiceI } from "@/interfaces/serviceInterface";
import axios from "axios";

const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_URL_BASE || "http://localhost:8080/api/v1",
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true,
});

const signup = async (body: ServiceI["registerUser"]) =>
  api.post("/auth/signup", body);

const services = {
  signup,
};

export default services;
