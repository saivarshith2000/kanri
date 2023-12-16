import { apiSlice } from "@/store/apiSlice";

export type IssueStatus = "OPEN" | "INPROGRESS" | "CLOSED" | "REJECTED";
export type IssueType = "EPIC" | "DEFECT" | "STORY" | "TASK" | "SPIKE";
export type IssuePriority = "LOW" | "MEDIUM" | "HIGH" | "BLOCKER";

export type Issue = {
    summary: string;
    code: string;
    status: IssueStatus;
    type: IssueType;
    priority: IssuePriority;
    description: string;
    story_points: number;
};

export type CreateIssuePayload = {
    projectCode: string;
    summary: string;
    description: string;
    type: IssueType;
    priority: IssuePriority;
    story_points: number;
    epicCode: string;
    assigneeEmail: string;
};

type getIssueByCodeParams = {
    projectCode: string;
    issueCode: string;
};

const extendedApi = apiSlice
    .enhanceEndpoints({
        addTagTypes: ["issue", "epic"],
    })
    .injectEndpoints({
        endpoints: (builder) => ({
            getIssues: builder.query<Issue[], string>({
                query: (projectCode: string) => ({
                    url: `/api/projects/${projectCode}/issues`,
                }),
                providesTags: ["issue"],
            }),
            getEpics: builder.query<Issue[], string>({
                query: (projectCode: string) => ({
                    url: `/api/projects/${projectCode}/epics`,
                }),
                providesTags: ["epic"],
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
    useGetEpicsQuery,
    useGetIssueByCodeQuery,
    useCreateIssueMutation,
} = extendedApi;