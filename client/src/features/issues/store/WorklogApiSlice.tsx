import { apiSlice } from "@/store/apiSlice";

export type Worklog = {
  description: string;
  story_points_spent: number;
  started_at: Date;
  user_email: string;
};

type createWorklogPayload = {
  projectCode: string;
  issueCode: string;
  description: string;
  story_points_spent: number;
  started_at: Date;
};

type getWorklogsParams = {
  projectCode: string;
  issueCode: string;
};

const extendedApi = apiSlice
  .enhanceEndpoints({
    addTagTypes: ["comment"],
  })
  .injectEndpoints({
    endpoints: (builder) => ({
      getWorklogs: builder.query<Worklog[], getWorklogsParams>({
        query: (params) => ({
          url: `/api/projects/${params.projectCode}/issues/${params.issueCode}/worklogs`,
        }),
        providesTags: ["comment"],
      }),
      createWorklog: builder.mutation<Worklog, createWorklogPayload>({
        query: (body) => ({
          url: `/api/projects/${body.projectCode}/issues/${body.issueCode}/worklogs`,
          method: "POST",
          body,
        }),
        invalidatesTags: ["comment"],
      }),
    }),
    overrideExisting: false,
  });

export const { useGetWorklogsQuery, useCreateWorklogMutation } = extendedApi;
