import { isAuthenticatedSelector } from "@/features/auth/store";
import { useSelector } from "react-redux";
import { Navigate, Outlet } from "react-router-dom";
import { Navbar } from "./Navbar";

export function ProtectedRoute() {
  const isAuthenticated = useSelector(isAuthenticatedSelector);

  return isAuthenticated ? (
    <div className="h-screen bg-gray-100">
      <Navbar />
      <Outlet />
    </div>
  ) : (
    <Navigate to="/auth/signin" />
  );
}
