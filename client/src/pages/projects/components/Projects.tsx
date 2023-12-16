import { Loader, Text } from '@mantine/core';
import { useGetProjectsQuery } from '../store';
import { notifications } from '@mantine/notifications';

export function Projects() {
  const { data: projects, error, isLoading } = useGetProjectsQuery();

  if (isLoading) {
    return <Loader />;
  } else if (error) {
    notifications.show({
      message: "An error occured while fetching your projects",
      color: "red",
      autoClose: 2000
    })
  } else {
    return projects?.map(p => <Text>{p.code}</Text>)
  }
  // const ownedProjects = data!.filter((p) => p.role === "OWNER").length;
  // const limitReached =
  // ownedProjects >= parseInt(import.meta.env.VITE_KANRI_MAX_PROJECT_LIMIT);

  // return (
  //   <div className="flex flex-col w-4/5 m-auto my-4">
  //     <div className="flex flex-row  justify-between items-center">
  //       <p className="text-3xl text-gray-800">Your Projects</p>
  //       <CreateProjectDialog disabled={limitReached} />
  //     </div>
  //     <ProjectList projects={data!} />
  //   </div>
  // );
}