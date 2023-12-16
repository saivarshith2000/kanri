import { toast } from "react-toastify";
import { Project, useGetProjectsQuery } from "./store/projectApiSlice";
import { Spinner } from "@/components/Spinner";
import { Link } from "react-router-dom";
import { CreateProjectDialog } from "./components/CreateProjectDialog";

function RoleBadge({ role }: { role: string }) {
  let color = {
    bgColor: "bg-gray-50",
    ringColor: "ring-gray-500/10",
    textColor: "text-gray-600",
  };
  if (role === "USER") {
    color = {
      bgColor: "bg-blue-50",
      ringColor: "ring-blue-500/10",
      textColor: "text-blue-600",
    };
  } else if (role == "ADMIN") {
    color = {
      bgColor: "bg-orange-50",
      ringColor: "ring-orange-500/10",
      textColor: "text-orange-600",
    };
  } else if (role == "OWNER") {
    color = {
      bgColor: "bg-red-50",
      ringColor: "ring-red-500/10",
      textColor: "text-red-600",
    };
  }

  return (
    <span
      className={`inline-flex items-center rounded-md ${color.bgColor} px-2 py-1 text-xs font-medium ${color.textColor} ring-1 ring-inset ${color.ringColor} w-fit`}
    >
      {role}
    </span>
  );
}

function ProjectCard({ project }: { project: Project }) {
  return (
    <Link to={`/project/${project.code}/issues`}>
      <div className="p-4 rounded-md shadow-sm bg-white hover:shadow-md flex flex-col gap-2 justify-start hover:cursor-pointer w-[250px]">
        <p className="text-2xl truncate">{project.name}</p>
        <p className="font-bold">{project.code}</p>
        <RoleBadge role={project.role} />
      </div>
    </Link>
  );
}

function ProjectList({ projects }: { projects: Project[] }) {
  if (projects.length === 0) {
    return (
      <div className="text-gray-600 text-2xl text-center w-[400px] m-auto mt-16">
        You don't have any projects yet. Create a new one to get started
      </div>
    );
  }
  return (
    <div className="m-auto p-4 grid grid-cols-3 gap-4">
      {projects.map((p) => (
        <ProjectCard key={p.code} project={p} />
      ))}
    </div>
  );
}

export function ProjectListPage() {
  const { data, error, isLoading } = useGetProjectsQuery();
  if (isLoading) {
    return <Spinner text="Loading..." />;
  } else if (error) {
    toast.error("An error occured while fetching your projects");
  } else {
    const ownedProjects = data!.filter((p) => p.role === "OWNER").length;
    const limitReached =
      ownedProjects >= parseInt(import.meta.env.VITE_KANRI_MAX_PROJECT_LIMIT);

    return (
      <div className="flex flex-col w-4/5 m-auto my-4">
        <div className="flex flex-row  justify-between items-center">
          <p className="text-3xl text-gray-800">Your Projects</p>
          <CreateProjectDialog disabled={limitReached} />
        </div>
        <ProjectList projects={data!} />
      </div>
    );
  }
}
