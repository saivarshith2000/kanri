import { Route } from "react-router-dom";
import { SigninPage } from "./SigninPage";
import { SignupPage } from "./SignupPage";

export function AuthRoutes() {
  return (
    <Route path="auth">
      <Route path="signin" element={<SigninPage />} />
      <Route path="signup" element={<SignupPage />} />
    </Route>
  );
}
