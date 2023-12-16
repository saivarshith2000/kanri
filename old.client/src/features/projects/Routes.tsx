import { Route } from "react-router-dom";
import { ProjectListPage } from "./ProjectListPage";

export default function ProjectRoutes() {
  return (
    <Route path="projects">
      <Route path="" index element={<ProjectListPage />} />
    </Route>
  );
}
