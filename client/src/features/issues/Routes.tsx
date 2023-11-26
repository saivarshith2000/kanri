import { Route } from "react-router-dom";
import { IssueListPage } from "./IssueListPage";

export default function IssueRoutes() {
  return (
    <Route path="project/:projectCode/">
      <Route path="issues" element={<IssueListPage />} />
    </Route>
  );
}
