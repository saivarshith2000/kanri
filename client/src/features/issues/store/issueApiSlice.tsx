import { apiSlice } from "@/store/apiSlice";

export type Issue = {
  summary: string;
  code: string;
  status: string;
  priority: string;
  type: string;
  description: string;
};

export type CreateIssuePayload = {
  projectCode: string;
  summary: string;
  description: string;
  type: "EPIC" | "DEFECT" | "STORY" | "TASK" | "SPIKE";
  priority: "LOW" | "MEDIUM" | "HIGH" | "BLOCKER";
  story_points: number;
  epicCode: "TESTA-1";
  assigneeEmail: "user1@dev.com";
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
          url: `/api/projects/${body.projectCode}/issues`,
          method: "POST",
          body,
        }),
        invalidatesTags: ["issue"],
      }),
    }),
    overrideExisting: false,
  });

export const { useGetIssuesQuery, useCreateIssueMutation } = extendedApi;
