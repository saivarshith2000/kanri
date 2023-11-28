import { Route } from "react-router-dom";
import { IssueListPage } from "./IssueListPage";
import IssueDetailsPage from "./IssueDetailsPage";

export default function IssueRoutes() {
  return (
    <Route path="project/:projectCode/">
      <Route path="issues" element={<IssueListPage />} />
      <Route path="issues/:issueCode" element={<IssueDetailsPage />} />
    </Route>
  );
}
