import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import { Root } from './components';
import { Landing } from './pages/Landing/Landing';
import { Signin } from './pages/signin/Signin';
import { Signup } from './pages/signup/Signup';
import { PublicRoute } from './components/PublicRoute/ProtectedRoute';
import { ProtectedRoute } from './components/ProtectedRoute/ProtectedRoute';

const Projects = () => <>Hello Projects</>;

const router = createBrowserRouter([
  {
    path: '/',
    element: <Root />,
    children: [
      {
        path: '/',
        element: <PublicRoute />,
        children: [
          {
            path: '',
            element: <Landing />,
          },
          {
            path: '/signin',
            element: <Signin />,
          },
          {
            path: '/signup',
            element: <Signup />,
          },
        ],
      },
      {
        path: '/',
        element: <ProtectedRoute />,
        children: [
          {
            path: '/projects',
            element: <Projects />,
          },
        ],
      },
    ],
  },
]);

export function Router() {
  return <RouterProvider router={router} />;
}
