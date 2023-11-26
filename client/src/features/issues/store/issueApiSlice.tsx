import { apiSlice } from "@/store/apiSlice";

export type Issue = {
  name: string;
  code: string;
  description: string;
  role: string;
};

export type CreateIssuePayload = {
  projectCode: string;
  name: string;
  code: string;
  description: string;
};

const extendedApi = apiSlice
  .enhanceEndpoints({
    addTagTypes: ["issue"],
  })
  .injectEndpoints({
    endpoints: (builder) => ({
      getIssues: builder.query<Issue[], string>({
        query: (projectCode: string) => ({
          url: `/api/projects/${projectCode}/issues`,
        }),
        providesTags: ["issue"],
      }),
      createIssue: builder.mutation<Issue, CreateIssuePayload>({
        query: (body) => ({
          url: "/api/projects/<PROJECT-CODE>/issues",
          method: "POST",
          body,
        }),
        invalidatesTags: ["issue"],
      }),
    }),
    overrideExisting: false,
  });

export const { useGetIssuesQuery, useCreateIssueMutation } = extendedApi;
