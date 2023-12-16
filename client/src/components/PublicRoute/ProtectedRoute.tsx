import { isAuthenticatedSelector } from '@/pages/signin/store';
import { useSelector } from 'react-redux';
import { Navigate, Outlet } from 'react-router-dom';

export function PublicRoute() {
  const isAuthenticated = useSelector(isAuthenticatedSelector);

  return !isAuthenticated ? (
    <div className="h-screen bg-gray-100">
      <Outlet />
    </div>
  ) : (
    <Navigate to="projects" />
  );
}
