import { apiSlice } from "@/store/apiSlice";

export type Comment = {
  id: number;
  content: string;
  user_email: string;
  created_at: string;
};

type createCommentPayload = {
  projectCode: string;
  issueCode: string;
  content: string;
};

type deleteCommentPayload = {
  projectCode: string;
  issueCode: string;
  id: number;
};

type getCommentsParams = {
  projectCode: string;
  issueCode: string;
};

const extendedApi = apiSlice
  .enhanceEndpoints({
    addTagTypes: ["comment"],
  })
  .injectEndpoints({
    endpoints: (builder) => ({
      getComments: builder.query<Comment[], getCommentsParams>({
        query: (params) => ({
          url: `/api/projects/${params.projectCode}/issues/${params.issueCode}/comments`,
        }),
        providesTags: ["comment"],
      }),
      createComment: builder.mutation<Comment, createCommentPayload>({
        query: (body) => ({
          url: `/api/projects/${body.projectCode}/issues/${body.issueCode}/comments`,
          method: "POST",
          body,
        }),
        invalidatesTags: ["comment"],
      }),
      deleteComment: builder.mutation<Comment, deleteCommentPayload>({
        query: (body) => ({
          url: `/api/projects/${body.projectCode}/issues/${body.issueCode}/comments/${body.id}`,
          method: "DELETE",
        }),
        invalidatesTags: ["comment"],
      }),
    }),
    overrideExisting: false,
  });

export const {
  useGetCommentsQuery,
  useCreateCommentMutation,
  useDeleteCommentMutation,
} = extendedApi;
