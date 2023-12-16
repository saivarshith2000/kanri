import { apiSlice } from '@/store/apiSlice';

export type ProjectRole = 'OWNER' | 'ADMIN' | 'USER';

export type Project = {
  name: string;
  code: string;
  description: string;
  role: ProjectRole;
};

export type CreateProjectPayload = {
  name: string;
  code: string;
  description: string;
};

const extendedApi = apiSlice
  .enhanceEndpoints({
    addTagTypes: ['project'],
  })
  .injectEndpoints({
    endpoints: (builder) => ({
      getProjects: builder.query<Project[], void>({
        query: () => ({ url: '/api/projects/' }),
        providesTags: ['project'],
      }),
      createProject: builder.mutation<Project[], CreateProjectPayload>({
        query: (body) => ({
          url: '/api/projects/',
          method: 'POST',
          body,
        }),
        invalidatesTags: ['project'],
      }),
    }),
    overrideExisting: false,
  });

export const { useGetProjectsQuery, useCreateProjectMutation } = extendedApi;
