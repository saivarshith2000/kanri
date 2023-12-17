import { apiSlice } from '@/store/apiSlice';

export type User = {
  email: string;
  display_name: string;
};

const extendedApi = apiSlice
  .enhanceEndpoints({
    addTagTypes: ['user'],
  })
  .injectEndpoints({
    endpoints: (builder) => ({
      getUsers: builder.query<User[], string>({
        query: (projectCode: string) => ({
          url: `/api/projects/${projectCode}/users`,
        }),
        providesTags: ['user'],
      }),
    }),
    overrideExisting: false,
  });

export const { useGetUsersQuery } = extendedApi;
