import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { Root } from './components';
import { Landing } from './pages/Landing/Landing';
import { Signin } from './pages/signin/Signin';
import { Signup } from './pages/signup/Signup';
import { PublicRoute } from './components/PublicRoute/ProtectedRoute';
import { ProtectedRoute } from './components/ProtectedRoute/ProtectedRoute';
import { Projects } from './pages/projects/components/Projects';
import { FirebaseAuthWrapper } from './components/FirebaseAuthWrapper/FirebaseAuthWrapper';
import ErrorPage from './pages/ErrorPage/ErrorPage';

export function Router() {
  return <BrowserRouter>
    <Routes>
      <Route element={<FirebaseAuthWrapper />}>
        <Route element={<Root />}>
          <Route element={<PublicRoute />}>
            <Route path="/" element={<Landing />} />
            <Route path="/signin" element={<Signin />} />
            <Route path="/signup" element={<Signup />} />
          </Route>
          <Route element={<ProtectedRoute />}>
            <Route path="/projects" element={<Projects />} />
          </Route>
          <Route path="*" element={<ErrorPage />} />
        </Route>
      </Route>

    </Routes>
  </BrowserRouter>
}
