import { apiSlice } from "@/store/apiSlice";

export type Issue = {
  summary: string;
  code: string;
  status: "OPEN" | "INPROGRESS" | "CLOSED" | "REJECTED";
  type: "EPIC" | "DEFECT" | "STORY" | "TASK" | "SPIKE";
  priority: "LOW" | "MEDIUM" | "HIGH" | "BLOCKER";
  description: string;
  story_points: number;
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

type getIssueByCodeParams = {
  projectCode: string;
  issueCode: string;
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
      getIssueByCode: builder.query<Issue, getIssueByCodeParams>({
        query: (params) => ({
          url: `/api/projects/${params.projectCode}/issues/${params.issueCode}`,
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

export const {
  useGetIssuesQuery,
  useGetIssueByCodeQuery,
  useCreateIssueMutation,
} = extendedApi;
