import { ToastContainer } from "react-toastify";
import { Route, Routes, BrowserRouter } from "react-router-dom";
import { Provider } from "react-redux";
import { AuthRoutes } from "@/features/auth/Routes";
import ProjectRoutes from "@/features/projects/Routes";
import { store } from "@/store";
import ErrorPage from "./ErrorPage";

import "react-toastify/dist/ReactToastify.min.css";
import { ProtectedRoute } from "./components/ProtectedRoute";
import { PublicRoute } from "./components/PublicRoute";
import { FirebaseAuthWrapper } from "./components/FirebaseAuthWrapper";
import IssueRoutes from "./features/issues/Routes";

function App() {
  return (
    <Provider store={store}>
      <BrowserRouter>
        <ToastContainer autoClose={3000} />
        <Routes>
          <Route element={<FirebaseAuthWrapper />}>
            <Route element={<PublicRoute />}>{AuthRoutes()}</Route>
            <Route element={<ProtectedRoute />}>{ProjectRoutes()}</Route>
            <Route element={<ProtectedRoute />}>{IssueRoutes()}</Route>
            <Route path="*" element={<ErrorPage />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </Provider>
  );
}

export default App;
