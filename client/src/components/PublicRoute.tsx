import { isAuthenticatedSelector } from "@/features/auth/store";
import { useSelector } from "react-redux";
import { Navigate, Outlet } from "react-router-dom";

export function PublicRoute() {
  const isAuthenticated = useSelector(isAuthenticatedSelector);

  return !isAuthenticated ? <Outlet /> : <Navigate to="/projects" />;
}
